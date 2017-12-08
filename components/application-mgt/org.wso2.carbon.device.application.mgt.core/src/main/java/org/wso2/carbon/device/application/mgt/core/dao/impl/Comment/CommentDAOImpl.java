package org.wso2.carbon.device.application.mgt.core.dao.impl.Comment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.wso2.carbon.device.application.mgt.common.ApplicationRelease;
import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.PaginationRequest;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;
import org.wso2.carbon.device.application.mgt.core.dao.CommentDAO;
import org.wso2.carbon.device.application.mgt.core.dao.common.Util;
import org.wso2.carbon.device.application.mgt.core.dao.impl.AbstractDAOImpl;
import org.wso2.carbon.device.application.mgt.core.exception.ApplicationManagementDAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sun.imageio.plugins.jpeg.JPEG.version;

/**
 * This handles CommentDAO related operations.
 */
public class CommentDAOImpl extends AbstractDAOImpl implements CommentDAO {

    private static final Log log = LogFactory.getLog(CommentDAOImpl.class);


    @Override
    public int addComment(int tenantId, Comment comment, String createdBy, int parentId, String uuid)
            throws CommentManagementException, DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Request received in DAO Layer to add comment for application release ("+uuid+")");
        }

        Connection conn=this.getDBConnection();
        PreparedStatement stmt = null;
        ResultSet rs;
        int index = 0;
        int commentId = -1;
        String sql = "INSERT INTO AP_APP_COMMENT (TENANT_ID, COMMENT_TEXT, CREATED_BY, PARENT_ID,AP_APP_RELEASE_ID," +
                "AP_APP_ID) VALUES (?,?,?,?,(SELECT ID FROM AP_APP_RELEASE WHERE UUID=?)," +
                "(SELECT AP_APP_ID FROM AP_APP_RELEASE WHERE UUID=?));";
        try{
            stmt = conn.prepareStatement(sql, new String[] {"id"});
            stmt.setInt(++index, tenantId);
            stmt.setString(++index, comment.getComment());
            stmt.setString(++index,createdBy);
            stmt.setInt(++index,parentId);
            stmt.setString(++index,uuid);
            stmt.setString(++index,uuid);
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                commentId = rs.getInt(1);
            }
        }  finally {
            Util.cleanupResources(stmt, null);
        }
        return  commentId;
    }

    @Override
    public int addComment(int tenantId,Comment comment, String createdBy, String appType, String appName,
                          String version) throws CommentManagementException, DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Request received in DAO Layer to add to application ("+appName+") and version ("+version+")");
        }

        Connection conn=this.getDBConnection();
        PreparedStatement stmt = null;
        ResultSet rs;
        int index = 0;
        int commentId = -1;
        String sql = "INSERT INTO AP_APP_COMMENT ( TENANT_ID,COMMENT_TEXT, CREATED_BY,AP_APP_RELEASE_ID,AP_APP_ID) " +
                "VALUES (?,?,?,(SELECT ID FROM AP_APP_RELEASE WHERE VERSION =? AND (SELECT ID FROM AP_APP WHERE " +
                "TYPE=? AND NAME=?)),(SELECT ID FROM AP_APP WHERE TYPE=? AND NAME=?));";

        try{
            stmt = conn.prepareStatement(sql, new String[] {"id"});
            stmt.setInt(++index, tenantId);
            stmt.setString(++index, comment.getComment());
            stmt.setString(++index,createdBy);
            stmt.setString(++index,version);
            stmt.setString(++index,appType);
            stmt.setString(++index,appName);
            stmt.setString(++index,appType);
            stmt.setString(++index,appName);

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                commentId = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw e;

        } finally {
            Util.cleanupResources(stmt, null);
        }
     return commentId;
    }

    @Override
    public Comment updateComment(int apAppCommentId, String updatedComment, String modifiedBy, Timestamp modifiedAt)
            throws CommentManagementException, DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Request received in DAO Layer to update the comment with ID ("+apAppCommentId+")");
        }

        Connection connection;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "UPDATE AP_APP_COMMENT SET COMMENT_TEXT=?, MODEFIED_BY=?, MODEFIED_AT=? WHERE ID=?;";
        try {
            connection = this.getDBConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, updatedComment);
            statement.setString(2,modifiedBy);
            statement.setTimestamp(3,modifiedAt);
            statement.setInt(4,apAppCommentId);

            statement.executeUpdate();
            rs= statement.executeQuery();
