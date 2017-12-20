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

import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.PaginationRequest;
import org.wso2.carbon.device.application.mgt.common.PaginationResult;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;

import java.sql.SQLException;
import java.util.List;

/**
 * CommentsManager is responsible for handling all the add/update/delete/get operations related with
 *
 */
public interface CommentsManager {

//    /**
//     * To add a comment to a application.
//     *
//     * @param comment comment of the application.
//     * @param createdBy Username of the created person.
//     * @param appType type of the commented application.
//     * @param appName name of the commented application.
//     * @param version version of the commented application.
//     * @return Comment Id
//     * @throws CommentManagementException Exceptions of the comment management.
//     */
//    int addComment(Comment comment,String createdBy,String appType,String appName,String version) throws CommentManagementException;

    /**
     * To add a comment to a application
     *
     * @param comment comment of the application.
     * @param uuid uuid of the application release
     * @param tenantId tenant id of the application
     * @return {@link Comment} Comment added
     * @throws CommentManagementException Exceptions of the comment management.
     */
    Comment addComment(Comment comment,String uuid,int tenantId)throws CommentManagementException;

    /**
     * To validate the pre-request of the comment
     *
     * @param apAppCommentId ID of the comment.
     * @param comment comment needed to be validate.
     * @return validated the comment.
     * @throws CommentManagementException Exceptions of the comment management.
     *
     */
    Boolean validateComment(int apAppCommentId,String comment) throws CommentManagementException;
//
//    /**
//     * To update already added comment.
//     *
//     * @param apAppCommentId id of the comment
//     * @param updatedComment comment after updated
//     * @param modifiedBy Username of the modified person.
//     * @param modifiedAt time of the modification.
//     * @return Updated comment
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception
//     * @throws SQLException sql exception
//     */
//    Comment updateComment(int apAppCommentId, String updatedComment,String modifiedBy, Timestamp modifiedAt) throws CommentManagementException, SQLException, DBConnectionException;

//    /**
//     * To get all the comments of an application
//     *
//     * @return List of comments
//     * @throws CommentManagementException Exceptions of the comment management.
//     */
//    List <Comment> getAllComments(String uuid)throws CommentManagementException;

//    /**
//     * Get all comments to pagination
//     *
//     *@param request Pagination request
//     * @param uuid uuid of the application release
//     * @return {@link PaginationResult} pagination result with starting offSet and limit
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws SQLException SQL Exception
//     */
//    PaginationResult getAllComments(PaginationRequest request,String uuid) throws CommentManagementException, SQLException;

    /**
     * Get all comments to pagination
     *
     * @param request Pagination request
     * @param uuid uuid of the application release
     * @return {@link PaginationResult} pagination result with starting offSet and limit
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws SQLException SQL Exception
     */
    List<Comment> getAllComments(PaginationRequest request, String uuid) throws CommentManagementException, SQLException;


    /**
     * To get the comment with id.
     *
     * @param apAppCommentId id of the comment
     * @return {@link Comment}Comment of the comment id
     * @throws CommentManagementException Exceptions of the comment management.
     */
    Comment getComment(int apAppCommentId)throws CommentManagementException;

//    /**
//     * To get the comment with id.
//     *
//     * @param uuid uuid of the comment
//     * @return List of comment for an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     */
//   List<Comment> getComment(String uuid)throws CommentManagementException;

//    /**
//     * To get list of comments using release id and application id.
//     *
//     * @param appReleasedId Id of the released version of the application.
//     * @param appId id of the commented application.
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     */
//    List<Comment> getComments(int appReleasedId,int appId)throws CommentManagementException;

//    /**
//     * To get list of comments using application type, application name and version of the application.
//     *
//     * @param appType type of the commented application.
//     * @param appName name of the commented application.
//     * @param version version of the commented application.
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     *
//     */
//    List<Comment> getComments(String appType,String appName,String version)throws CommentManagementException;

//    /**
//     * To get list of comments using tenant id.
//     *
//     * @param tenantId tenant id of the commented application
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     *
//     */
//    List<Comment> getComments(int tenantId)throws CommentManagementException;

//    /**
//     * To get list of comments by created user.
//     *
//     * @param createdBy Username of the created person.
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     *
//     */
//    List<Comment> getCommentsByUser(String createdBy)throws CommentManagementException;

//    /**
//     * To get list of comments by created use and created time.
//     *
//     * @param createdBy Username of the created person.
//     * @param createdAt time of the comment created.
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     *
//     */
//    List<Comment> getCommentsByUser(String createdBy,Timestamp createdAt)throws CommentManagementException;

//    /**
//     * To get list of comments by modified users.
//     *
//     * @param modifiedBy Username of the modified person.
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     *
//     */
//    List<Comment> getCommentsByModifiedUser(String modifiedBy)throws CommentManagementException;

//    /**
//     * To get list of comments using modified user's name and modified time.
//     *
//     * @param modifiedBy Username of the modified person.
//     * @param modifiedAt time of the modification
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     *
//     */
//    List<Comment> getCommentsByModifiedUser(String modifiedBy,Timestamp modifiedAt)throws CommentManagementException;

//    /**
//     * To get list of comments using application type, application name , application version and parent id of the comment.
//     *
//     * @param appType type of the commented application.
//     * @param appName name of the commented application.
//     * @param version version of the commented application.
//     * @param parentId parent id of the parent comment.
//     * @return List of comments of an application
//     * @throws CommentManagementException Exceptions of the comment management.
//     *
//     */
//    List<Comment> getComments(String appType,String appName,String version,int parentId)throws CommentManagementException;
//
//    /**
//     * To get the count of comments on application
//     *
//     * @param uuid uuid of the comment
//     * @return number of comments
//     * @throws CommentManagementException Exceptions of the comment management.
//     */
//    int getCommentCount(String uuid) throws CommentManagementException;
//
//    /**
//     * To get the count of comments on application
//     *
//     * @param appId application id
//     * @param appReleaseId application release id
//     * @return count of the comments
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception
//     * @throws SQLException sql exception
//     */
//    int getCommentCountByApp(int appId, int appReleaseId) throws CommentManagementException, DBConnectionException, SQLException;
//
//    /**
//     * To get the count of the comments of an application
//     *
//     * @param appType application type
//     * @param appName Name of the application
//     * @param version version of the application
//     * @return number of the comments
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception
//     * @throws SQLException sql exception
//     */
//    int getCommentCountByApp(String appType, String appName, String version) throws CommentManagementException,
//            DBConnectionException, SQLException ;

//    /**
//     * To get the count of comments by number of users
//     *
//     * @param createdBy comment created user's name
//     * @return count of the comments
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception
//     * @throws SQLException sql exception
//     */
//    int getCommentCountByUser(String createdBy) throws CommentManagementException, DBConnectionException,
//            SQLException;

//    /**
//     * To get count of the comments by parent comment
//     *
//     * @param uuid uuid of the comment
//     * @param parentId id of the parent comment
//     * @return count of comments
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception
//     * @throws SQLException sql exception
//     */
//    int getCommentCountByParent(String uuid,int parentId) throws CommentManagementException, DBConnectionException,
//            SQLException;

