package org.wso2.carbon.device.application.mgt.api.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.device.application.mgt.api.APIUtil;
import org.wso2.carbon.device.application.mgt.api.services.CommentManagementAPI;
import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.Filter;
import org.wso2.carbon.device.application.mgt.common.LifecycleState;
import org.wso2.carbon.device.application.mgt.common.PaginationRequest;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.LifecycleManagementException;
import org.wso2.carbon.device.application.mgt.common.services.CommentsManager;
import org.wso2.carbon.device.application.mgt.common.services.LifecycleStateManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment Management related jax-rs APIs.
 */
@Path("/appRelease/comments")
public class CommentManagementAPIImpl implements CommentManagementAPI{

    private static Log log = LogFactory.getLog(CommentManagementAPIImpl.class);

    @Override
    @GET
    public Response getAllComments(String uuid,@QueryParam("start")int start,@QueryParam("rowCount")int rowCount) throws Exception{
        CommentsManager commentsManager = APIUtil.getCommentsManager();
        List<Comment> comments = new ArrayList<>();
        try {


PaginationRequest request=new PaginationRequest(start,rowCount);
              commentsManager.getAllComments(request,uuid);

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
            Comment newcomment = commentsManager.addComment(comment);

            if (comment != null){
                return Response.status(Response.Status.CREATED).entity(newcomment).build();
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
    @PUT
    @Consumes("comment/json")
    public Response updateComment(Comment comment) {
        CommentsManager commentsManager = APIUtil.getCommentsManager();
        try {
            comment = commentsManager.updateComment(comment);
        } catch (NotFoundException e) {
            return APIUtil.getResponse(e, Response.Status.NOT_FOUND);
        } catch (CommentManagementException e) {
            String msg = "Error occurred while editing a comment.";
            log.error(msg, e);
            return APIUtil.getResponse(e, Response.Status.BAD_REQUEST);
        }
        return Response.status(Response.Status.OK).entity(comment).build();

    }




    @Override
    @DELETE
    @Path("/{identifier}")
    public Response deleteComment(@PathParam("identifier") String identifier) {
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
        return Response.status(Response.Status.OK).entity("Comment is deleted successfully.").build();
    }

//    @Override
//    public Response getStars() throws Exception {
//        CommentsManager commentsManager = APIUtil.getCommentsManager();
//        List<Comment> comments = new ArrayList<>();
//        try {
//
//            if(comments==null){
//                return Response.created(null).build();
//            }else {
//
//                ;
//            }
//        } catch (CommentManagementException e) {
//            String msg = "Error occurred while retrieving comments.";
//            log.error(msg, e);
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//        return Response.status(Response.Status.OK).entity(comments).build();
//    }
//
//    @Override
//    public Response getRatedUsers() throws Exception {
//        return null;
//    }


}

