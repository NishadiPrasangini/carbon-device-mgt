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
package org.wso2.carbon.device.application.mgt.core.dao;

import org.wso2.carbon.device.application.mgt.common.Application;
import org.wso2.carbon.device.application.mgt.common.ApplicationList;
import org.wso2.carbon.device.application.mgt.common.Filter;
import org.wso2.carbon.device.application.mgt.common.LifecycleStateTransition;
import org.wso2.carbon.device.application.mgt.core.exception.ApplicationManagementDAOException;

import java.util.List;

/**
 * ApplicationDAO is responsible for handling all the Database related operations related with Application Management.
 */
public interface ApplicationDAO {

    /**
     * To create an application.
     *
     * @param application Application that need to be created.
     * @return Created Application.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    Application createApplication(Application application) throws ApplicationManagementDAOException;

    /**
     * To get the applications that satisfy the given criteria.
     *
     * @param filter   Filter criteria.
     * @param tenantId Id of the tenant.
     * @return Application list
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    ApplicationList getApplications(Filter filter, int tenantId) throws ApplicationManagementDAOException;

    /**
     * To get the application with the given uuid
     *
     * @param uuid     UUID of the application to be retrieved.
     * @param tenantId ID of the tenant.
     * @param userName Name of the user.
     * @return the application
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    Application getApplication(String uuid, int tenantId, String userName) throws ApplicationManagementDAOException;

    /**
     * To get the application id of the application specified by the UUID
     *
     * @param uuid     UUID of the application.
     * @param tenantId ID of the tenant.
     * @return ID of the Application.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    int getApplicationId(String uuid, int tenantId) throws ApplicationManagementDAOException;

    /**
     * To edit the given application.
     *
     * @param application Application that need to be edited.
     * @param tenantId    Tenant ID of the Application.
     * @return Updated Application.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    Application editApplication(Application application, int tenantId) throws ApplicationManagementDAOException;

    /**
     * To delete the application identified by the UUID
     *
     * @param uuid     UUID of the application.
     * @param tenantId ID of tenant which the Application belongs to.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    void deleteApplication(String uuid, int tenantId) throws ApplicationManagementDAOException;

    /**
     * To get the application count that satisfies gives search query.
     *
     * @param filter Application Filter.
     * @return count of the applications
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    int getApplicationCount(Filter filter) throws ApplicationManagementDAOException;

    /**
     * To delete the properties of a application.
     *
     * @param applicationId ID of the application to delete the properties.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    void deleteProperties(int applicationId) throws ApplicationManagementDAOException;

    /**
     * To delete the tags of a application.
     *
     * @param applicationId ID of the application to delete the tags.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    void deleteTags(int applicationId) throws ApplicationManagementDAOException;

    /**
     * To change the lifecycle state of the application.
     *
     * @param applicationUUID     UUID of the application.
     * @param lifecycleIdentifier New lifecycle state.
     * @param username            Name of the user.
     * @param tenantId            ID of the tenant.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    void changeLifecycle(String applicationUUID, String lifecycleIdentifier, String username, int tenantId)
            throws ApplicationManagementDAOException;

    /**
     * To get the next possible lifecycle states for the application.
     *
     * @param applicationUUID UUID of the application.
     * @param tenantId        ID of the tenant.
     * @return Next possible lifecycle states.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    List<LifecycleStateTransition> getNextLifeCycleStates(String applicationUUID, int tenantId)
            throws ApplicationManagementDAOException;

    /**
     * To update the screen-shot count of a application.
     *
     * @param applicationUUID UUID of the application.
     * @param tenantId        ID of the tenant.
     * @param count           New count of the screen-shots.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    void updateScreenShotCount(String applicationUUID, int tenantId, int count)
            throws ApplicationManagementDAOException;

    /**
     * To check whether atleast one application exist under category.
     *
     * @param categoryName Name of the category.
     * @return true if atleast one application exist under the given category, otherwise false.
     * @throws ApplicationManagementDAOException Application Management DAO Exception.
     */
    boolean isApplicationExist(String categoryName) throws ApplicationManagementDAOException;
}
