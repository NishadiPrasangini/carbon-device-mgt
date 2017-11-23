/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.wso2.carbon.device.application.mgt.core.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.device.application.mgt.common.*;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.services.*;
import org.wso2.carbon.device.application.mgt.core.dao.CommentDAO;
import org.wso2.carbon.device.application.mgt.core.dao.common.ApplicationManagementDAOFactory;
import org.wso2.carbon.device.application.mgt.core.exception.ApplicationManagementDAOException;
import org.wso2.carbon.device.application.mgt.core.exception.NotFoundException;
import org.wso2.carbon.device.application.mgt.core.internal.DataHolder;
import org.wso2.carbon.device.application.mgt.core.util.ConnectionManagerUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static sun.security.pkcs.PKCS8Key.version;

/**
 * This class is the default implementation for the Managing the comments.
 */
public class CommentsManagerImpl implements CommentsManager {

    private static final Log log = LogFactory.getLog(ApplicationManagerImpl.class);
    private CommentDAO commentDAO;

    public CommentsManagerImpl() {
        initDataAccessObjects();
    }
    private void initDataAccessObjects() {
        this.commentDAO= ApplicationManagementDAOFactory.getCommentDAO();

    }

    @Override
    public int addComment(int tenantId, Comment comment, String createdBy, int parentId, int appReleaseId, int appId)  throws CommentManagementException  {
        Comment validation= validateComment(comment.getId(),comment.getComment());
//        validateCommentCreateRequest( comment.getComment(),comment);
        if (log.isDebugEnabled()) {
            log.debug("Comment release request is received for the comment " + validation.toString());
        }
        comment.setCreatedAt(new Date());
        try {
            ConnectionManagerUtil.beginDBTransaction();
            comment.setApplication(comment.getApplication());
           comment = ApplicationManagementDAOFactory.getCommentDAO().getComment(comment.getId());
            ConnectionManagerUtil.commitDBTransaction();
            return comment.getId();
//        } catch (ApplicationManagementDAOException e) {
//            ConnectionManagerUtil.rollbackDBTransaction();
//            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return comment.getId();
    }

    /**
     * To validate the pre-request of the comment
     *
     * @param apAppCommentId ID of the comment.
     * @return Application related with the UUID
     *
     * how should we validate comment.
     */
    private Comment validateComment(int apAppCommentId,String comment) throws CommentManagementException{
        if (apAppCommentId == 0) {
            throw new CommentManagementException("Comment ID is null. Comment id is a required "
                    + "parameter to get the relevant comment.");
        }
        if(comment==null){
            throw new NullPointerException("No comment is entered.");
        }
        Comment comm= null;
        try {
            comm = DataHolder.getInstance().getCommentsManager().getComment(apAppCommentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(comm==null){
            try {
                throw new NotFoundException("Comment with comment id "+apAppCommentId+"does not exit");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        return comm;
    }
//    /**
//     * To validate a create release request to make sure all the pre-conditions satisfied.
//     *
//     * @param applicationUuid    UUID of the Application.
//     * @param applicationRelease ApplicationRelease that need to be created.
//     * @throws ApplicationManagementException Application Management Exception.
//     */
//    private void validateCommentCreateRequest(String applicationUuid, Comment applicationRelease)
//            throws ApplicationManagementException {
//        if (applicationRelease == null || applicationRelease.getVersion() == null) {
//            throw new ApplicationManagementException("ApplicationRelease version name is a mandatory parameter for "
//                    + "creating release. It cannot be found.");
//        }
//        if (getRelease(applicationUuid, applicationRelease.getVersion()) != null) {
//            throw new ApplicationManagementException(
//                    "Application Release for the Application UUID " + applicationUuid + " " + "with the version "
//                            + applicationRelease.getVersion() + " already exists. Cannot create an "
//                            + "application release with the same version.");
//        }
//    }

    @Override
    public boolean updateComment(int apAppCommentId, String updatedComment, String modifiedBy, Timestamp modifiedAt) throws Exception {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment validation= validateComment(apAppCommentId,updatedComment);
        if (log.isDebugEnabled()) {
            log.debug("Comment release retrieval request is received for the comment id " +
                    apAppCommentId + " and version " + version);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().updateComment(apAppCommentId,updatedComment,modifiedBy,modifiedAt);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public List <Comment> getComment() throws Exception {
        return null;
    }

//    @Override
//    public int addComment(int tenantId, Comment comment, String createdBy, int parentId, int appReleaseId, int appId) throws Exception {
//        return 0;
//    }

    @Override
    public int addcomment(Comment comment, String createdBy, String appType, String appName, String version) throws Exception {
        return 0;
    }

    @Override
    public Comment addcomment(Comment comment) throws CommentManagementException {
        Comment validation= validateComment(comment.getId(),comment.getComment());
//        validateCommentCreateRequest( comment.getComment(),comment);
        if (log.isDebugEnabled()) {
            log.debug("Comment release request is received for the comment " + validation.toString());
        }
        comment.setCreatedAt(new Date());
        try {
            ConnectionManagerUtil.beginDBTransaction();
            comment.setApplication(comment.getApplication());
            comment = ApplicationManagementDAOFactory.getCommentDAO().getComment(comment.getId());
            ConnectionManagerUtil.commitDBTransaction();
            return comment;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return comment;
    }

//    @Override
//    public boolean updateComment(int apAppCommentId, String updatedComment, String modifiedBy, Timestamp modifiedAt) throws Exception {
//        return false;
//    }

    @Override
    public Comment getComment(int apAppCommentId) throws Exception {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=new Comment();
        Comment validation= validateComment(apAppCommentId,comment.getComment());
        if (log.isDebugEnabled()) {
            log.debug("Comment retrieval request is received for the comment id " +
                    apAppCommentId + " and version " + version);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().getComment(apAppCommentId);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }


    }



    @Override
    public List<Comment> getComments(int appReleasedId, int appId) throws Exception {

        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request is received to retrieve all the comments related with the application " +
                    comment.toString());
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().getComments(appReleasedId,appId);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version) throws Exception {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request is received to retrieve all the comments related with the application " +
                    comment.toString());
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().getComments(appType,appName,version);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

    }

    @Override
    public List<Comment> getComments(int tenantId) throws Exception {
        int tenantId1 = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request is received to retrieve all the comments related with the application " +
                    comment.toString());
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().getComments(tenantId);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

    }

    @Override
    public List<Comment> getCommentsByUser(String createdBy) throws Exception {

            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
            Comment comment=new Comment();
            Comment validation=validateComment(comment.getId(),comment.getComment());

            if (log.isDebugEnabled()) {
                log.debug("Request is received to retrieve all the comments related with the application " +
                        comment.toString());
            }
            try {
                ConnectionManagerUtil.openDBConnection();
                return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByUser(createdBy);

            } finally {
                ConnectionManagerUtil.closeDBConnection();
            }

        }

    @Override
    public List<Comment> getCommentsByUser(String createdBy, Timestamp createdAt) throws Exception {

            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
            Comment comment=new Comment();
            Comment validation=validateComment(comment.getId(),comment.getComment());

            if (log.isDebugEnabled()) {
                log.debug("Request is received to retrieve all the comments related with the application " +
                        comment.toString());
            }
            try {
                ConnectionManagerUtil.openDBConnection();
                return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByUser(createdBy,createdAt);

            } finally {
                ConnectionManagerUtil.closeDBConnection();
            }

        }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy) throws Exception {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request is received to retrieve all the comments related with the application " +
                    comment.toString());
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByModifiedUser(modifiedBy);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

    }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy, Timestamp modifiedAt) throws Exception {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request is received to retrieve all the comments related with the application " +
                    comment.toString());
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByModifiedUser(modifiedBy,modifiedAt);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version, int parentId) throws Exception {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request is received to retrieve all the comments related with the application " +
                    comment.toString());
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().getComments(appType,appName,version,parentId);

        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

    }

