package org.wso2.carbon.device.application.mgt.api.services;

import io.swagger.annotations.*;
import org.wso2.carbon.device.application.mgt.api.beans.ErrorResponse;
import org.wso2.carbon.device.application.mgt.common.Comment;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
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

@Path("/Comments")
@Api(value = "Comments Management", description = "This API carries all comments management related operations " +
        "such as get all the comments, add comment, etc.")
@Produces(MediaType.APPLICATION_JSON)

public interface CommentManagementAPI {
    String SCOPE = "scope";

    @GET
    @Path("/uuid/{uuid}")
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

    Response getAllComments(
            @ApiParam(
                    name="uuid",
                    value="uuid of the released version of application.",
                    required = true)
            @PathParam("uuid")
                    String uuid,
            @ApiParam(
                    name="start",
                    value="Starting comment number.",
                    required = false)
            @QueryParam("start")
                    int start,
            @ApiParam(
                    name="limit",
                    value = "Limit of paginated comments",
                    required = false)
            @QueryParam("limit")
                    int limit);

    @POST
    @Path("/uuid/{uuid}")
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

    Response addComments(
            @ApiParam(
                    name = "comment",
                    value = "Comment details",
                    required = true)
                    Comment comment,
            @ApiParam(
                    name="uuid",
                    value="uuid of the release version of the application",
                    required=true)
            @PathParam("uuid")
                    String uuid);

    @PUT
    @Path("/{uuid}/{comments}")
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
    Response updateComment(
            @ApiParam(
                    name="uuid",
                    value = "uuid of the release version of the application.",
                    required = true)
            @QueryParam("uuid")
                    String uuid,
            @ApiParam(
                    name = "comment",
                    value = "The comment that need to be updated.",
                    required = true)
            @Valid Comment comment);

    @DELETE
    @Path("/uuid/{uuid}/identifier/{identifier}")
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

    Response deleteComment(
                    @ApiParam(
                            name="uuid",
                            value="uuid of the released version of application.",
                            required = false)
                    @PathParam("uuid")
                            String uuid,
                    @ApiParam(
                            name="identifier",
                            value="Id of the comment.",
                            required = true)
                    @PathParam("identifier")
                            int identifier);

    @GET
    @Path("/{uuid}/{stars}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "get stars",
            notes = "Get all stars",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:stars:get")
                    })
            }
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully retrieved stars.",
                            response = List.class,
                            responseContainer = "List"),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred while getting the stars",
                            response = ErrorResponse.class)
            })

    Response getStars(
            @ApiParam(
                    name = "uuid",
                    value = "uuid of the application release",
                    required = true)
            @PathParam("uuid")
                    String uuid);

    @GET
    @Path("/{uuid}/{stars}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "get rated users",
            notes = "Get all users",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:user:get")
                    })
            }
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully retrieved user.",
                            response = List.class,
                            responseContainer = "List"),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred while getting the comment list.",
                            response = ErrorResponse.class)
            })

    Response getRatedUser(
            @ApiParam(
                    name = "uuid",
                    value = "uuid of the application release",
                    required = true)
            @PathParam("uuid")
                    String uuid);

    @POST
    @Path("/uuid/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "POST",
            value = "Add a star value",
            notes = "This will add star value",
            tags = "Comment Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = SCOPE, value = "perm:stars:add")
                    })
            }
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "OK. \n Successfully rated to the application.",
                            response = Comment.class),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. \n " +
                                    "Empty body because the client already has the latest rating of the requested resource."),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n Error occurred rating for the application.",
                            response = ErrorResponse.class)
            })

    Response addStars(
            @ApiParam(
                    name = "stars",
                    value = "ratings for the application",
                    required = true)
                    int stars,
            @ApiParam(
                    name="uuid",
                    value="uuid of the release version of the application",
                    required=true)
            @PathParam("uuid")
                    String uuid) throws SQLException;

//    @PUT
//    @Path("/{uuid}/{stars}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @ApiOperation(
//            consumes = MediaType.APPLICATION_JSON,
//            produces = MediaType.APPLICATION_JSON,
//            httpMethod = "PUT",
//            value = "Edit ratings",
//            notes = "This will edit the star value",
//            tags = "Comment Management",
//            extensions = {
//                    @Extension(properties = {
//                            @ExtensionProperty(name = SCOPE, value = "perm:stars:update")
//                    })
//            }
//    )
//
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            code = 201,
//                            message = "OK. \n Successfully updated the stars.",
//                            response = Comment.class),
//                    @ApiResponse(
//                            code = 500,
//                            message = "Internal Server Error. \n Error occurred while updating the stars.",
//                            response = ErrorResponse.class)
//            })
//    Response updateStars(
//            @ApiParam(
//                    name="uuid",
//                    value = "uuid of the release version of the application.",
//                    required = true)
//            @QueryParam("uuid")
//                    String uuid,
//            @ApiParam(
//                    name = "stars",
//                    value = "star value of an application",
//                    required = true)
//            @Valid int stars) throws SQLException;
}
