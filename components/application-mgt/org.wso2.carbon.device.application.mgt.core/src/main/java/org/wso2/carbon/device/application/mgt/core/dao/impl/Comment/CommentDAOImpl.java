package org.wso2.carbon.device.application.mgt.core.dao.impl.Comment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;
import org.wso2.carbon.device.application.mgt.common.services.CommentsManager;
import org.wso2.carbon.device.application.mgt.core.dao.CommentDAO;
import org.wso2.carbon.device.application.mgt.core.dao.common.Util;
import org.wso2.carbon.device.application.mgt.core.dao.impl.AbstractDAOImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This handles CommentDAO related operations.
 */
public class CommentDAOImpl extends AbstractDAOImpl implements CommentDAO {

    private static final Log log = LogFactory.getLog(CommentDAOImpl.class);


    @Override
    public int addComment(int tenantId, Comment comment, String createdBy,int parentId, int appReleaseId, int appId) throws CommentManagementException, DBConnectionException {
        if (log.isDebugEnabled()) {
            log.debug("Request received in DAO Layer to add COMMENT");
        }

        Connection conn=this.getDBConnection();
        PreparedStatement stmt = null;

        int index = 0;
        int commentId = -1;
        String sql = "INSERT INTO `AP_APP_COMMENT` (`TENANT_ID`, `COMMENT_TEXT`, `CREATED_BY`, `PARENT_ID`, `AP_APP_RELEASE_ID`, `AP_APP_ID`) VALUES (?,?,?,?,?,?);"
                ;
        try{

            stmt = conn.prepareStatement(sql, new String[] {"id"});
                stmt.setInt(++index, tenantId);
                stmt.setString(++index, comment.getComment());
                stmt.setString(++index,createdBy);
                stmt.setInt(++index,parentId);
                stmt.setInt(++index,appReleaseId);
                stmt.setInt(++index,appId);
                stmt.addBatch();

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

    }

    @Override
    public int addComment(Comment comment, String createdBy, String appType, String appName, String version) throws CommentManagementException {

int commentId = -1;

     return commentId;
    }

    @Override
    public boolean updateComment(int apAppCommentId, String updatedComment, String modifiedBy, Timestamp modifiedAt) throws CommentManagementException, DBConnectionException, SQLException {
        Connection connection;
        PreparedStatement statement = null;
        int rows;
        String sql = "UPDATE `AP_APP_COMMENT` SET `COMMENT_TEXT`='?', `MODEFIED_BY`='?', `MODEFIED_AT`='?' WHERE `ID`='?';";

        try {
            connection = this.getDBConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, updatedComment);
            statement.setString(2,modifiedBy);
            statement.setTimestamp(3,modifiedAt);
            statement.setInt(4,apAppCommentId);

            rows = statement.executeUpdate();
            return (rows > 0);
        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(statement, null);
        }

    }



    @Override
    public Comment getComment(int apAppCommentId) throws CommentManagementException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comment with the ap_comment_id(" + apAppCommentId + ") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Comment comment=null;
        String sql = "";

        try {

            conn = this.getDBConnection();
            sql += "select COMMENT_TEXT FROM AP_APP_COMMENT where ID=1;";

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, apAppCommentId);
            if (rs.next()) {
               comment = Util.loadMatchingComment(rs, false);
            }

        } catch (SQLException e) {
           throw e;
        } catch (DBConnectionException e) {
            throw e;
        }  finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getComments(int appReleasedId, int appId) throws CommentManagementException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the ap_release_id(" + appReleasedId + ") and app id("+appId+") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
