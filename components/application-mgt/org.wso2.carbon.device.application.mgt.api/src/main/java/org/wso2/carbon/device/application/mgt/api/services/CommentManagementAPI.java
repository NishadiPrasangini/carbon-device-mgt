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
                        name = "Edit a Comment",
                        description = "Edit a Commnent",
                        key = "perm:comment:edit",
                        permissions = {"/device-mgt/comment/edit"}
                ),

                @Scope(
                        name = "Delete a Comment",
                        description = "Delete a comment",
                        key = "perm:comment:delete",
                        permissions = {"/device-mgt/comment/delete"}
                ),


        }
)
@Path("/Comments")
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
    Response getAllComments() throws Exception;

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
            notes = "This will edit the new comment",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:comment:update")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "OK. \n Successfully edited comment.",
                            response = Comment.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred while editing the comment.",
                            response = ErrorResponse.class)
            })
    Response updateComment
            (
            @ApiParam(
                    name = "comment",
                    value = "The comment that need to be edited.",
                    required = true)
            @Valid Comment comment);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "POST",
            value = "Create a comment",
            notes = "This will create a new comment",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:comment:create")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "OK. \n Successfully created a comment.",
                            response = Comment.class),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. \n " +
                                    "Empty body because the client already has the latest comment of the requested "
                                    + "resource."),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred while getting the comment list.",
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
