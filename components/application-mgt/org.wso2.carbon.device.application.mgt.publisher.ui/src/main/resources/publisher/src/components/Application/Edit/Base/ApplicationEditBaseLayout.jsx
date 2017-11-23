/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {Col, Row} from "reactstrap";
import React, {Component} from 'react';
import GeneralInfo from "../GenenralInfo/GeneralInfo";
import ReleaseManager from '../../Release/ReleaseMgtBase/ReleaseManager';
import {FormattedMessage} from 'react-intl';
import ApplicationMgtApi from "../../../../api/applicationMgtApi";

class ApplicationEdit extends Component {

    constructor() {
        super();
        this.getTabContent = this.getTabContent.bind(this);
        this.state = {
            application: {},
            general: "active",
            release: "",
            pkgmgt: "",
            activeTab: 1
        }
    }

    componentWillMount() {

        let appId = window.location.pathname.split("/")[4];
        let response = ApplicationMgtApi.getApplication(appId);

        response.then(res => {
            let data = res.data.applications;
            let application = data.filter(app => {
                return app.uuid === appId;
            });

            this.setState({application: application[0]});
        })
    }


    handleTabClick(event) {
        event.stopPropagation();
        const key = event.target.value;

        switch (key) {
            case "1": {
                this.setState({activeTab: 1, general: "active", release: "", pkgmgt: ""});
                break;
            }
            case "2": {
                this.setState({activeTab: 2, general: "", release: "active", pkgmgt: ""});
                break;
            }
            case "3": {
                this.setState({activeTab: 3, general: "", release: "", pkgmgt: "active"});
                break;
            }
            default: {
                return "No Content";
            }
        }
    }

    getTabContent(tab) {
        switch (tab) {
            case 1: {
                {
                    console.log(this.state.application)
                }
                return <GeneralInfo application={this.state.application}/>
            }
            case 2: {
                return <ReleaseManager/>
            }
            case 3: {
                return ("Step3")
            }
        }
    }

    handleOnBackClick() {
        window.location.href = "/publisher/assets/apps"
    }

    render() {
        return (
            <div className="publisher-card">
                <Row id="application-edit-header">
                    <Col xs="3">
                        <a className="back-to-app" onClick={this.handleOnBackClick.bind(this)}>
                            <i className="fw fw-left-arrow"></i>
                        </a>
                    </Col>
                    <Col>
                        {this.state.application.name}
                    </Col>
                </Row>
                <hr/>
                <Row id="application-edit-main-container">
                    <Col xs="3">
                        <div className="tab">
                            <button className={this.state.general} value={1} onClick={this.handleTabClick.bind(this)}>
                                <FormattedMessage id="General" defaultMessage="General"/>
                            </button>
                            <button className={this.state.release} value={2} onClick={this.handleTabClick.bind(this)}>
                                <FormattedMessage id="App.Releases" defaultMessage="Application Releases"/>
                            </button>
                            <button className={this.state.pkgmgt} value={3} onClick={this.handleTabClick.bind(this)}>
                                <FormattedMessage id="Package.Manager" defaultMessage="Package Manager"/>
                            </button>
                        </div>
                    </Col>
                    <Col xs="9">
                        {/* Application edit content */}
                        <div id="application-edit-content">
                            {this.getTabContent(this.state.activeTab)}
                        </div>
                    </Col>
                </Row>
            </div>
        )
    }
}

export default ApplicationEdit;
