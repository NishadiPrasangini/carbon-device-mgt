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
import './stepper.css';
import Step from "./Step";

class Stepper extends Component {

    render() {
        const {stepContent, activeStep, previousStep} = this.props;
        return (
            <div className="stepper-header row">
                {stepContent.map(content => {
                    return (
                        <Step
                            passed={previousStep}
                            text={content.text}
                            index={content.index}
                            optional={content.optional}
                            active={activeStep === content.index}
                            finalStep={content.index === stepContent.length}
                        />
                    )
                })}
            </div>
        )
    }
}

Stepper.propTypes = {
    stepContent: PropTypes.array,
    activeStep: PropTypes.number
};

export default Stepper;