//        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select COMMENT_TEXT FROM AP_APP_COMMENT where AP_APP_RELEASE_ID='?' and AP_APP_ID='?';";

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, appReleasedId);
            stmt.setInt(2,appId);
            comments = new ArrayList<>();
            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version) throws CommentManagementException, DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the application name(" + appName + "),application type("+appType+") and application version ("+version+") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select `COMMENT_TEXT`, `PARENT_ID` ,`TENANT_ID` from `AP_APP_COMMENT`c ,"+
                    "(select `ID`as `releaseId`, `AP_APP_ID`as `releaseAP_APP_ID` from `AP_APP_RELEASE`r where `VERSION`='?') r,"+
                    "(select `ID`as `appId` from `AP_APP` p where `NAME`='?' and `TYPE`='?')p"+
                    " where `AP_APP_RELEASE_ID`=`releaseId` and `releaseAP_APP_ID`=`appId` and `AP_APP_ID`=`releaseAP_APP_ID`"+
                    "order by `CREATED_AT` DESC;";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            comments = new ArrayList<>();
            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getComments(int tenantId) throws CommentManagementException, DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the tenant_id(" + tenantId + ")  from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select COMMENT_TEXT FROM AP_APP_COMMENT where TENANT_ID='?' ;";

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, tenantId);
            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }


        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getCommentsByUser(String createdBy) throws CommentManagementException, DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the created by(" + createdBy + ")  from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select `COMMENT_TEXT`,`PARENT_ID`,`TENANT_ID`,`CREATED_AT` from `AP_APP_COMMENT` where `CREATED_BY`= '?'" +
                    " order by `CREATED_AT` DESC;";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, createdBy);

            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }


        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getCommentsByUser(String createdBy, Timestamp createdAt) throws CommentManagementException, DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the created by(" + createdBy + ") at ("+createdAt+") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select `COMMENT_TEXT`,`PARENT_ID`,`TENANT_ID` from `AP_APP_COMMENT` where `CREATED_BY`= '?'" +
                    "and `CREATED_AT`= '?' order by `CREATED_AT` DESC;";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, createdBy);
            stmt.setTimestamp(2,createdAt);
            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }


        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy) throws CommentManagementException, DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the modified by(" +modifiedBy + ")  from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select `COMMENT_TEXT`,`PARENT_ID`,`TENANT_ID`,`CREATED_AT` ,`MODEFIED_AT` from `AP_APP_COMMENT` " +
                    "where `MODEFIED_BY`= '?' order by `CREATED_AT` DESC;";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, modifiedBy);

            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }


        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getCommentsByModifiedUser(String modifiedBy, Timestamp modifiedAt) throws CommentManagementException, DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the modified by(" +modifiedBy + ")  from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select `COMMENT_TEXT`,`PARENT_ID`,`TENANT_ID`,`CREATED_AT` from `AP_APP_COMMENT` " +
                    "where `MODEFIED_BY`= '?',`MODEFIED_AT`='?' order by `CREATED_AT` DESC;";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, modifiedBy);
            stmt.setTimestamp(2,modifiedAt);

            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }


        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public List<Comment> getComments(String appType, String appName, String version, int parentId) throws CommentManagementException, DBConnectionException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Getting comments with the application name(" + appName + "),application type("+appType+") andCommentManagementException application version ("+version+") from the database");
        }
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
//        List<Comment> comments = null;

        try {

            conn = this.getDBConnection();
            sql += "select `COMMENT_TEXT` ,`TENANT_ID` from `AP_APP_COMMENT`c ," +
                    "(select `ID`as `releaseId`, `AP_APP_ID`as `releaseAP_APP_ID` from `AP_APP_RELEASE`r where `VERSION`='?' ) r," +
                    "(select `ID`as `appId` from `AP_APP` p where `NAME`='?' and `TYPE`='?')p " +
                    "where `PARENT_ID`='?' and`AP_APP_RELEASE_ID`=`releaseId` and `releaseAP_APP_ID`=`appId` and " +
                    "`AP_APP_ID`=`releaseAP_APP_ID` order by `CREATED_AT` DESC;";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, version);
            stmt.setString(2,appName);

            stmt.setString(3,appType);
            stmt.setInt(4,parentId);
            comments = new ArrayList<>();
            while (rs.next()) {
                Comment comment = DeviceManagementDAOUtil.loadDevice(rs);
                comments.add(comment);
            }

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

