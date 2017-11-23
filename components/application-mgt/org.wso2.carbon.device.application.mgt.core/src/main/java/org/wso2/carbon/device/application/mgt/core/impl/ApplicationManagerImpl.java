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
package org.wso2.carbon.device.application.mgt.core.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.device.application.mgt.common.Application;
import org.wso2.carbon.device.application.mgt.common.ApplicationList;
import org.wso2.carbon.device.application.mgt.common.Category;
import org.wso2.carbon.device.application.mgt.common.Filter;
import org.wso2.carbon.device.application.mgt.common.Lifecycle;
import org.wso2.carbon.device.application.mgt.common.LifecycleState;
import org.wso2.carbon.device.application.mgt.common.LifecycleStateTransition;
import org.wso2.carbon.device.application.mgt.common.Platform;
import org.wso2.carbon.device.application.mgt.common.User;
import org.wso2.carbon.device.application.mgt.common.Visibility;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;
import org.wso2.carbon.device.application.mgt.common.services.ApplicationManager;
import org.wso2.carbon.device.application.mgt.core.dao.ApplicationDAO;
import org.wso2.carbon.device.application.mgt.core.dao.LifecycleStateDAO;
import org.wso2.carbon.device.application.mgt.core.dao.common.DAOFactory;
import org.wso2.carbon.device.application.mgt.core.exception.ApplicationManagementDAOException;
import org.wso2.carbon.device.application.mgt.core.exception.NotFoundException;
import org.wso2.carbon.device.application.mgt.core.exception.ValidationException;
import org.wso2.carbon.device.application.mgt.core.internal.DataHolder;
import org.wso2.carbon.device.application.mgt.core.util.ConnectionManagerUtil;
import org.wso2.carbon.device.application.mgt.core.util.HelperUtil;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Default Concrete implementation of Application Management related implementations.
 */
public class ApplicationManagerImpl implements ApplicationManager {

    private static final Log log = LogFactory.getLog(ApplicationManagerImpl.class);
    private static final String CREATED = "CREATED";

