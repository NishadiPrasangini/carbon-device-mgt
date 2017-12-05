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
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;
import org.wso2.carbon.device.application.mgt.common.exception.TransactionManagementException;
import org.wso2.carbon.device.application.mgt.common.services.*;
import org.wso2.carbon.device.application.mgt.core.dao.CommentDAO;
import org.wso2.carbon.device.application.mgt.core.dao.common.ApplicationManagementDAOFactory;
import org.wso2.carbon.device.application.mgt.core.dao.common.DAOFactory;
import org.wso2.carbon.device.application.mgt.core.dao.common.Util;
import org.wso2.carbon.device.application.mgt.core.exception.ApplicationManagementDAOException;
import org.wso2.carbon.device.application.mgt.core.exception.NotFoundException;
import org.wso2.carbon.device.application.mgt.core.util.ConnectionManagerUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static sun.security.pkcs.PKCS8Key.version;

/**
 * This class is the default implementation for the Managing the comments.
 */
public class CommentsManagerImpl implements CommentsManager {

    private static final Log log = LogFactory.getLog(CommentsManagerImpl.class);
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

        if (log.isDebugEnabled()) {
            log.debug("Request for comment is received. " + validation.toString());
        }
//        comment.setCreatedAt(Timestamp.from(Instant.now()));
        try {
            ConnectionManagerUtil.beginDBTransaction();
            comment.setApplication(comment.getApplication());
            Application application=null;

//            String comm=comment.toString();
            ApplicationManagementDAOFactory.getCommentDAO().addComment(tenantId,comment,createdBy,parentId,application.getUuid());
            ConnectionManagerUtil.commitDBTransaction();
            return comment.getId();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return comment.getId();
    }

