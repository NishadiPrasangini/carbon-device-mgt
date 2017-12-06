package org.wso2.carbon.device.application.mgt.api.services.impl;

import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.device.application.mgt.api.APIUtil;
import org.wso2.carbon.device.application.mgt.api.services.CommentManagementAPI;
import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.Filter;
import org.wso2.carbon.device.application.mgt.common.PaginationRequest;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;
import org.wso2.carbon.device.application.mgt.common.services.CommentsManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment Management related jax-rs APIs.
 */
@Path("/comments")
public class CommentManagementAPIImpl implements CommentManagementAPI{

    private static Log log = LogFactory.getLog(CommentManagementAPIImpl.class);

    @Override
    @GET
    @Path("/{uuid}")
    public Response getAllComments(
            @PathParam("uuid") String uuid,
            @QueryParam("start")int start,
            @QueryParam("limit")int limit){

        CommentsManager commentsManager = APIUtil.getCommentsManager();
        List<Comment> comments = new ArrayList<>();

        try {
            PaginationRequest request=new PaginationRequest(start,limit);
            commentsManager.getAllComments(request,uuid);
        } catch (CommentManagementException e) {
            String msg = "Error occurred while retrieving comments.";
            log.error(msg, e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(comments).build();
    }

    @Override
    @POST
    @Consumes("uuid/comments/json")
    public Response addComments(
            @ApiParam Comment comment,
            @PathParam("uuid") String uuid){

        CommentsManager commentsManager = APIUtil.getCommentsManager();
        try {
            Comment newComment = commentsManager.addComment(comment,uuid);
            if (comment != null){
                return Response.status(Response.Status.CREATED).entity(newComment).build();
            }else{
                String msg = "Given comment is not valid ";
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
    @Consumes("/uuid/comment/json")
    public Response updateComment(
            @PathParam("uuid") String uuid,
            @ApiParam Comment comment) {

        CommentsManager commentsManager = APIUtil.getCommentsManager();
        try {
            comment = commentsManager.updateComment(uuid,comment);
        } catch (NotFoundException e) {
            return APIUtil.getResponse(e, Response.Status.NOT_FOUND);
        } catch (CommentManagementException e) {
            String msg = "Error occurred while editing the comment.";
            log.error(msg, e);
            return APIUtil.getResponse(e, Response.Status.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(comment).build();

    }




    @Override
    @DELETE
    @Path("/{uuid}/{identifier}")
    public Response deleteComment(
            @PathParam("uuid")
                    String uuid,
            @PathParam("identifier")
                    int identifier){

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