    @Override
    public int getCommentCountByUser(String commUser) throws Exception {
        return 0;
    }

    @Override
    public int getCommentCountByUser(String modifiedBy, Timestamp modifedAt) throws Exception {
        return 0;
    }

    @Override
    public int getCommentCountByApp(int appId, int appReleaseId) throws Exception {
        return 0;
    }

    @Override
    public int getCommentCountByApp(String appType, String appName, String version) throws Exception {
        return 0;
    }

    @Override
    public void deleteComment(int apAppCommentId) throws Exception {
        List<Comment> comments = getComments(apAppCommentId);

        for (Comment comm : comments) {
            deleteComment(apAppCommentId);
        }
    }


    @Override
    public void deleteComments(int appId, int appReleaseID) throws Exception {
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        Comment comment1 = getComment(comment.getId());
        if (comment1 == null) {
            throw new ApplicationManagementException(
                    "Cannot delete a non-existing application comments for the " + "application with application id"
                            + comment.getId());
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appId,appReleaseID);
//            ApplicationManagementDAOFactory.getApplicationReleaseDAO().deleteReleaseProperties(applicationRelease.getId());
            ConnectionManagerUtil.commitDBTransaction();
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version) throws Exception {
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        Comment comment1 = getComment(comment.getId());
        if (comment1 == null) {
            throw new ApplicationManagementException(
                    "Cannot delete a non-existing application comments for the " + "application with application id"
                            + comment.getId());
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version);
//            ApplicationManagementDAOFactory.getApplicationReleaseDAO().deleteReleaseProperties(applicationRelease.getId());
            ConnectionManagerUtil.commitDBTransaction();
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version, String createdBy) throws Exception {
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        Comment comment1 = getComment(comment.getId());
        if (comment1 == null) {
            throw new ApplicationManagementException(
                    "Cannot delete a non-existing application comments for the " + "application with application id"
                            + comment.getId());
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version,createdBy);
//            ApplicationManagementDAOFactory.getApplicationReleaseDAO().deleteReleaseProperties(applicationRelease.getId());
            ConnectionManagerUtil.commitDBTransaction();
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void deleteCommentsByUser(String createdBy, int tenantId) throws Exception {
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        Comment comment1 = getComment(comment.getId());
        if (comment1 == null) {
            throw new ApplicationManagementException(
                    "Cannot delete a non-existing application comments for the " + "application with application id"
                            + comment.getId());
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteCommentsByUser(createdBy,tenantId);
//            ApplicationManagementDAOFactory.getApplicationReleaseDAO().deleteReleaseProperties(applicationRelease.getId());
            ConnectionManagerUtil.commitDBTransaction();
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version, int parentId) throws Exception {
        Comment comment=new Comment();
        Comment validation=validateComment(comment.getId(),comment.getComment());

        Comment comment1 = getComment(comment.getId());
        if (comment1 == null) {
            throw new ApplicationManagementException(
                    "Cannot delete a non-existing application comments for the " + "application with application id"
                            + comment.getId());
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version,parentId);
//            ApplicationManagementDAOFactory.getApplicationReleaseDAO().deleteReleaseProperties(applicationRelease.getId());
            ConnectionManagerUtil.commitDBTransaction();
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }
}