    @Override
    public Application createApplication(Application application) throws ApplicationManagementException {
        application.setUser(new User(PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername(),
                PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true)));
        if (log.isDebugEnabled()) {
            log.debug("Create Application received for the tenant : " + application.getUser().getTenantId() + " From"
                    + " the user : " + application.getUser().getUserName());
        }
        validateApplication(application);
        application.setUuid(HelperUtil.generateApplicationUuid());
        application.setCreatedAt(new Date());
        application.setModifiedAt(new Date());
        Platform platform = DataHolder.getInstance().getPlatformManager()
                .getPlatform(application.getUser().getTenantId(), application.getPlatform().getIdentifier());
        if (platform == null) {
            throw new NotFoundException("Invalid platform is provided for the application " + application.getUuid());
        }
        application.setPlatform(platform);
        Category category = DataHolder.getInstance().getCategoryManager()
                .getCategory(application.getCategory().getName());
        if (category == null) {
            throw new NotFoundException("Invalid Category is provided for the application " + application.getUuid());
        }
        application.setCategory(category);
        try {
            ConnectionManagerUtil.beginDBTransaction();
            if (log.isDebugEnabled()) {
                log.debug("Application creation pre-conditions are met and the platform mentioned by identifier "
                        + platform.getIdentifier() + " is found");
            }
            LifecycleStateDAO lifecycleStateDAO = DAOFactory.getLifecycleStateDAO();
            LifecycleState lifecycleState = lifecycleStateDAO.getLifeCycleStateByIdentifier(CREATED);
            if (lifecycleState == null) {
                ConnectionManagerUtil.commitDBTransaction();
                throw new NotFoundException("Invalid lifecycle state.");
            }
            Lifecycle lifecycle = new Lifecycle();
            lifecycle.setLifecycleState(lifecycleState);
            lifecycle.setLifecycleStateModifiedAt(new Date());
            lifecycle.setGetLifecycleStateModifiedBy(application.getUser().getUserName());
            application.setCurrentLifecycle(lifecycle);
            application = DAOFactory.getApplicationDAO().createApplication(application);
            DataHolder.getInstance().getVisibilityManager().put(application.getId(), application.getVisibility());
            ConnectionManagerUtil.commitDBTransaction();
            return application;
        } catch (ApplicationManagementException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public Application editApplication(Application application) throws ApplicationManagementException {
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        if (application.getUuid() == null) {
            throw new ValidationException("Application UUID cannot be empty");
        }

        if (!isApplicationOwnerOrAdmin(application.getUuid(), userName, tenantId)) {
            throw new ApplicationManagementException(
                    "User " + userName + " does not have permissions to edit the " + "application with the UUID "
                            + application.getUuid());
        }
        if (this.getApplication(application.getUuid()) != null) {
            if (application.getPlatform() == null || application.getPlatform().getIdentifier() == null) {
                throw new NotFoundException("Platform information not available with the application!");
            }
            Platform platform = DataHolder.getInstance().getPlatformManager()
                    .getPlatform(tenantId, application.getPlatform().getIdentifier());
            if (platform == null) {
                throw new NotFoundException(
                        "Platform specified by identifier " + application.getPlatform().getIdentifier()
                                + " is not found. Please give a valid platform identifier.");
            }
            application.setPlatform(platform);
            if (application.getCategory() != null) {
                String applicationCategoryName = application.getCategory().getName();
                if (applicationCategoryName == null || applicationCategoryName.isEmpty()) {
                    throw new ApplicationManagementException(
                            "Application category name cannot be null or " + "empty. Cannot edit the application.");
                }
                Category category = DataHolder.getInstance().getCategoryManager()
                        .getCategory(application.getCategory().getName());
                if (category == null) {
                    throw new NotFoundException(
                            "Invalid Category is provided for the application " + application.getUuid() + ". "
                                    + "Cannot edit application");
                }
                application.setCategory(category);
            }
            try {
                ConnectionManagerUtil.beginDBTransaction();
                ApplicationDAO applicationDAO = DAOFactory.getApplicationDAO();
                application.setModifiedAt(new Date());
                Application modifiedApplication = applicationDAO.editApplication(application, tenantId);
                Visibility visibility = DataHolder.getInstance().getVisibilityManager()
                        .put(application.getId(), application.getVisibility());
                modifiedApplication.setVisibility(visibility);
                ConnectionManagerUtil.commitDBTransaction();
                return modifiedApplication;
            } catch (ApplicationManagementDAOException e) {
                ConnectionManagerUtil.rollbackDBTransaction();
                throw e;
            } finally {
                ConnectionManagerUtil.closeDBConnection();
            }
        } else {
            throw new NotFoundException("No applications found with application UUID - " + application.getUuid());
        }
    }

    @Override
    public void deleteApplication(String uuid) throws ApplicationManagementException {
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        if (!isApplicationOwnerOrAdmin(uuid, userName, tenantId)) {
            throw new ApplicationManagementException("User '" + userName + "' of tenant - " + tenantId + " does have"
                    + " the permission to delete the application with UUID " + uuid);
        }
        try {
            ApplicationDAO applicationDAO = DAOFactory.getApplicationDAO();
            ConnectionManagerUtil.beginDBTransaction();
            int appId = applicationDAO.getApplicationId(uuid, tenantId);
            if (appId != -1) {
                applicationDAO.deleteTags(appId);
                applicationDAO.deleteProperties(appId);
                DataHolder.getInstance().getVisibilityManager().remove(appId);
                applicationDAO.deleteApplication(uuid, tenantId);
            }
            ConnectionManagerUtil.commitDBTransaction();
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            String msg = "Failed to delete application: " + uuid;
            throw new ApplicationManagementException(msg, e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public ApplicationList getApplications(Filter filter) throws ApplicationManagementException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();

        try {
            if (isAuthorized(userName, tenantId, CarbonConstants.UI_ADMIN_PERMISSION_COLLECTION)) {
                userName = "ALL";
            }
        } catch (UserStoreException e) {
            throw new ApplicationManagementException("User-store exception while checking whether the user " +
                    userName + " of tenant " + tenantId + " has the publisher permission");
        }
        filter.setUserName(userName);

        try {
            ConnectionManagerUtil.openDBConnection();
            ApplicationDAO applicationDAO = DAOFactory.getApplicationDAO();
            ApplicationList applicationList = applicationDAO.getApplications(filter, tenantId);
            for (Application application : applicationList.getApplications()) {
                application.setVisibility(DataHolder.getInstance().getVisibilityManager().get(application.getId()));
            }
            return applicationList;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void changeLifecycle(String applicationUuid, String lifecycleIdentifier) throws
            ApplicationManagementException {
        boolean isAvailableNextState = false;
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        List<LifecycleStateTransition> nextLifeCycles = getLifeCycleStates(applicationUuid);

        for (LifecycleStateTransition lifecycleStateTransition : nextLifeCycles) {
            if (log.isDebugEnabled()) {
                log.debug("Lifecycle state of the application " + applicationUuid + " can be changed to"
                        + lifecycleStateTransition.getNextState());
            }
            if (lifecycleStateTransition.getNextState().equalsIgnoreCase(lifecycleIdentifier)) {
                isAvailableNextState = true;
                break;
            }
        }
        if (!isAvailableNextState) {
            throw new ApplicationManagementException("User " + userName + " does not have the permission to change "
                    + "the lifecycle state of the application " + applicationUuid + " to lifecycle state "
                    + lifecycleIdentifier);
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationDAO applicationDAO = DAOFactory.getApplicationDAO();
            applicationDAO.changeLifecycle(applicationUuid, lifecycleIdentifier, userName, tenantId);
            ConnectionManagerUtil.commitDBTransaction();
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public List<LifecycleStateTransition> getLifeCycleStates(String applicationUUID)
            throws ApplicationManagementException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        boolean isAdminOrApplicationOwner = isApplicationOwnerOrAdmin(applicationUUID, userName, tenantId);

        if (log.isDebugEnabled()) {
            log.debug("User " + userName + " in tenant " + tenantId + " is an Admin or Application owner of the "
                    + "application " + applicationUUID);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            List<LifecycleStateTransition> transitions = DAOFactory.getApplicationDAO()
                    .getNextLifeCycleStates(applicationUUID, tenantId);
            List<LifecycleStateTransition> filteredTransitions = new ArrayList<>();

            if (log.isDebugEnabled()) {
                log.debug("Lifecycle of the application with UUID : " + applicationUUID + " can be changed to "
                        + transitions.size() + ". The number may vary according to the permission level of user : "
                        + userName + " of tenant " + tenantId);
            }
            for (LifecycleStateTransition transition : transitions) {
                String permission = transition.getPermission();
                if (permission != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("In order to make the state change to " + transition.getNextState() + " permission "
                                + permission + "  is required");
                    }
                    if (isAuthorized(userName, tenantId, permission)) {
                        filteredTransitions.add(transition);
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("User " + userName + " does not have the permission " + permission + " to "
                                    + "change the life-cycle state to " + transition.getNextState() + "  of the "
                                    + "application " + applicationUUID);
                        }
                    }
                } else if (isAdminOrApplicationOwner) {
                    filteredTransitions.add(transition);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("User " + userName + " can do " + filteredTransitions.size() + " life-cyle state changes "
                        + "currently on application with the UUID " + applicationUUID);
            }
            return filteredTransitions;
        } catch (UserStoreException e) {
            throw new ApplicationManagementException(
                    "Userstore exception while checking whether user " + userName + " from tenant " + tenantId
                            + " is authorized to do a life-cycle status change in an application ", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public Application getApplication(String uuid) throws ApplicationManagementException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();

        try {
            if (isAuthorized(userName, tenantId, CarbonConstants.UI_ADMIN_PERMISSION_COLLECTION)) {
                userName = "ALL";
            }
        } catch (UserStoreException e) {
            throw new ApplicationManagementException(
                    "User-store exception while getting application with the UUID " + uuid);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            Application application = DAOFactory.getApplicationDAO().getApplication(uuid, tenantId, userName);
            if (application != null) {
                application.setVisibility(DataHolder.getInstance().getVisibilityManager().get(application.getId()));
            }
            return application;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    /**
     * To check whether current user is application owner or admin.
     *
     * @param applicationUUID UUID of the Application.
     * @return true if the current user is application owner or admin, unless false.
     * @throws ApplicationManagementException Application Management Exception.
     */
    private boolean isApplicationOwnerOrAdmin(String applicationUUID, String userName, int tenantId)
            throws ApplicationManagementException {
        try {
            if (isAuthorized(userName, tenantId, CarbonConstants.UI_ADMIN_PERMISSION_COLLECTION)) {
                return true;
            }
        } catch (UserStoreException e) {
            throw new ApplicationManagementException("Userstore exception while checking whether user is an admin", e);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            Application application = DAOFactory.getApplicationDAO()
                    .getApplication(applicationUUID, tenantId, userName);
            return application.getUser().getUserName().equals(userName)
                    && application.getUser().getTenantId() == tenantId;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    /**
     * To check whether current user has the permission to do some secured operation.
     *
     * @param username   Name of the User.
     * @param tenantId   ID of the tenant.
     * @param permission Permission that need to be checked.
     * @return true if the current user has the permission, otherwise false.
     * @throws UserStoreException UserStoreException
     */
    private boolean isAuthorized(String username, int tenantId, String permission) throws UserStoreException {
        UserRealm userRealm = DataHolder.getInstance().getRealmService().getTenantUserRealm(tenantId);
        return userRealm != null && userRealm.getAuthorizationManager() != null && userRealm.getAuthorizationManager()
                .isUserAuthorized(MultitenantUtils.getTenantAwareUsername(username),
                        permission, CarbonConstants.UI_PERMISSION_ACTION);
    }

    /**
     * To validate the application
     *
     * @param application Application that need to be created
     * @throws ValidationException Validation Exception
     */
    private void validateApplication(Application application) throws ValidationException {

        if (application.getName() == null) {
            throw new ValidationException("Application name cannot be empty");
        }

        if (application.getUser() == null || application.getUser().getUserName() == null ||
                application.getUser().getTenantId() == 0) {
            throw new ValidationException("Username and tenant Id cannot be empty");
        }

        if (application.getCategory() == null || application.getCategory().getName() == null || application
                .getCategory().getName().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }

        if (application.getPlatform() == null || application.getPlatform().getIdentifier() == null) {
            throw new ValidationException("Platform identifier cannot be empty");
        }
        try {
            validateApplicationExistence(application);
        } catch (ApplicationManagementException e) {
            throw new ValidationException("Error occured while validating whether there is already an application " +
                    "registered with same name.", e);
        }
    }

    private void validateApplicationExistence(Application application) throws ApplicationManagementException {
        Filter filter = new Filter();
        filter.setFullMatch(true);
        filter.setSearchQuery(application.getName().trim());
        filter.setOffset(0);
        filter.setLimit(1);

        ApplicationList applicationList = getApplications(filter);
        if (applicationList != null && applicationList.getApplications() != null &&
                !applicationList.getApplications().isEmpty()) {
            throw new ValidationException("Already an application registered with same name - "
                    + applicationList.getApplications().get(0).getName());
        }
    }
}
