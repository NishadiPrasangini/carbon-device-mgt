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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * This interface specifies the database access operations performed for comments.
 */
@SuppressWarnings("ALL")
public interface CommentDAO  {




    int addComment(int tenantId , Comment comment, String createdBy, int parentId, int appReleaseId , int appId) throws CommentManagementException, DBConnectionException;

    int addComment(Comment comment,String createdBy,String appType,String appName,String version) throws CommentManagementException;

    boolean updateComment(int apAppCommentId, String updatedComment,String modifiedBy, Timestamp modifiedAt) throws CommentManagementException, DBConnectionException, SQLException;

    Comment getComment(int apAppCommentId)throws CommentManagementException;

    List<Comment> getComments(int appReleasedId,int appId)throws CommentManagementException;

    List<Comment> getComments(String appType,String appName,String version) throws CommentManagementException, DBConnectionException, SQLException;

    List<Comment> getComments(int tenantId) throws CommentManagementException, DBConnectionException, SQLException;

    List<Comment> getCommentsByUser(String createdBy) throws CommentManagementException, DBConnectionException, SQLException;

    List<Comment> getCommentsByUser(String createdBy,Timestamp createdAt) throws CommentManagementException, DBConnectionException, SQLException;

    List<Comment> getCommentsByModifiedUser(String modifiedBy) throws CommentManagementException, DBConnectionException, SQLException;

    List<Comment> getCommentsByModifiedUser(String modifiedBy,Timestamp modifiedAt) throws CommentManagementException, DBConnectionException, SQLException;

    List<Comment> getComments(String appType,String appName,String version,int parentId) throws CommentManagementException, DBConnectionException, SQLException;

    int getCommentCountByUser(String createdBy) throws CommentManagementException, DBConnectionException, SQLException;

    int getCommentCountByUser(String modifiedBy,Timestamp modifedAt) throws CommentManagementException, DBConnectionException, SQLException;

    int getCommentCountByApp(int appId, int appReleaseId) throws CommentManagementException, DBConnectionException, SQLException;

    int getCommentCountByApp(String appType,String appName,String version) throws CommentManagementException, DBConnectionException, SQLException;

    void deleteComment(int apAppCommentId) throws CommentManagementException, DBConnectionException, SQLException;

    void deleteComments(int appId,int appReleaseID) throws CommentManagementException, DBConnectionException, SQLException;

    void deleteComments(String appType,String appName,String version)throws CommentManagementException;

    void deleteComments(String appType,String appName,String version,String createdBy)throws CommentManagementException;

    void deleteCommentsByUser(String commUser,int tenantId)throws CommentManagementException;

    void deleteComments(String appType,String appName,String version,int parentId)throws CommentManagementException;









}