    @Override
    public int addComment(Comment comment, String createdBy, String appType, String appName, String version) throws CommentManagementException {
        Comment validation= validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request for comment is received. " + validation.toString());
        }
//        comment.setCreatedAt(Timestamp.from(Instant.now()));
        try {
            ConnectionManagerUtil.beginDBTransaction();
            comment.setApplication(comment.getApplication());
            Application application=new Application();

//            String comm=comment.toString();
            ApplicationManagementDAOFactory.getCommentDAO().addComment(comment.getTenantId(),comment,comment.getCreatedBy(),comment.getParent(),application.getUuid());
            ConnectionManagerUtil.commitDBTransaction();
            return comment.getId();

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
     *
     */
    private Comment validateComment(int apAppCommentId,String comment) throws CommentManagementException{
        if (apAppCommentId == 0) {
            throw new CommentManagementException("Comment ID is null. Comment id is a required "
                    + "parameter to get the relevant comment.");
        }
        if(comment==null){
            throw new NullPointerException("No comment is entered.");
        }

            try {
                throw new NotFoundException("Comment with comment id "+apAppCommentId+"does not exit");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

        return getComment(apAppCommentId);
    }

    @Override
    public Comment updateComment(int apAppCommentId, String updatedComment, String modifiedBy, Timestamp modifiedAt) throws CommentManagementException, SQLException, DBConnectionException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment validation= validateComment(apAppCommentId,updatedComment);
        if (log.isDebugEnabled()) {
            log.debug("Comment retrieval request is received for the comment id " +
                    apAppCommentId + " and version " + version);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().updateComment(apAppCommentId,updatedComment,modifiedBy,modifiedAt);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return ApplicationManagementDAOFactory.getCommentDAO().getComment(apAppCommentId);
    }

    @Override
    public List<Comment> getAllComments(String uuid) throws CommentManagementException {

        if (log.isDebugEnabled()) {
            log.debug("get all comments");
        }
        try {
            ConnectionManagerUtil.openDBConnection();

                return ApplicationManagementDAOFactory.getCommentDAO().getAllComments(uuid);


        } catch (DBConnectionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return null;
    }
    @Override
    public PaginationResult getAllComments(PaginationRequest request,String uuid) throws CommentManagementException, SQLException {
        PaginationResult paginationResult = new PaginationResult();
        List<Comment> comments;

        request = Util.validateCommentListPageSize(request);
        int count;
        if (log.isDebugEnabled()) {
            log.debug("get all comments");
        }
        try {

            ConnectionManagerUtil.openDBConnection();
            comments=ApplicationManagementDAOFactory.getCommentDAO().getAllComments(uuid);
            count=commentDAO.getCommentCount(request,uuid);
            paginationResult.setData(comments);
            paginationResult.setRecordsFiltered(count);
            paginationResult.setRecordsTotal(count);

            return paginationResult;


        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return paginationResult;

        }


//    @Override
//    public int addcomment(Comment comment, String createdBy, String appType, String appName, String version) throws CommentManagementException {
//        return 0;
//    }

    @Override
    public Comment addComment(Comment comment) throws CommentManagementException {
        Comment validation= validateComment(comment.getId(),comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Request for comment is received. " + validation.toString());
        }
//        comment.setCreatedAt(Timestamp.from(Instant.now()));
        try {
            ConnectionManagerUtil.beginDBTransaction();
           comment.setApplication(comment.getApplication());
           Application application=new Application();

//            String comm=comment.toString();
             ApplicationManagementDAOFactory.getCommentDAO().addComment(comment.getTenantId(),comment,comment.getCreatedBy(),comment.getParent(),application.getUuid());

            ConnectionManagerUtil.commitDBTransaction();
            return comment;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return comment;
    }



    @Override
    public Comment getComment(int apAppCommentId) throws CommentManagementException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=null;

        if (log.isDebugEnabled()) {
            log.debug("Comment retrieval request is received for the comment id " +
                    apAppCommentId + " and version " + version);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            comment=ApplicationManagementDAOFactory.getCommentDAO().getComment(apAppCommentId);


        } catch (DBConnectionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

return comment;
    }

    public Comment getComment(String uuid) throws CommentManagementException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=null;

        if (log.isDebugEnabled()) {
            log.debug("Comment retrieval request is received for the comment id " +
                    comment.getId() + " and version " + version);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            comment=ApplicationManagementDAOFactory.getCommentDAO().getComment(uuid);


        } catch (DBConnectionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return comment;
    }

    @Override
    public List<Comment> getComments(int appReleasedId, int appId) throws CommentManagementException {

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

        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return getComments(appReleasedId,appId);
    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version) throws CommentManagementException {
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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return null;
    }

    @Override
    public List<Comment> getComments(int tenantId) throws CommentManagementException {
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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return null;
    }

    @Override
    public List<Comment> getCommentsByUser(String createdBy) throws CommentManagementException {

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

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (DBConnectionException e) {
                e.printStackTrace();
            } finally {
                ConnectionManagerUtil.closeDBConnection();
            }

        return null;
    }

    @Override
    public List<Comment> getCommentsByUser(String createdBy, Timestamp createdAt) throws CommentManagementException {

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

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (DBConnectionException e) {
                e.printStackTrace();
            } finally {
                ConnectionManagerUtil.closeDBConnection();
            }

        return null;
    }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy) throws CommentManagementException {
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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return null;
    }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy, Timestamp modifiedAt) throws CommentManagementException {
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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return null;
    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version, int parentId) throws CommentManagementException {
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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }

        return null;
    }

    @Override
    public void deleteComment(int apAppCommentId) throws CommentManagementException {
        Comment comment=null;

        comment= getComment(apAppCommentId);
//        Comment validation=validateComment(apAppCommentId,comment.getComment());
        if (comment == null) {
            try {
                throw new ApplicationManagementException(
                        "Cannot delete a non-existing application comments for the application with application id"
                                + apAppCommentId);
            } catch (ApplicationManagementException e) {
                e.printStackTrace();
            }
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComment(apAppCommentId);

            ConnectionManagerUtil.commitDBTransaction();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TransactionManagementException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    public void deleteComment(String uuid) throws CommentManagementException {
        Comment comment=null;
        Comment validation=validateComment(comment.getId(),comment.getComment());


        comment= getComment(uuid);
        if (comment == null) {
            try {
                throw new ApplicationManagementException(
                        "Cannot delete a non-existing application comments for the application with application id"
                                + comment.getId());
            } catch (ApplicationManagementException e) {
                e.printStackTrace();
            }
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComment(uuid);

            ConnectionManagerUtil.commitDBTransaction();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TransactionManagementException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }


    @Override
    public void deleteComments(int appId, int appReleaseID) throws CommentManagementException {
        Comment comment=null;
        Comment validation=validateComment(comment.getId(),comment.getComment());


        comment= getComment(comment.getId());
        if (comment == null) {
            try {
                throw new ApplicationManagementException(
                        "Cannot delete a non-existing application comments for the application with application id"
                                + comment.getId());
            } catch (ApplicationManagementException e) {
                e.printStackTrace();
            }
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appId,appReleaseID);

            ConnectionManagerUtil.commitDBTransaction();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TransactionManagementException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version) throws CommentManagementException {
        Comment comment=null;
        Comment validation=validateComment(comment.getId(),comment.getComment());


        comment= getComment(comment.getId());
        if (comment == null) {
            try {
                throw new ApplicationManagementException(
                        "Cannot delete a non-existing application comments for the application with application id"
                                + comment.getId());
            } catch (ApplicationManagementException e) {
                e.printStackTrace();
            }
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version);

            ConnectionManagerUtil.commitDBTransaction();

        } catch (TransactionManagementException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version, String createdBy) throws CommentManagementException {
        Comment comment=null;
        Comment validation=validateComment(comment.getId(),comment.getComment());


        comment= getComment(comment.getId());
        if (comment == null) {
            try {
                throw new ApplicationManagementException(
                        "Cannot delete a non-existing application comments for the application with application id"
                                + comment.getId());
            } catch (ApplicationManagementException e) {
                e.printStackTrace();
            }
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version,createdBy);

            ConnectionManagerUtil.commitDBTransaction();

        } catch (TransactionManagementException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version, int parentId) throws CommentManagementException {
        Comment comment=null;
        Comment validation=validateComment(comment.getId(),comment.getComment());


        comment= getComment(comment.getId());;
        if (comment == null) {
            try {
                throw new ApplicationManagementException(
                        "Cannot delete a non-existing application comments for the application with application id"
                                + comment.getId());
            } catch (ApplicationManagementException e) {
                e.printStackTrace();
            }
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version,parentId);

            ConnectionManagerUtil.commitDBTransaction();

        } catch (TransactionManagementException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public Comment updateComment(Comment comment) throws CommentManagementException, SQLException, DBConnectionException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment validation= validateComment(comment.getId(),comment.getComment());
        if (log.isDebugEnabled()) {
            log.debug("Comment retrieval request is received for the comment id " +
                    comment.getId() + " and version " + version);
        }
        try {
            Application application=null;
            ConnectionManagerUtil.openDBConnection();
            return ApplicationManagementDAOFactory.getCommentDAO().updateComment(application.getUuid(),comment.getComment(),comment.getModifiedBy(),comment.getModifiedAt());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return ApplicationManagementDAOFactory.getCommentDAO().getComment(comment.getId());
    }

    //    @Override
//    public int addStars(String version, int appId, int stars, ApplicationRelease applicationRelease) throws ApplicationManagementException {
//        if (log.isDebugEnabled()) {
//            log.debug("Stars are received for the application " + applicationRelease.getId());
//        }
//
//        try {
//
//            ConnectionManagerUtil.beginDBTransaction();
//            int avgStars=(applicationRelease.getStars()*(applicationRelease.getNoOfRatedUsers()-1))/applicationRelease.getNoOfRatedUsers();
////            applicationRelease.setStars(avgStars);
//            DAOFactory.getApplicationReleaseDAO().insertStars(version,appId,avgStars);
//            ConnectionManagerUtil.commitDBTransaction();
//            return avgStars;
//        } catch (ApplicationManagementDAOException e) {
//            ConnectionManagerUtil.rollbackDBTransaction();
//            throw e;
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }

    @Override
    public int updateStars(String version, String appName, int stars , String uuid) throws ApplicationManagementException {
        if (log.isDebugEnabled()) {
            log.debug("Stars are received for the application " + uuid);
        }

        try {
            ConnectionManagerUtil.beginDBTransaction();
            int ratedUsers= ApplicationManagementDAOFactory.getCommentDAO().getRatedUser(version,appName,uuid);
            int totalStars=ApplicationManagementDAOFactory.getCommentDAO().insertStars(version,appName,stars,uuid);
            int avgStars=(totalStars*(ratedUsers-1))/ratedUsers;

            ConnectionManagerUtil.commitDBTransaction();
            return avgStars;
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

    @Override
    public int addStars(String version, String appName, int stars , String uuid) throws ApplicationManagementException {
        if (log.isDebugEnabled()) {
            log.debug("Stars are received for the application " + uuid);
        }

        try {
            ConnectionManagerUtil.beginDBTransaction();
            int ratedUsers=ApplicationManagementDAOFactory.getCommentDAO().getRatedUser(version,appName,uuid);
            int newStars=ApplicationManagementDAOFactory.getCommentDAO().addStars(version,appName,stars,uuid);
            int oldStars=ApplicationManagementDAOFactory.getCommentDAO().getStars(version,appName,uuid);
            int totalStars=newStars+oldStars;
            int avgStars=(totalStars*(ratedUsers-1))/ratedUsers;

            ConnectionManagerUtil.commitDBTransaction();
            return avgStars;
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            throw e;
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }
}

