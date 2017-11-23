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
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;

import java.sql.Timestamp;
import java.util.List;

/**
 * CommentsManager is responsible for handling all the add/update/delete/get operations related with
 *
 */
public interface CommentsManager {



    int addComment(int tenantId , Comment comment, String createdBy, int parentId, int appReleaseId , int appId)throws Exception;

    int addcomment(Comment comment,String createdBy,String appType,String appName,String version) throws Exception;
    Comment addcomment(Comment comment)throws CommentManagementException;

    boolean updateComment(int apAppCommentId, String updatedComment,String modifiedBy, Timestamp modifiedAt)throws Exception;

   List <Comment> getComment()throws Exception;

    Comment getComment(int apAppCommentId)throws Exception;

    List<Comment> getComments(int appReleasedId,int appId)throws Exception;

    List<Comment> getComments(String appType,String appName,String version)throws Exception;

    List<Comment> getComments(int tenantId)throws Exception;

    List<Comment> getCommentsByUser(String createdBy)throws Exception;

    List<Comment> getCommentsByUser(String createdBy,Timestamp createdAt)throws Exception;

    List<Comment> getCommentsByModifiedUser(String modifiedBy)throws Exception;

    List<Comment> getCommentsByModifiedUser(String modifiedBy,Timestamp modifiedAt)throws Exception;

    List<Comment> getComments(String appType,String appName,String version,int parentId)throws Exception;

    int getCommentCountByUser(String createdBy)throws Exception;

    int getCommentCountByUser(String modifiedBy,Timestamp modifedAt)throws Exception;

    int getCommentCountByApp(int appId, int appReleaseId)throws Exception;

    int getCommentCountByApp(String appType,String appName,String version)throws Exception;

    void deleteComment(int apAppCommentId)throws Exception;

    void deleteComments(int appId,int appReleaseID)throws Exception;

    void deleteComments(String appType,String appName,String version)throws Exception;

    void deleteComments(String appType,String appName,String version,String createdBy)throws Exception;

    void deleteCommentsByUser(String commUser,int tenantId)throws Exception;

    void deleteComments(String appType,String appName,String version,int parentId)throws Exception;







}
