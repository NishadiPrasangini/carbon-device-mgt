package org.wso2.carbon.device.application.mgt.api.services;

import io.swagger.annotations.*;
import org.wso2.carbon.device.application.mgt.api.beans.ErrorResponse;
import org.wso2.carbon.device.application.mgt.common.Application;
import org.wso2.carbon.device.application.mgt.common.Comment;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

        /**
        * APIs to handle comment management related tasks.
        */
@SwaggerDefinition(
        info = @Info(
                version = "1.0.0",
                title = "Comment Management Service",
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "name", value = "CommentManagementService"),
                                @ExtensionProperty(name = "context", value = "/api/application-mgt/v1.0/comments"),
                        })
                }
        ),
        tags = {
                @Tag(name = "comment_management", description = "Comment Management related "
                        + "APIs")
        }
)
@Scopes(
        scopes = {
                @Scope(
                        name = "Get Comments Details",
                        description = "Get comments details",
                        key = "perm:comment:get",
                        permissions = {"/device-mgt/comment/get"}
                ),
                @Scope(
                        name = "Add a Comment",
                        description = "Add a comment",
                        key = "perm:comment:add",
                        permissions = {"/device-mgt/comment/add"}
                ),
                @Scope(
                        name = "Update a Comment",
                        description = "Update a Commnent",
                        key = "perm:comment:update",
                        permissions = {"/device-mgt/comment/update"}
                ),

                @Scope(
                        name = "Delete a Comment",
                        description = "Delete a comment",
                        key = "perm:comment:delete",
                        permissions = {"/device-mgt/comment/delete"}
                ),


        }
)
@Path("/Application_Release/Comments")
@Api(value = "Comments Management", description = "This API carries all comments management related operations " +
        "such as get all the comments, add comment, etc.")
@Produces(MediaType.APPLICATION_JSON)

public interface CommentManagementAPI {
    String SCOPE = "scope";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "get comments",
            notes = "Get all comments",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:comment:get")
                    })
            }
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully retrieved comments.",
                            response = List.class,
                            responseContainer = "List"),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred while getting the comment list.",
                            response = ErrorResponse.class)
            })
    Response getAllComments(String uuid,@QueryParam("start")int start,@QueryParam("rowCount")int rowCount) throws Exception;

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "POST",
            value = "Add a comment",
            notes = "This will add a new comment",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:comment:add")
                    })
            }
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "OK. \n Successfully add a comment.",
                            response = Comment.class),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. \n " +
                                    "Empty body because the client already has the latest comment of the requested "
                                    + "resource."),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred adding a comment.",
                            response = ErrorResponse.class)
            })
    Response addComments(Comment comment);

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "PUT",
            value = "Edit a comment",
            notes = "This will edit the comment",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:comment:edit")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "OK. \n Successfully updated comment.",
                            response = Comment.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred while updating the new comment.",
                            response = ErrorResponse.class)
            })
    Response updateComment
            (
            @ApiParam(
                    name = "comment",
                    value = "The comment that need to be updated.",
                    required = true)
            @Valid Comment comment);

            @Path("/{appRelease}/{identifier}")
            @Produces(MediaType.APPLICATION_JSON)
            @Consumes(MediaType.APPLICATION_JSON)
            @ApiOperation(
                    consumes = MediaType.APPLICATION_JSON,
                    produces = MediaType.APPLICATION_JSON,
                    httpMethod = "DELETE",
                    value = "Remove comment",
                    notes = "Remove comment",
                    tags = "Comment Management",
                    extensions = {
                            @Extension(properties = {
                                    @ExtensionProperty(name = SCOPE, value = "perm:comment:remove")
                            })
                    }
            )
            @ApiResponses(
                    value = {
                            @ApiResponse(
                                    code = 200,
                                    message = "OK. \n Successfully deleted the comment"),
                            @ApiResponse(
                                    code = 500,
                                    message = "Internal Server Error. \n Error occurred while deleting the comment.",
                                    response = ErrorResponse.class)
                    })

    Response deleteComment(@PathParam("identifier") String identifier);



//
//            @GET
//            @Produces(MediaType.APPLICATION_JSON)
//            @ApiOperation(
//                    produces = MediaType.APPLICATION_JSON,
//                    httpMethod = "GET",
//                    value = "get stars",
//                    notes = "Get all stars",
//                    tags = "Comment Management",
//                    extensions = {
//                            @Extension(properties = {
//                                    @ExtensionProperty(name = SCOPE, value = "perm:stars:get")
//                            })
//                    }
//            )
//
//            @ApiResponses(
//                    value = {
//                            @ApiResponse(
//                                    code = 200,
//                                    message = "OK. \n Successfully retrieved stars.",
//                                    response = List.class,
//                                    responseContainer = "List"),
//                            @ApiResponse(
//                                    code = 500,
//                                    message = "Internal Server Error. \n Error occurred while getting the comment list.",
//                                    response = ErrorResponse.class)
//                    })
//            Response getStars() throws Exception;
//
//            @GET
//            @Produces(MediaType.APPLICATION_JSON)
//            @ApiOperation(
//                    produces = MediaType.APPLICATION_JSON,
//                    httpMethod = "GET",
//                    value = "get rated users",
//                    notes = "Get all users",
//                    tags = "Comment Management",
//                    extensions = {
//                            @Extension(properties = {
//                                    @ExtensionProperty(name = SCOPE, value = "perm:user:get")
//                            })
//                    }
//            )
//
//            @ApiResponses(
//                    value = {
//                            @ApiResponse(
//                                    code = 200,
//                                    message = "OK. \n Successfully retrieved user.",
//                                    response = List.class,
//                                    responseContainer = "List"),
//                            @ApiResponse(
//                                    code = 500,
//                                    message = "Internal Server Error. \n Error occurred while getting the comment list.",
//                                    response = ErrorResponse.class)
//                    })
//            Response getRatedUsers() throws Exception;

}
