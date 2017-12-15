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
import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.PaginationRequest;
import org.wso2.carbon.device.application.mgt.common.PaginationResult;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;
import org.wso2.carbon.device.application.mgt.common.exception.TransactionManagementException;
import org.wso2.carbon.device.application.mgt.common.services.*;
import org.wso2.carbon.device.application.mgt.core.dao.CommentDAO;
import org.wso2.carbon.device.application.mgt.core.dao.common.ApplicationManagementDAOFactory;
import org.wso2.carbon.device.application.mgt.core.dao.common.Util;
import org.wso2.carbon.device.application.mgt.core.exception.ApplicationManagementDAOException;
import org.wso2.carbon.device.application.mgt.core.exception.NotFoundException;
import org.wso2.carbon.device.application.mgt.core.util.ConnectionManagerUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
//import java.util.ArrayList;
import java.util.List;

/**
 * This class is the default implementation for the Managing the comments.
 */
@SuppressWarnings( "deprecation" )
public class CommentsManagerImpl implements CommentsManager {

    private static final Log log = LogFactory.getLog(CommentsManagerImpl.class);
    private CommentDAO commentDAO;

    public CommentsManagerImpl() {
        initDataAccessObjects();
    }

    private void initDataAccessObjects() {
        this.commentDAO= ApplicationManagementDAOFactory.getCommentDAO();
    }

//    @Override
//    public int addComment(Comment comment, String createdBy, String appType, String appName, String version)
//            throws CommentManagementException {
//
//        Comment validation= validateComment(comment.getId(),comment.getComment());
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request for comment is received. " + validation.toString());
//        }
//        comment.setCreatedAt(Timestamp.from(Instant.now()));
//        try {
//            ConnectionManagerUtil.beginDBTransaction();
//            ApplicationManagementDAOFactory.getCommentDAO().addComment(comment.getTenantId(),comment, createdBy,
//                    appType,appName,version);
//            ConnectionManagerUtil.commitDBTransaction();
//            return comment.getId();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return comment.getId();
//    }

    @Override
    public Comment addComment(Comment comment,String uuid,int tenantId) throws CommentManagementException {

        Comment validation= validateComment(comment.getId(),comment.getComment());


        if (log.isDebugEnabled()) {
            log.debug("Request for comment is received. " + validation.toString());
        }
        comment.setCreatedAt(Timestamp.from(Instant.now()));

        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().addComment(tenantId,comment,
                    comment.getCreatedBy(),comment.getParent(),uuid);
            ConnectionManagerUtil.commitDBTransaction();
            return comment;
        } catch (Exception e) {
            log.error("Exception occurs.", e);
            ConnectionManagerUtil.rollbackDBTransaction();
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return comment;
    }