//            while (rs.next()) {
//
//
//                if (rows > 0) {
//                    Comment comment=new Comment();
//                    comment.setId(rs.getInt("ID"));
//                    comment.setTenantId(rs.getInt("TENANT_ID"));
//                    comment.setComment(rs.getString("COMMENT_TEXT"));
//                    comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
//                    comment.setCreatedBy(rs.getString("CREATED_BY"));
//                    comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
//                    comment.setModifiedBy(rs.getString("MODEFIED_AT"));
//                    comment.setParent(rs.getInt("PARENT_ID"));
//
////                    comments.add(comment);
//
//                    return comment;
//                }
//            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(statement, rs);
        }
        return getComment(apAppCommentId);
    }

    public Comment updateComment(String uuid, int apAppCommentID,String updatedComment, String modifiedBy, Timestamp
            modifiedAt) throws CommentManagementException, DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Request received in DAO Layer to update the comment with application ("+uuid+") and " +
                    "comment id ( "+apAppCommentID+")");
        }

        Connection connection;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "UPDATE AP_APP_COMMENT SET COMMENT_TEXT=?,MODEFIED_BY=?,MODEFIED_AT=? WHERE ID=?; ";
        try {
            connection = this.getDBConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, updatedComment);
            statement.setString(2,modifiedBy);
            statement.setTimestamp(3,modifiedAt);
            statement.setInt(4,apAppCommentID);
            statement.executeUpdate();
            rs= statement.executeQuery();
//            while (rs.next()) {
//
//
//                if (rows > 0) {
//                    Comment comment=new Comment();
//                    comment.setId(rs.getInt("ID"));
//                    comment.setTenantId(rs.getInt("TENANT_ID"));
//                    comment.setComment(rs.getString("COMMENT_TEXT"));
//                    comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
//                    comment.setCreatedBy(rs.getString("CREATED_BY"));
//                    comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
//                    comment.setModifiedBy(rs.getString("MODEFIED_AT"));
//                    comment.setParent(rs.getInt("PARENT_ID"));
//
////                    comments.add(comment);
//
//                    return comment;
//                }
//            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(statement, rs);
        }
        return getComment(apAppCommentID);
    }

    @Override
    public Comment getComment(int apAppCommentId) throws CommentManagementException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comment with the comment id(" + apAppCommentId + ") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs ;
        String sql = "";

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT FROM AP_APP_COMMENT WHERE ID=?;";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, apAppCommentId);
            rs=stmt.executeQuery();

            if (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("MODEFIED_AT"));
                comment.setParent(rs.getInt("PARENT_ID"));

