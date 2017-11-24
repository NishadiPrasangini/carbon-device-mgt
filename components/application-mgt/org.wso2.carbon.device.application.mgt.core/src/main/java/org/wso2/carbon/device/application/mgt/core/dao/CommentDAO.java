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

import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * This interface specifies the database access operations performed for comments.
 */
@SuppressWarnings("ALL")
public interface CommentDAO  {

    /**
     * To add a comment to a application.
     *
     * @param tenantId tenantId of the commented application.
     * @param comment comment of the application.
     * @param createdBy Username of the created person.
     * @param parentId parent id of the parent comment.
     * @param appReleaseId Id of the released version of the application.
     * @param appId id of the commented application.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     */
    int addComment(int tenantId , Comment comment, String createdBy, int parentId, int appReleaseId , int appId) throws CommentManagementException, DBConnectionException;

    /**
     * To add a comment to a application.
     *
     * @param comment comment of the application.
     * @param createdBy Username of the created person.
     * @param appType type of the commented application.
     * @param appName name of the commented application.
     * @param version version of the commented application.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     */
    int addComment(Comment comment,String createdBy,String appType,String appName,String version) throws CommentManagementException;

    /**
     * To update already added comment.
     *
     * @param apAppCommentId id of the comment
     * @param updatedComment comment after updated
     * @param modifiedBy Username of the modified person.
     * @param modifiedAt time of the modification.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception
     * @throws SQLException sql exception
     */
    boolean updateComment(int apAppCommentId, String updatedComment,String modifiedBy, Timestamp modifiedAt) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get the comment with id.
     *
     * @param apAppCommentId id of the comment
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     */
    String getComment(int apAppCommentId)throws CommentManagementException;

    /**
     * To get list of comments using release id and application id.
     *
     * @param appReleasedId Id of the released version of the application.
     * @param appId id of the commented application.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     */
    List<Comment> getComments(int appReleasedId,int appId)throws CommentManagementException;

    /**
     * To get list of comments using application type, application name and version of the application.
     *
     * @param appType type of the commented application.
     * @param appName name of the commented application.
     * @param version version of the commented application.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    List<Comment> getComments(String appType,String appName,String version) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get list of comments using tenant id.
     *
     * @param tenantId tenant id of the commented application
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    List<Comment> getComments(int tenantId) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get list of comments by created user.
     *
     * @param createdBy Username of the created person.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    List<Comment> getCommentsByUser(String createdBy) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get list of comments by created use and created time.
     *
     * @param createdBy Username of the created person.
     * @param createdAt time of the comment created.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    List<Comment> getCommentsByUser(String createdBy,Timestamp createdAt) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get list of comments by modified users.
     *
     * @param modifiedBy Username of the modified person.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    List<Comment> getCommentsByModifiedUser(String modifiedBy) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get list of comments using modified user's name and modified time.
     *
     * @param modifiedBy Username of the modified person.
     * @param modifiedAt time of the modification
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    List<Comment> getCommentsByModifiedUser(String modifiedBy,Timestamp modifiedAt) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get list of comments using application type, application name , application version and parent id of the comment.
     *
     * @param appType type of the commented application.
     * @param appName name of the commented application.
     * @param version version of the commented application.
     * @param parentId parent id of the parent comment.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    List<Comment> getComments(String appType,String appName,String version,int parentId) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get a count of the comments by usernames.
     *
     * @param createdBy Username of the created person.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    int getCommentCountByUser(String createdBy) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get a count of comments by modification details.
     *
     * @param modifiedBy Username of the modified person.
     * @param modifedAt time of the modification
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    int getCommentCountByUser(String modifiedBy,Timestamp modifedAt) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get count of comments by application versions.
     *
     * @param appId id of the commented application.
     * @param appReleaseId Id of the released version of the application.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    int getCommentCountByApp(int appId, int appReleaseId) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To get count of comments by application details.
     *
     * @param appType type of the commented application.
     * @param appName name of the commented application.
     * @param version version of the commented application.
     * @return
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    int getCommentCountByApp(String appType,String appName,String version) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To delete comment using comment id.
     *
     * @param apAppCommentId id of the comment
     * @param updatedComment comment after updated
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    void deleteComment(int apAppCommentId) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To delete comments using application details.
     *
     * @param appId id of the commented application.
     * @param appReleaseID Id of the released version of the application.
     * @throws CommentManagementException Exceptions of the comment management.
     * @throws DBConnectionException db connection exception.
     * @throws SQLException sql exception
     */
    void deleteComments(int appId,int appReleaseID) throws CommentManagementException, DBConnectionException, SQLException;

    /**
     * To delete comments using application details.
     *
     * @param appType type of the commented application.
     * @param appName name of the commented application.
     * @param version version of the commented application.
     * @throws CommentManagementException Exceptions of the comment management.
     */
    void deleteComments(String appType,String appName,String version)throws CommentManagementException;

    /**
     * To delete comments using users created and application details.
     *
     * @param appType type of the commented application.
     * @param appName name of the commented application.
     * @param version version of the commented application.
     * @param createdBy Username of the created person.
     * @throws CommentManagementException Exceptions of the comment management.
     */
    void deleteComments(String appType,String appName,String version,String createdBy)throws CommentManagementException;


    /**
     * To delete comments by parent id of the comment.
     *
     * @param appType type of the commented application.
     * @param appName name of the commented application.
     * @param version version of the commented application.
     * @param parentId parent id of the parent comment.
     * @throws CommentManagementException Exceptions of the comment management.
     */
    void deleteComments(String appType,String appName,String version,int parentId)throws CommentManagementException;

}