    /**
     * To validate the pre-request of the comment
     *
     * @param apAppCommentId ID of the comment.
     * @param comment comment needed to be validate.
     * @return Application related with the UUID.
     *
     *
     */
    private Comment validateComment(int apAppCommentId,String comment) throws CommentManagementException{

        if (apAppCommentId == 0) {
            throw new CommentManagementException("Comment ID is null. Comment id is a required parameter to get the " +
                    "relevant comment.");
        }

        if(comment==null){
            throw new NullPointerException("No comment is entered.");
        }

        try {
            throw new NotFoundException("Comment with comment id "+apAppCommentId+" does not exit");
        } catch (NotFoundException e) {
            log.error("Not Found Exception occurs.", e);
        }
        return getComment(apAppCommentId);
    }

//    @Override
//    public Comment updateComment(int apAppCommentId, String updatedComment, String modifiedBy, Timestamp modifiedAt)
//            throws CommentManagementException, SQLException, DBConnectionException {
//
//        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//        Comment validation= validateComment(apAppCommentId,updatedComment);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Comment retrieval request is received for the comment id " + apAppCommentId );
//        }
//
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().updateComment(apAppCommentId,updatedComment,
//                    modifiedBy,modifiedAt);
//        } catch (SQLException | DBConnectionException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return ApplicationManagementDAOFactory.getCommentDAO().getComment(apAppCommentId);
//    }

//    @Override
//    public List<Comment> getAllComments(String uuid) throws CommentManagementException {
//
//        if (log.isDebugEnabled()) {
//            log.debug("get all comments comments of application uuid "+uuid);
//        }
//
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getAllComments(uuid);
//        } catch (DBConnectionException | SQLException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return null;
//    }

    @Override
    public PaginationResult getAllComments(PaginationRequest request,String uuid) throws CommentManagementException,
            SQLException {

        PaginationResult paginationResult = new PaginationResult();
        List<Comment> comments;
        request = Util.validateCommentListPageSize(request);


        if (log.isDebugEnabled()) {
            log.debug("get all comments of the application release"+uuid);
        }

        try {
            ConnectionManagerUtil.openDBConnection();
            comments=commentDAO.getAllComments(uuid,request);//count ,pagination request
           // count=commentDAO.getCommentCount(request,uuid);
            paginationResult.setData(comments);
            paginationResult.setRecordsFiltered(comments.size());
            paginationResult.setRecordsTotal(comments.size());

            return paginationResult;
        } catch (DBConnectionException e) {
            log.error("DB Connection Exception occurs.", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return paginationResult;
    }

    @Override
    public Comment getComment(int apAppCommentId) throws CommentManagementException {

        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        Comment comment=null;

        if (log.isDebugEnabled()) {
            log.debug("Comment retrieval request is received for the comment id " + apAppCommentId );
        }

        try {
            ConnectionManagerUtil.openDBConnection();
            comment=ApplicationManagementDAOFactory.getCommentDAO().getComment(apAppCommentId);
        } catch (DBConnectionException e) {
            log.error("DB Connection Exception occurs.", e);
        } catch (SQLException e) {
            log.error("SQL Exception occurs.", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return comment;
    }

//    public List<Comment> getComment(String uuid) throws CommentManagementException {
//
//        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//        List<Comment> comments=new ArrayList<>();
//
//        if (log.isDebugEnabled()) {
//            log.debug("Comment retrieval request is received for the application " + uuid);
//        }
//
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            comments=ApplicationManagementDAOFactory.getCommentDAO().getComment(uuid);
//        } catch (DBConnectionException | SQLException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return comments;
//    }

//    @Override
//    public List<Comment> getComments(int appReleasedId, int appId) throws CommentManagementException {
//
//        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve all the comments related with the application "
//                    +appReleasedId+" and "+appId);
//        }
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getComments(appReleasedId,appId);
//        } catch (DBConnectionException e) {
//            log.error("DB Connection Exception occurs.", e);
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return ApplicationManagementDAOFactory.getCommentDAO().getComments(appReleasedId,appId);
//    }

//    @Override
//    public List<Comment> getComments(String appType, String appName, String version) throws CommentManagementException {
//
//        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve all the comments related with the application " + appType+
//                    " , "+appName+" and "+version);
//        }
//
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getComments(appType,appName,version);
//        } catch (SQLException | DBConnectionException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return null;
//    }

//    @Override
//    public List<Comment> getComments(int tenantId) throws CommentManagementException {
//
//        int tenantId1 = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve all the comments related with the application with tenant Id"+tenantId);
//        }
//
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getComments(tenantId);
//        } catch (SQLException | DBConnectionException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return null;
//    }

//    @Override
//    public List<Comment> getCommentsByUser(String createdBy) throws CommentManagementException {
//
//            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//            if (log.isDebugEnabled()) {
//                log.debug("Request is received to retrieve all the comments created by "+createdBy);
//            }
//
//            try {
//                ConnectionManagerUtil.openDBConnection();
//                return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByUser(createdBy);
//            } catch (SQLException | DBConnectionException e) {
//                e.printStackTrace();
//            } finally {
//                ConnectionManagerUtil.closeDBConnection();
//            }
//        return null;
//    }

//    @Override
//    public List<Comment> getCommentsByUser(String createdBy, Timestamp createdAt) throws CommentManagementException {
//
//            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//            if (log.isDebugEnabled()) {
//                log.debug("Request is received to retrieve all the comments created by"+createdBy+", at "
//                        +createdAt+" related with the application ");
//            }
//            try {
//                ConnectionManagerUtil.openDBConnection();
//                return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByUser(createdBy,createdAt);
//            } catch (SQLException | DBConnectionException e) {
//                e.printStackTrace();
//            } finally {
//                ConnectionManagerUtil.closeDBConnection();
//            }
//        return null;
//    }
//
//    @Override
//    public List<Comment> getCommentsByModifiedUser(String modifiedBy) throws CommentManagementException {
//
//            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//            if (log.isDebugEnabled()) {
//                log.debug("Request is received to retrieve all the comments "+modifiedBy+" related with the" +
//                        " application ");
//            }
//            try {
//                ConnectionManagerUtil.openDBConnection();
//                return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByModifiedUser(modifiedBy);
//            } catch (SQLException | DBConnectionException e) {
//                e.printStackTrace();
//            } finally {
//                ConnectionManagerUtil.closeDBConnection();
//            }
//        return null;
//    }
//
//    @Override
//    public List<Comment> getCommentsByModifiedUser(String modifiedBy, Timestamp modifiedAt) throws
//            CommentManagementException {
//
//            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//            if (log.isDebugEnabled()) {
//                log.debug("Request is received to retrieve all the comments modified by "+modifiedBy+" at "
//                        +modifiedAt+" related with the application ");
//            }
//            try {
//                ConnectionManagerUtil.openDBConnection();
//                return ApplicationManagementDAOFactory.getCommentDAO().getCommentsByModifiedUser(modifiedBy,modifiedAt);
//            } catch (SQLException | DBConnectionException e) {
//                e.printStackTrace();
//            } finally {
//                ConnectionManagerUtil.closeDBConnection();
//            }
//        return null;
//    }
//
//    @Override
//    public List<Comment> getComments(String appType, String appName, String version, int parentId) throws
//            CommentManagementException {
//
//            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//            if (log.isDebugEnabled()) {
//                log.debug("Request is received to retrieve all the comments related with the application "
//                        +appType+","+appName+","+version);
//            }
//            try {
//                ConnectionManagerUtil.openDBConnection();
//                return ApplicationManagementDAOFactory.getCommentDAO().getComments(appType,appName,version,parentId);
//            } catch (SQLException | DBConnectionException e) {
//                e.printStackTrace();
//            } finally {
//                ConnectionManagerUtil.closeDBConnection();
//            }
//        return null;
//    }

//    public int getCommentCount(String uuid) throws CommentManagementException {
//
//        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve count of all the comments related with the application "+uuid);
//        }
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getCommentCount(uuid);
//        } catch (SQLException e) {
//            log.error("SQL Exception occurs.", e);
//        } catch (DBConnectionException e) {
//            log.error("DB Connection Exception occurs.", e);
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//        return 0;
//    }

//    @Override
//    public int getCommentCountByApp(int appId, int appReleaseId) throws CommentManagementException,
//            DBConnectionException, SQLException {
//
//        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve count of all the comments related with the application "
//                    +appId+","+appReleaseId);
//        }
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getCommentCountByApp(appId, appReleaseId);
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//
//    }

//    @Override
//    public int getCommentCountByApp(String appType, String appName, String version) throws CommentManagementException,
//            DBConnectionException, SQLException {
//
//        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve count of all the comments related with the application " +
//                    ""+appType+","+appName+","+version);
//        }
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getCommentCountByApp(appType, appName, version);
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//
//    }
//
//    @Override
//    public int getCommentCountByUser(String createdBy) throws CommentManagementException, DBConnectionException,
//            SQLException {
//
//        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve count of all the comments created by "+createdBy+
//                    " related with the application ");
//        }
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getCommentCountByUser(createdBy);
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }
//
//    public int getCommentCountByParent(String uuid,int parentId) throws CommentManagementException, DBConnectionException,
//            SQLException {
//        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Request is received to retrieve count of all the comments of "+parentId+
//                    " related with the application "+uuid);
//        }
//        try {
//            ConnectionManagerUtil.openDBConnection();
//            return ApplicationManagementDAOFactory.getCommentDAO().getCommentCountByParent(uuid,parentId);
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }
//

    @Override
    public void deleteComment(int apAppCommentId) throws CommentManagementException {

        Comment comment;
        comment= getComment(apAppCommentId);

        if (comment == null) {
            throw new CommentManagementException(
                        "Cannot delete a non-existing comment for the application with comment id" + apAppCommentId);
        }
        try {
            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.getCommentDAO().deleteComment(apAppCommentId);
            ConnectionManagerUtil.commitDBTransaction();
        } catch (DBConnectionException e) {
            log.error("DB Connection Exception occurs.", e);
        } catch (SQLException e) {
            log.error("SQL Exception occurs.", e);
        } catch (TransactionManagementException e) {
            log.error("Transaction Management Exception occurs.", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
    }

//    public void deleteComment(String uuid) throws CommentManagementException {
//
//        if (getCommentCount(uuid)== 0) {
//            try {
//                throw new ApplicationManagementException(
//                        "Cannot delete a non-existing comment for the application with application release" + uuid);
//            } catch (ApplicationManagementException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            ConnectionManagerUtil.beginDBTransaction();
//            ApplicationManagementDAOFactory.getCommentDAO().deleteComment(uuid);
//
//            ConnectionManagerUtil.commitDBTransaction();
//        } catch (DBConnectionException | SQLException | TransactionManagementException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }
//
//    @Override
//    public void deleteComments(int appId, int appReleaseID) throws CommentManagementException, DBConnectionException, SQLException {
//
//        if (getCommentCountByApp(appId,appReleaseID) == 0) {
//            try {
//                throw new ApplicationManagementException(
//                        "Cannot delete a non-existing application comments for the application with " +
//                                "application id" + appId+"and release "+appReleaseID);
//            } catch (ApplicationManagementException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            ConnectionManagerUtil.beginDBTransaction();
//            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appId,appReleaseID);
//
//            ConnectionManagerUtil.commitDBTransaction();
//        } catch (DBConnectionException | SQLException | TransactionManagementException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }
//
//    @Override
//    public void deleteComments(String appType, String appName, String version) throws CommentManagementException, DBConnectionException, SQLException {
//
//        if (getCommentCountByApp(appType,appName,version) == 0) {
//            try {
//                throw new ApplicationManagementException(
//                        "Cannot delete a non-existing application comments for the application with application " +
//                                appName+","+appType+","+version);
//            } catch (ApplicationManagementException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            ConnectionManagerUtil.beginDBTransaction();
//            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version);
//
//            ConnectionManagerUtil.commitDBTransaction();
//        } catch (TransactionManagementException | DBConnectionException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }
//
//    @Override
//    public void deleteComments(String appType, String appName, String version, String createdBy) throws
//            CommentManagementException, DBConnectionException, SQLException {
//
//        if (getCommentCountByUser(createdBy)== 0) {
//            try {
//                throw new ApplicationManagementException(
//                        "Cannot delete a non-existing application comments created by "+createdBy+" for the application");
//            } catch (ApplicationManagementException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            ConnectionManagerUtil.beginDBTransaction();
//            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(appType,appName,version,createdBy);
//
//            ConnectionManagerUtil.commitDBTransaction();
//        } catch (TransactionManagementException | DBConnectionException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }
//
//    @Override
//    public void deleteComments(String uuid, int parentId) throws
//            CommentManagementException, DBConnectionException, SQLException {
//
//        if (getCommentCountByParent(uuid,parentId)== 0) {
//            try {
//                throw new ApplicationManagementException(
//                        "Cannot delete a non-existing application comments for parent id "+parentId+" of the " +
//                                "application with application id" + uuid);
//            } catch (ApplicationManagementException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            ConnectionManagerUtil.beginDBTransaction();
//            ApplicationManagementDAOFactory.getCommentDAO().deleteComments(uuid,parentId);
//
//            ConnectionManagerUtil.commitDBTransaction();
//        } catch (TransactionManagementException | DBConnectionException e) {
//            e.printStackTrace();
//        } finally {
//            ConnectionManagerUtil.closeDBConnection();
//        }
//    }

    @Override
    public Comment updateComment(Comment comment,int apAppCommentId) throws CommentManagementException, SQLException,
            DBConnectionException {

        PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId(true);
        validateComment(apAppCommentId,comment.getComment());

        if (log.isDebugEnabled()) {
            log.debug("Comment retrieval request is received for the comment id " +apAppCommentId);
        }
        try {
            ConnectionManagerUtil.openDBConnection();
            ApplicationManagementDAOFactory.getCommentDAO().getComment(apAppCommentId);
            return ApplicationManagementDAOFactory.getCommentDAO().updateComment(apAppCommentId,
                    comment.getComment(),comment.getModifiedBy(),comment.getModifiedAt());
        } catch (SQLException e) {
            log.error("SQL Exception occurs.", e);
        } catch (DBConnectionException e) {
            log.error("DB Connection Exception occurs.", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return ApplicationManagementDAOFactory.getCommentDAO().getComment(apAppCommentId);
    }

    @Override
    public int getStars(String uuid) throws SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Get the average of rated stars for the application "+uuid);
        }

        try {
            ConnectionManagerUtil.openDBConnection();

            return ApplicationManagementDAOFactory.getCommentDAO().getStars(uuid);
        } catch (DBConnectionException e) {
            log.error("DB Connection Exception occurs.", e);
        } catch (ApplicationManagementDAOException e) {
            log.error("Application Management Exception occurs.", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return 0;
    }

    @Override
    public int updateStars(int stars , String uuid) throws ApplicationManagementException {

        if (log.isDebugEnabled()) {
            log.debug("Stars are received for the application " + uuid);
        }
        int totalStars=0;
        try {
            ConnectionManagerUtil.beginDBTransaction();

            int ratedUsers=ApplicationManagementDAOFactory.getCommentDAO().getRatedUser(uuid);
            int newStars=ApplicationManagementDAOFactory.getCommentDAO().updateStars(stars,uuid);
            int oldStars=ApplicationManagementDAOFactory.getCommentDAO().getStars(uuid);

            if(ratedUsers!=0 && newStars!=0) {
                totalStars = newStars + oldStars;
                int avgStars = (totalStars * (ratedUsers - 1)) / ratedUsers;
                ConnectionManagerUtil.commitDBTransaction();

                return avgStars;
            }else {
                totalStars = newStars + oldStars;
                ConnectionManagerUtil.commitDBTransaction();

                return totalStars;
            }
        } catch (ApplicationManagementDAOException e) {
            ConnectionManagerUtil.rollbackDBTransaction();
            log.error("Application Management Exception occurs.", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return totalStars;
    }

    public int getRatedUser(String uuid){

        if (log.isDebugEnabled()) {
            log.debug("Get the rated users for the application release number"+uuid);
        }
        try {
            ConnectionManagerUtil.openDBConnection();

            return ApplicationManagementDAOFactory.getCommentDAO().getRatedUser(uuid);
        } catch (DBConnectionException e) {
            log.error("DB Connection Exception occurs.", e);
        } catch (ApplicationManagementDAOException e) {
            log.error("Application Management Exception occurs.", e);
        } finally {
            ConnectionManagerUtil.closeDBConnection();
        }
        return 0;
    }
}