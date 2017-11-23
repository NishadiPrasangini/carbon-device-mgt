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

import PropTypes from 'prop-types';
import React, {Component} from 'react';
import Theme from '../../../theme';

/**
 * Floating Action button.
 * */
class FloatingButton extends Component {

    constructor() {
        super();
        this.scriptId = "floatingButton";
    }

    componentWillMount() {
        /**
         *Loading the theme files based on the the user-preference.
         */
        Theme.insertThemingScripts(this.scriptId);
    }

    componentWillUnmount() {
        Theme.removeThemingScripts(this.scriptId);
    }

    handleClick(event) {
        this.props.onClick(event);
    }

    render() {
        let classes = 'btn-circle ' + this.props.className;
        return (
            <div className={classes} onClick={this.handleClick.bind(this)}>
                <i className="fw fw-add"></i>
            </div>
        )
    }
}

FloatingButton.propTypes = {
    classNames: PropTypes.string,
    onClick: PropTypes.func
};

export default FloatingButton;