//                comments.add(comment);
               Util.cleanupResources(stmt,rs);
                return comment;
            }

        } catch (SQLException e) {
            throw new CommentManagementException("Error occurred while retrieving information of all comments", e);
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }   finally {
            Util.cleanupResources(stmt, null);
        }
        return null;
    }

    @Override
    public List<Comment> getComment(String uuid) throws CommentManagementException{

        if (log.isDebugEnabled()) {
            log.debug("Getting comment with the application release(" +uuid + ") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs ;
        String sql = "";
        List<Comment> comments=new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT FROM AP_APP_COMMENT WHERE (SELECT ID FROM AP_APP_RELEASE where UUID=?)AND " +
                    "(SELECT AP_APP_ID FROM AP_APP_RELEASE where UUID=?);";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            stmt.setString(2,uuid);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("MODEFIED_AT"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        }   catch (DBConnectionException | SQLException e) {
            e.printStackTrace();
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }



    @Override
    public List<Comment> getAllComments(String uuid) throws CommentManagementException, SQLException,
            DBConnectionException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comment of the application release ("+uuid+") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Comment> comments=new ArrayList<>();
        String sql = "";

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT FROM AP_APP_COMMENT, AP_APP_RELEASE WHERE " +
                    "AP_APP_COMMENT.AP_APP_RELEASE_ID=AP_APP_RELEASE.ID AND AP_APP_RELEASE.UUID = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,uuid);
            rs= stmt.executeQuery();

            while (rs.next()) {
//                Comment comment=Util.loadComment(rs);
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } finally {
            Util.cleanupResources(stmt, rs);
        }
        return comments;
    }

    @Override
    public int getCommentCount(PaginationRequest request, String uuid) throws CommentManagementException {

        int commentCount = 0;
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean isUuidProvided=false;
//        int parentId = request.getParent();
//        boolean isParentCommentprovided = false;
//        String appName = request.getAppName();
//        boolean isAppNameProvided = false;
//        String appVersion = request.getAppVersion();
//        boolean isAppVersionProvided = false;
//        String appType=request.getAppType();
//        boolean isAppTypeProvided=false;
//
//        int appId=request.getAppId();
//        boolean isAppIdProvided=false;
//        String modifiedBy = request.getModifiedBy();
//        boolean isModifiedByProvided = false;
//        Timestamp modifiedAt=request.getModifiedAt();
//        boolean isModifiedAtProvided=false;
//        String createdBy = request.getCreatedBy();
//        boolean isCreatedByProvided = false;
//        Timestamp createdAt = request.getCreatedAt();
//        boolean isCreatedAtProvided = false;

//        Date since = request.getSince();
//        boolean isSinceProvided = false;
        try {
            conn = this.getDBConnection();
            if (uuid!= null) {
                isUuidProvided = true;
            }
//            if (parentId!= 0 ) {
//                sql = sql + "  PARENT_ID=?;";
//                isParentCommentprovided = true;
//            }
//
//            //Add query for last updated timestamp
//            if (isParentCommentprovided && isUuidProvided) {
//                sql = sql + " AND (SELECT ID FROM AP_APP_RELEASE WHERE UUID=?)";
//            }
//            if(isUuidProvided) {
//
//                if (appName != null && !appName.isEmpty()) {
//                    sql = sql + " AND (SELECT ID FROM AP_APP WHERE NAME=?)";
//                    isAppNameProvided = true;
//                }
//
//                if (appVersion != null && !appVersion.isEmpty()) {
//                    sql = sql + "AND(SELECT ID FROM AP_APP_RELEASE WHERE VERSION=?";
//                    isAppVersionProvided = true;
//                }
//
//                if (appType != null && !appType.isEmpty()) {
//                    sql = sql + "AND (select ID FROM AP_APP WHERE TYPE=?)";
//                    isAppTypeProvided = true;
//                }
//            }

//            if(appId!=0 ){
//                sql=sql+"AP_APP_ID=?";
//                isAppIdProvided=true;
//            }
//
//            if(isUuidProvided) {
//                sql = sql + "AND";
//                if (modifiedBy != null && !modifiedBy.isEmpty()) {
//                    sql = sql + "MODEFIED_BY=?";
//                    isModifiedByProvided = true;
//                }
//                sql = sql + "AND";
//                if (modifiedAt != null) {
//                    sql = sql + "MODEFIED_AT=?";
//                    isModifiedAtProvided = true;
//                }
//            }
//            if(isUuidProvided) {
//                sql = sql + "AND";
//                if (createdBy != null && !createdBy.isEmpty()) {
//                    sql = sql + "CREATED_BY=?";
//                    isCreatedByProvided = true;
//                }
//                sql = sql + "AND";
//                if (createdAt != null) {
//                    sql = sql + "CREATED_AT=?";
//                    isCreatedAtProvided = true;
//                }
//            }
            if (isUuidProvided) {
                String sql = "SELECT COUNT(AP_APP_COMMENT.ID) FROM AP_APP_COMMENT,AP_APP_RELEASE " +
                        "WHERE AP_APP_COMMENT.AP_APP_RELEASE_ID= AP_APP_RELEASE.ID AND " +
                        "AP_APP_COMMENT.AP_APP_ID= AP_APP_RELEASE.AP_APP_ID AND AP_APP_RELEASE.UUID=?;";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, uuid);
                rs = stmt.executeQuery();
//            if (isParentCommentprovided) {
//                stmt.setInt(paramIdx++, request.getParent());
//            }
//            if (isAppNameProvided) {
//                stmt.setString(paramIdx++, request.getAppName());
//            }
//            if (isAppVersionProvided) {
//                stmt.setString(paramIdx++, request.getAppVersion());
//            }
//
//            if (isAppTypeProvided) {
//                stmt.setString(paramIdx++, request.getAppType());
//            }
//            if (isAppIdProvided) {
//                stmt.setInt(paramIdx++, request.getAppId());
//            }
//            if (isModifiedByProvided) {
//                stmt.setString(paramIdx++, request.getModifiedBy());
//
//                if (isModifiedAtProvided) {
//                    stmt.setTimestamp(paramIdx++, request.getModifiedAt());
//                }
//            }
//            else if(isCreatedByProvided) {
//                stmt.setString(paramIdx++, request.getModifiedBy());
//
//                if (isCreatedAtProvided) {
//                    stmt.setTimestamp(paramIdx++, request.getCreatedAt());
//                }
//
//            }
                if (rs.next()) {
                    commentCount = rs.getInt("COMMENTS_COUNT");
                }
            }
        } catch (SQLException e) {
            throw new CommentManagementException("Error occurred while retrieving information", e);
        } catch (DBConnectionException e) {
            e.printStackTrace();
        } finally {
            Util.cleanupResources(stmt, rs);
        }
        return commentCount;
    }


    @Override
    public List<Comment> getComments(int appReleasedId, int appId) throws CommentManagementException {

        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the application release id(" + appReleasedId + ") and " +
                    "application id("+appId+") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs ;
        String sql = "";
        List<Comment> comments = new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT FROM AP_APP_COMMENT WHERE AP_APP_RELEASE_ID=? AND AP_APP_ID=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appReleasedId);
            stmt.setInt(2,appId);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            e.printStackTrace();

        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version) throws CommentManagementException,
            DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the application name(" + appName + "),application type("+appType+") " +
                    "and application version ("+version+") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs;
        String sql = "";
        List<Comment> comments =new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT,PARENT_ID,TENANT_ID FROM AP_APP_COMMENT C ,"+
                    "(SELECT ID AS RELEASE_ID, AP_APP_ID AS RELEASE_AP_APP_ID FROM AP_APP_RELEASE R WHERE VERSION=?) R,"+
                    "(SELECT ID AS APP_ID FROM AP_APP P WHERE NAME=? AND TYPE=?)P"+
                    " WHERE AP_APP_RELEASE_ID=RELEASE_ID AND RELEASE_AP_APP_ID=APP_ID AND AP_APP_ID=RELEASE_AP_APP_ID"+
                    "ORDER BY CREATED_AT DESC;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }

    @Override
    public List<Comment> getComments(int tenantId) throws CommentManagementException, DBConnectionException,
            SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the tenant_id(" + tenantId + ")  from the database");
        }

        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs;
        String sql = "";
        List<Comment> comments = new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT FROM AP_APP_COMMENT WHERE TENANT_ID='?';";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, tenantId);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByUser(String createdBy) throws CommentManagementException, DBConnectionException,
            SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the created by(" + createdBy + ")  from the database");
        }

        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs;
        String sql = "";
        List<Comment> comments = new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT ,PARENT_ID,TENANT_ID,CREATED_AT FROM AP_APP_COMMENT WHERE CREATED_BY= ?" +
                    " ORDER BY CREATED_AT DESC;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, createdBy);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByUser(String createdBy, Timestamp createdAt) throws CommentManagementException,
            DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the created by(" + createdBy + ") at ("+createdAt+") from the database");
        }

        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs;
        String sql = "";
        List<Comment> comments = new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT,PARENT_ID,TENANT_ID FROM AP_APP_COMMENT WHERE CREATED_BY=?" +
                    "AND CREATED_AT= ? ORDER BY CREATED_AT DESC;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, createdBy);
            stmt.setTimestamp(2,createdAt);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy) throws CommentManagementException,
            DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the modified by(" +modifiedBy + ")  from the database");
        }

        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs ;
        String sql = "";
        List<Comment> comments = new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT,PARENT_ID,TENANT_ID,CREATED_AT,MODEFIED_AT FROM AP_APP_COMMENT " +
                    "WHERE MODEFIED_BY= ? ORDER BY CREATED_AT DESC;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, modifiedBy);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy, Timestamp modifiedAt)
            throws CommentManagementException, DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the modified by(" +modifiedBy + ") at ("+modifiedAt+") from the " +
                    "database");
        }

        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs;
        String sql = "";
        List<Comment> comments = new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT,PARENT_ID,TENANT_ID,CREATED_AT FROM AP_APP_COMMENT WHERE MODEFIED_BY= ?," +
                    "MODEFIED_AT=? ORDER BY CREATED_AT DESC;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, modifiedBy);
            stmt.setTimestamp(2,modifiedAt);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return comments;
    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version, int parentId)
            throws CommentManagementException, DBConnectionException, SQLException {

        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the application name(" + appName + "),application type("+appType+") and" +
                    "application version ("+version+") from the database");
        }

        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs;
        String sql = "";
        List<Comment> comments =new ArrayList<>();

        try {
            conn = this.getDBConnection();
            sql += "SELECT COMMENT_TEXT,TENANT_ID FROM AP_APP_COMMENT C ," +
                    "(SELECT ID AS RELEASE_ID, AP_APP_ID AS RELEASE_AP_APP_ID FROM AP_APP_RELEASE R WHERE VERSION=? ) " +
                    "R,(SELECT ID AS APP_ID FROM AP_APP P WHERE NAME=? AND TYPE=?)P " +
                    "WHERE PARENT_ID=? AND AP_APP_RELEASE_ID=RELEASE_ID AND RELEASE_AP_APP_ID=APP_ID AND " +
                    "AP_APP_ID=RELEASE_AP_APP_ID ORDER BY CREATED_AT DESC;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            stmt.setInt(4,parentId);
            rs=stmt.executeQuery();

            while (rs.next()) {
                Comment comment=new Comment();
                comment.setId(rs.getInt("ID"));
                comment.setTenantId(rs.getInt("TENANT_ID"));
                comment.setComment(rs.getString("COMMENT_TEXT"));
                comment.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                comment.setCreatedBy(rs.getString("CREATED_BY"));
                comment.setModifiedAt(rs.getTimestamp("MODEFIED_AT"));
                comment.setModifiedBy(rs.getString("modifiedBy"));
                comment.setParent(rs.getInt("PARENT_ID"));

                comments.add(comment);
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
       return comments;
    }

    @Override
    public int getCommentCount(String uuid) throws CommentManagementException, DBConnectionException,
            SQLException {

        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;

        try {
            conn = this.getDBConnection();
            String sql = "\n" +
                    "SELECT COUNT(AP_APP_COMMENT.ID) FROM AP_APP_COMMENT,AP_APP_RELEASE WHERE " +
                    "AP_APP_COMMENT.AP_APP_RELEASE_ID=AP_APP_RELEASE.ID AND AP_APP_COMMENT.AP_APP_ID=" +
                    "AP_APP_RELEASE.AP_APP_ID AND AP_APP_RELEASE.UUID=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return commentCount;
    }

    @Override
    public int getCommentCountByUser(String createdBy) throws CommentManagementException, DBConnectionException,
            SQLException {

        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;

        try {
            conn = this.getDBConnection();
            String sql = "SELECT COUNT(ID) FROM AP_APP_COMMENT WHERE CREATED_BY= ?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, createdBy);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);

        }
        return commentCount;

    }

    @Override
    public int getCommentCountByUser(String modifiedBy, Timestamp modifedAt) throws CommentManagementException,
            DBConnectionException, SQLException {

        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;

        try {
            conn = this.getDBConnection();
            String sql = "SELECT COUNT(ID) FROM AP_APP_COMMENT WHERE MODEFIED_BY= ? AND MODEFIED_AT=?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, modifiedBy);
            stmt.setTimestamp(2, modifedAt);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return commentCount;
    }

    @Override
    public int getCommentCountByApp(int appId, int appReleaseId) throws CommentManagementException,
            DBConnectionException, SQLException {

        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;

        try {
            conn = this.getDBConnection();
            String sql = "SELECT COUNT(ID) FROM AP_APP_COMMENT WHERE AP_APP_RELEASE_ID=? AND AP_APP_ID=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appReleaseId);
            stmt.setInt(2, appId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return commentCount;
    }

    @Override
    public int getCommentCountByApp(String appType, String appName, String version) throws CommentManagementException,
            DBConnectionException, SQLException {

        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;

        try {
            conn = this.getDBConnection();
            String sql = "SELECT COUNT(ID) AS COMMENT_COUNT FROM AP_APP_COMMENT C, " +
                    "(SELECT ID AS RELEASE_ID, AP_APP_ID AS RELEASE_AP_APP_ID FROM AP_APP_RELEASE R WHERE VERSION=? )R," +
                    "(SELECT ID AS APP_ID FROM AP_APP P WHERE NAME=? and TYPE=?)P " +
                    "WHERE AP_APP_RELEASE_ID=RELEASE_ID AND RELEASE_AP_APP_ID=APP_ID AND AP_APP_ID=RELEASE_AP_APP_ID;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2, appName);
            stmt.setString(3, appType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return commentCount;
    }

    public int getCommentCountByParent(String uuid,int parentId) throws CommentManagementException, DBConnectionException, SQLException {

        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;

        try {
            conn = this.getDBConnection();
            String sql = "SELECT COUNT(AP_APP_COMMENT.ID) FROM AP_APP_COMMENT,AP_APP_RELEASE WHERE " +
                    "AP_APP_COMMENT.AP_APP_RELEASE_ID=AP_APP_RELEASE.ID AND " +
                    "AP_APP_COMMENT.AP_APP_ID=AP_APP_RELEASE.AP_APP_ID and AP_APP_RELEASE.UUID=? and PARENT_ID=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1,uuid);
            stmt.setInt(2,parentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        return commentCount;
    }

    @Override
    public void deleteComment(int apAppCommentId) throws CommentManagementException, DBConnectionException,
            SQLException {

        Connection conn;
        PreparedStatement stmt = null;

        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM AP_APP_COMMENT WHERE ID=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, apAppCommentId);
            stmt.executeUpdate();
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    public void deleteComment(String uuid) throws CommentManagementException, DBConnectionException, SQLException {

        Connection conn;
        PreparedStatement stmt = null;

        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM AP_APP_COMMENT WHERE " +
                    "(SELECT ID FROM AP_APP_RELEASE WHERE UUID=?)AND (SELECT AP_APP_ID FROM AP_APP_RELEASE " +
                    "WHERE UUID=?);";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1,uuid);
            stmt.setString(2,uuid);
            stmt.executeUpdate();
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public void deleteComments(int appId, int appReleaseID) throws CommentManagementException, DBConnectionException,
            SQLException {

        Connection conn;
        PreparedStatement stmt = null;

        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM AP_APP_COMMENT WHERE AP_APP_RELEASE_ID=? and AP_APP_ID=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appReleaseID);
            stmt.setInt(2,appId);
            stmt.executeUpdate();
        } catch (DBConnectionException | SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version) throws CommentManagementException {

        Connection conn;
        PreparedStatement stmt = null;

        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM AP_APP_COMMENT WHERE " +
                    "(SELECT AP_APP_RELEASE_ID FROM AP_APP_RELEASE WHERE VERSION=? AND " +
                    "(SELECT AP_APP_ID FROM AP_APP WHERE NAME=? AND TYPE=?)) AND " +
                    "(SELECT AP_APP_ID FROM AP_APP AND NAME=? AND TYPE=?);";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            stmt.setString(4,appName);
            stmt.setString(5,appType);
            stmt.executeUpdate();
        } catch (DBConnectionException | SQLException e) {
            e.printStackTrace();
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version, String createdBy)
            throws CommentManagementException {

        Connection conn;
        PreparedStatement stmt = null;

        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM AP_APP_COMMENT WHERE " +
                    "(SELECT AP_APP_RELEASE_ID FROM AP_APP_RELEASE WHERE VERSION=? AND " +
                    "(SELECT AP_APP_ID FROM AP_APP WHERE NAME=? AND TYPE=?)) AND " +
                    "(SELECT AP_APP_ID FROM AP_APP WHERE NAME=? and TYPE=?) AND CREATED_BY=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            stmt.setString(4,appName);
            stmt.setString(5,appType);
            stmt.setString(6,createdBy);
            stmt.executeUpdate();
        } catch (DBConnectionException | SQLException e) {
            e.printStackTrace();
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public void deleteComments(String uuid,int parentId)
            throws CommentManagementException {

        Connection conn;
        PreparedStatement stmt = null;

        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM AP_APP_COMMENT WHERE AP_APP_RELEASE_ID=(SELECT ID FROM AP_APP_RELEASE WHERE UUID=?) " +
                    "AND AP_APP_ID=(SELECT AP_APP_ID FROM AP_APP_RELEASE where UUID=?)AND PARENT_ID=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1,uuid);
            stmt.setString(2,uuid);
            stmt.setInt(3,parentId);
            stmt.executeUpdate();
        } catch (DBConnectionException | SQLException e) {
            e.printStackTrace();
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public int addStars(int stars,String uuid) throws ApplicationManagementDAOException {

        Connection connection;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            connection = this.getDBConnection();
            String sql = "UPDATE AP_APP_RELEASE SET STARS=?, NO_OF_RATED_USERS=(NO_OF_RATED_USERS+1) WHERE UUID=?;";

            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,stars);
            stmt.setString(2, uuid);
            stmt.executeUpdate();
            resultSet = stmt.getResultSet();

            if (resultSet.next()) {
                ApplicationRelease applicationRelease=new ApplicationRelease();
                applicationRelease.setStars(resultSet.getInt(1));

                Util.cleanupResources(stmt, resultSet);
                }
            return getStars(uuid);
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException(
                    "SQL Exception while trying to add stars to an application (UUID : " + uuid + "), by executing " +
                            "the query " +  e);
        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException(
                    "Database Connection Exception while trying to add stars the " + "application with UUID " +uuid, e);
        } finally {
            Util.cleanupResources(stmt, resultSet);
        }
    }
//    @Override
//    public int updateStars(String version, String appName,int updatedStars,ApplicationRelease applicationRelease) throws ApplicationManagementDAOException {
//
//        Connection connection;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        if (applicationRelease.isDefault()) {
//
//        }
//        String sql = "UPDATE `APP_MANAGER`.`AP_APP_RELEASE` SET `STARS`='?' WHERE `VERSION`='?' and (select `ID` from `AP_APP` where `NAME`='?');";
//        int index = 0;
//       int generatedColumns[] = {applicationRelease.getId()};
//        try {
//            connection = this.getDBConnection();
//            statement = connection.prepareStatement(sql, generatedColumns);
//            statement.setInt(++index, updatedStars);
//            statement.setString(++index, version);
//            statement.setString(++index,appName);
//            statement.executeUpdate();
//            resultSet = statement.getGeneratedKeys();
//            if (resultSet.next()) {
//                applicationRelease.setStars(resultSet.getInt(1));
//            }
//            insertApplicationReleaseProperties(connection, applicationRelease);
//            return updatedStars;
//        } catch (SQLException e) {
//            throw new ApplicationManagementDAOException(
//                    "SQL Exception while trying to add stars to an application (UUID : " + applicationRelease
//                            .getApplication().getUuid() + "), by executing the query " + sql, e);
//        } catch (DBConnectionException e) {
//            throw new ApplicationManagementDAOException(
//                    "Database Connection Exception while trying to add stars the " + "applcation with UUID "
//                            + applicationRelease.getApplication().getUuid(), e);
//        } finally {
//            Util.cleanupResources(statement, resultSet);
//        }
//        return updatedStars;
//

    @Override
    public int getStars(String uuid) throws ApplicationManagementDAOException {

        Connection connection;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql;

        try {
            connection = this.getDBConnection();
            sql="SELECT STARS FROM AP_APP_RELEASE WHERE UUID=?;";

            statement = connection.prepareStatement(sql);
            statement.setString(1,uuid);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ApplicationRelease applicationRelease = new ApplicationRelease();
                applicationRelease.setStars(resultSet.getInt("STARS"));

                Util.cleanupResources(statement, resultSet);
                return applicationRelease.getStars();
            }
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException(
                    "SQL Exception while trying to get stars from an application (UUID : " + uuid + "), by executing " +
                            "the query " + e);
        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException(
                    "Database Connection Exception while trying to get of stars the application with UUID " + uuid, e);
        } finally {
            Util.cleanupResources(statement, resultSet);
        }
        return 0;
    }

    @Override
    public int getRatedUser(String uuid) throws ApplicationManagementDAOException {

        Connection connection;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql;

        try {
            connection = this.getDBConnection();
            sql = "SELECT NO_OF_RATED_USERS FROM AP_APP_RELEASE WHERE UUID=?;";
            statement = connection.prepareStatement(sql);
            statement.setString(1,uuid);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ApplicationRelease applicationRelease = new ApplicationRelease();
                applicationRelease.setNoOfRatedUsers(resultSet.getInt("NO_OF_RATED_USERS"));

                Util.cleanupResources(statement, resultSet);
                return applicationRelease.getNoOfRatedUsers();
            }
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException(
                    "SQL Exception while trying to get number of rated users to an application (UUID : " + uuid + ")," +
                            "by executing the query " + e);
        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException(
                    "Database Connection Exception while trying to get number of rated users of the application " +
                            "with UUID " + uuid, e);
        } finally {
            Util.cleanupResources(statement, resultSet);
        }
        return 0;
    }
}