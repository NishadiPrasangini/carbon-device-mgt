/*
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */

package org.wso2.carbon.device.application.mgt.common.services;

import org.wso2.carbon.device.application.mgt.common.ImageArtifact;
import org.wso2.carbon.device.application.mgt.common.exception.PlatformStorageManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.ResourceManagementException;

import java.io.InputStream;

/**
 * This class manages all the storage related requirements of Platform.
 */
public interface PlatformStorageManager {
    /**
     * To upload image artifacts related with an Application.
     *
     * @param platformIdentifier Identifier of the platform
     * @param iconFile           Icon File input stream
     * @throws ResourceManagementException Resource Management Exception.
     */
    void uploadIcon(String platformIdentifier, InputStream iconFile) throws ResourceManagementException;

    /**
     * To get the icon for a particular platform.
     *
     * @param platformIdentifier Identifier of the platform.
     * @return the icon for the given platform.
     * @throws PlatformStorageManagementException Platform Storage Management Exception.
     */
    ImageArtifact getIcon(String platformIdentifier) throws PlatformStorageManagementException;

    /**
     * To delete the icon of a particular platform
     *
     * @param platformIdentifier Identifier of the platform to which delete icon.
     * @throws PlatformStorageManagementException PlatformStorageManagement Exception.
     */
    void deleteIcon(String platformIdentifier) throws PlatformStorageManagementException;
}