//
    @Override
    public int getCommentCountByUser(String createdBy) throws CommentManagementException, DBConnectionException, SQLException {
        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;
        try {
            conn = this.getDBConnection();
            String sql = "select count(`ID`) from `AP_APP_COMMENT` where `CREATED_BY`= '?';";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, createdBy);


            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e
        } finally {
            Util.cleanupResources(stmt, null);
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
        }
        return commentCount;

    }

    @Override
    public int getCommentCountByUser(String modifiedBy, Timestamp modifedAt) throws CommentManagementException, DBConnectionException, SQLException {
        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;
        try {
            conn = this.getDBConnection();
            String sql = "select count(`ID`) from `AP_APP_COMMENT` where `MODEFIED_BY`= '?' and `MODEFIED_AT`='?';;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, modifiedBy);
            stmt.setTimestamp(2, modifedAt);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
        }
        return commentCount;

    }

    @Override
    public int getCommentCountByApp(int appId, int appReleaseId) throws CommentManagementException, DBConnectionException, SQLException {
        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;
        try {
            conn = this.getDBConnection();
            String sql = "select count(`ID`) FROM `AP_APP_COMMENT`  where `AP_APP_RELEASE_ID`='?' and `AP_APP_ID`='?';";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appReleaseId);
            stmt.setInt(2, appId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
        }
        return commentCount;
    }

    @Override
    public int getCommentCountByApp(String appType, String appName, String version) throws CommentManagementException, DBConnectionException, SQLException {
        Connection conn;
        PreparedStatement stmt = null;
        int commentCount = 0;
        try {
            conn = this.getDBConnection();
            String sql = "select count(`ID`)as `commentCount` from `AP_APP_COMMENT`c, " +
                    "(select `ID`as `releaseId`, `AP_APP_ID`as `releaseAP_APP_ID` from `AP_APP_RELEASE`r where `VERSION`='?' )r," +
                    "(select `ID`as `appId` from `AP_APP` p where `NAME`='' and `TYPE`='')p " +
                    "where `AP_APP_RELEASE_ID`=`releaseId` and `releaseAP_APP_ID`=`appId` and `AP_APP_ID`=`releaseAP_APP_ID`;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2, appName);
            stmt.setString(3, appType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt("COMMENT_COUNT");
            }
        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
        }
        return commentCount;
    }

    @Override
    public void deleteComment(int apAppCommentId) throws CommentManagementException, DBConnectionException, SQLException {

        Connection conn;
        PreparedStatement stmt = null;
        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM `APPMGT`.`AP_APP_COMMENT` WHERE `ID`='?';";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, apAppCommentId);

            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }

    }

    @Override
    public void deleteComments(int appId, int appReleaseID) throws CommentManagementException, DBConnectionException, SQLException {
        Connection conn;
        PreparedStatement stmt = null;
        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM `AP_APP_COMMENT` WHERE `AP_APP_RELEASE_ID`='?' and `AP_APP_ID`='?';";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appReleaseID);
            stmt.setInt(2,appId);

            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
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
            String sql = "DELETE FROM `AP_APP_COMMENT` WHERE " +
                    "(select `AP_APP_RELEASE_ID` from `AP_APP_RELEASE` where `VERSION`='?' and " +
                    "(select `AP_APP_ID` from `AP_APP` where `NAME`='?' and `TYPE`='?')) and " +
                    "(select `AP_APP_ID` from `AP_APP` where `NAME`='?' and `TYPE`='?');";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            stmt.setString(4,appName);
            stmt.setString(5,appType);


            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
        }

    @Override
    public void deleteComments(String appType, String appName, String version, String createdBy) throws CommentManagementException {
        Connection conn;
        PreparedStatement stmt = null;
        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM `AP_APP_COMMENT` WHERE " +
                    "(select `AP_APP_RELEASE_ID` from `AP_APP_RELEASE` where `VERSION`='?' and " +
                    "(select `AP_APP_ID` from `AP_APP` where `NAME`='?' and `TYPE`='?')) and " +
                    "(select `AP_APP_ID` from `AP_APP` where `NAME`='?' and `TYPE`='?') and `CREATED_BY`='?';";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            stmt.setString(4,appName);
            stmt.setString(5,appType);
            stmt.setString(6,createdBy);


            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public void deleteComments(String appType, String appName, String version, int parentId) throws CommentManagementException {
        Connection conn;
        PreparedStatement stmt = null;
        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM `AP_APP_COMMENT` WHERE " +
                    "(select `AP_APP_RELEASE_ID` from `AP_APP_RELEASE` where `VERSION`='?' and " +
                    "(select `AP_APP_ID` from `AP_APP` where `NAME`='?' and `TYPE`='?')) and " +
                    "(select `AP_APP_ID` from `AP_APP` where `NAME`='?' and `TYPE`='?') and `PARENT_ID`='?';";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, version);
            stmt.setString(2,appName);
            stmt.setString(3,appType);
            stmt.setString(4,appName);
            stmt.setString(5,appType);
            stmt.setInt(6,parentId);


            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }
}
