package org.wso2.carbon.device.application.mgt.api.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.device.application.mgt.api.APIUtil;
import org.wso2.carbon.device.application.mgt.api.services.CommentManagementAPI;
import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.LifecycleState;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.LifecycleManagementException;
import org.wso2.carbon.device.application.mgt.common.services.CommentsManager;
import org.wso2.carbon.device.application.mgt.common.services.LifecycleStateManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class CommentManagementAPIImpl implements CommentManagementAPI{

    private static Log log = LogFactory.getLog(CommentManagementAPIImpl.class);

    @Override
    @GET
    public Response getComments() throws Exception{
        CommentsManager commentsManager = APIUtil.getCommentsManager();
        List<Comment> comments = new ArrayList<>();
        try {
            comments = commentsManager.getComment();
        } catch (CommentManagementException e) {
            String msg = "Error occurred while retrieving comments.";
            log.error(msg, e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity(comments).build();
    }

    @Override
    @POST
    @Consumes("comments/json")
    public Response addComments(Comment comment){
        CommentsManager commentsManager = APIUtil.getCommentsManager();
        try {
            Comment comment1 = commentsManager.addcomment(comment);

            if (comment != null){
                return Response.status(Response.Status.CREATED).entity(comment1).build();
            }else{
                String msg = "Given comment is not matched ";
                log.error(msg);
                return  Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch (CommentManagementException e) {
            String msg = "Error occurred while creating the comment";
            log.error(msg, e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Override
    public Response editComment(Comment comment) {
        return null;
    }



    @Override
    @DELETE
    @Path("/{identifier}")
    public Response deleteComment(@PathParam("identifier") int identifier) {
        CommentsManager commentsManager = APIUtil.getCommentsManager();
        try {
            commentsManager.deleteComment(identifier);
        } catch (CommentManagementException e) {
            String msg = "Error occurred while deleting the comment.";
            log.error(msg, e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity("comment is deleted successfully.").build();
    }
    }