    /**
     * To delete comment using comment id.
     *
     * @param apAppCommentId id of the comment
     * @throws CommentManagementException Exceptions of the comment management
     */
    void deleteComment(int apAppCommentId) throws CommentManagementException;

//    /**
//     * To delete comment using comment id.
//     *
//     * @param uuid uuid of the comment
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws ApplicationManagementDAOException Exceptions of the application management.
//     */
//    void deleteComment(String uuid) throws CommentManagementException, ApplicationManagementDAOException;

//    /**
//     * To delete comments using application details.
//     *
//     * @param appId id of the commented application.
//     * @param appReleaseID Id of the released version of the application.
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception.
//     * @throws SQLException sql exception
//     */
//    void deleteComments(int appId,int appReleaseID) throws CommentManagementException, DBConnectionException, SQLException;

//    /**
//     * To delete comments using application details.
//     *
//     * @param appType type of the commented application.
//     * @param appName name of the commented application.
//     * @param version version of the commented application.
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception
//     * @throws SQLException sql exception
//     *
//     */
//    void deleteComments(String appType,String appName,String version) throws CommentManagementException, DBConnectionException, SQLException;

//    /**
//     * To delete comments using users created and application details.
//     *
//     * @param appType type of the commented application.
//     * @param appName name of the commented application.
//     * @param version version of the commented application.
//     * @param createdBy Username of the created person.
//     * @throws CommentManagementException Exceptions of the comment management.
//     */
//    void deleteComments(String appType,String appName,String version,String createdBy) throws CommentManagementException, DBConnectionException, SQLException;

//    /**
//     * To delete comments by parent id of the comment.
//     *
//     *@param uuid uuid of the comment.
//     * @param parentId parent id of the parent comment.
//     * @throws CommentManagementException Exceptions of the comment management.
//     * @throws DBConnectionException db connection exception
//     * @throws SQLException sql exception
//     *
//     */
//    void deleteComments(String uuid,int parentId) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To update a comment.
     *
     * @param comment comment of the application.
     * @param apAppCommentId id of the comment
     * @return {@link Comment}updated comment
     * @throws CommentManagementException Exceptions of the comment management
     * @throws SQLException SQL Exception
     * @throws DBConnectionException Database connection Exception
     */
    Comment updateComment(Comment comment,int apAppCommentId) throws CommentManagementException, SQLException, DBConnectionException;

    /**
     * To get number of rated users
     *
     * @param uuid uuid of the application
     * @return number of rated users
     * @throws SQLException sql exception
     */
    int getRatedUser(String uuid)throws SQLException;

    /**
     * To get the average of stars
     *
     * @param uuid uuid of the comment
     * @return value of the stars of an application
     * @throws SQLException sql exception
     */
    int getStars(String uuid)throws SQLException;

    /**
     * To update rating stars
     *
     * @param stars amount of stars
     * @param uuid uuid of the application
     * @return value of the added stars
     * @throws ApplicationManagementException Application Management Exception.
     */
    int updateStars(int stars, String uuid) throws ApplicationManagementException;
}