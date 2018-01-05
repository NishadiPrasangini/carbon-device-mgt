package org.wso2.carbon.device.application.mgt.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.wso2.carbon.device.application.mgt.common.Application;
import org.wso2.carbon.device.application.mgt.common.ApplicationRelease;
import org.wso2.carbon.device.application.mgt.common.Comment;
import org.wso2.carbon.device.application.mgt.common.PaginationRequest;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.core.dao.common.ApplicationManagementDAOFactory;
import org.wso2.carbon.device.application.mgt.core.util.ConnectionManagerUtil;
import org.wso2.carbon.device.mgt.core.dao.DeviceDAO;
import org.wso2.carbon.device.mgt.core.dao.DeviceManagementDAOFactory;
import org.wso2.carbon.device.mgt.core.dao.DeviceTypeDAO;
import org.wso2.carbon.device.mgt.core.dto.DeviceType;

public class CommentDAOTest extends BaseCommentManagementTest {

    CommentDAO commentDAO;
    ApplicationReleaseDAO applicationReleaseDAO;
    DeviceTypeDAO deviceTypeDAO;
    ApplicationDAO applicationDAO;
    private static final Log log = LogFactory.getLog(CommentDAOTest.class);

    @BeforeClass
    public void init() throws Exception {
        initDataSource();
        commentDAO = ApplicationManagementDAOFactory.getCommentDAO();
        applicationReleaseDAO=ApplicationManagementDAOFactory.getApplicationReleaseDAO();
        deviceTypeDAO=DeviceManagementDAOFactory.getDeviceTypeDAO();
        applicationDAO=ApplicationManagementDAOFactory.getApplicationDAO();
    }

    @Test
    public void addComment() throws Exception {
        Comment comment=TestDataHolder.generateDummyCommentData(TestDataHolder.TEST_COMMENT);
        Application app=TestDataHolder.generateApplicationDummyData(1);
        ApplicationRelease appRelease=TestDataHolder.generateApplicationReleaseDummyData(1);
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
//            DeviceManagementDAOFactory.beginTransaction();
//            deviceTypeDAO.addDeviceType(null,123,true);
//            applicationDAO.createApplication(app);
            applicationReleaseDAO.createRelease(appRelease);
            int apAppCommentId=commentDAO.addComment(123,comment,"test created by",1,"test uuid");
            Assert.assertEquals(apAppCommentId,1,"comment is added");

        } catch (CommentManagementException e) {
            String msg = "Error occurred while adding comment '" + comment + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist comment '" +comment.getId() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestComment=comment;
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test
    public void updateComment() throws Exception {
        Comment comment = new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            comment=commentDAO.updateComment("a", 1, "abc", "a", null);
            Assert.assertEquals(comment.getComment(),"abc");
        } catch (CommentManagementException e) {
            String msg = "Error occurred while updating comment '" + comment + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist comment '" + comment.getId() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestComment=comment;
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test
    public void getComment() throws Exception {
        Comment comment = new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            comment=commentDAO.getComment(1);
            Assert.assertEquals(comment.getComment(),"abc");
        } catch (CommentManagementException e) {
            String msg = "Error occurred while getting comment '" + comment + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist comment '" + comment.getId() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);

        }
        TestDataHolder.initialTestComment=comment;
        ConnectionManagerUtil.endDBTransaction();
    }
    @Test
    public void getAllComments() throws Exception {
        Comment comment = new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            PaginationRequest request=new PaginationRequest(1,5);
            commentDAO.getAllComments("a", request);
        } catch (CommentManagementException e) {
            String msg = "Error occurred while getting comment '" + comment + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist comment '" + comment.getId() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestComment=comment;
        ConnectionManagerUtil.endDBTransaction();

    }

    @Test
    public void getCommentCount() throws Exception {
        Comment comment = new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            int commentCount=commentDAO.getCommentCount("a");
            Assert.assertEquals(commentCount,0);
        } catch (CommentManagementException e) {
            String msg = "Error occurred while counting comment '" + comment + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist comment '" + comment.getId() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestComment=comment;
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test
    public void deleteComment() throws Exception {
        Comment comment = new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            commentDAO.deleteComment(1);
            Assert.assertEquals(comment.getId(),0);
        } catch (CommentManagementException e) {
            String msg = "Error occurred while deleting comment '" + comment + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist comment '" + comment.getId() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestComment=comment;
//        ConnectionManagerUtil.endDBTransaction();
    }

    @Test
    public void updateStars() throws Exception {
        ApplicationRelease app=new ApplicationRelease();
        try {
//            ApplicationManagementDAOFactory.initDatabases();

            ConnectionManagerUtil.beginDBTransaction();

            int updateStars=commentDAO.updateStars(5, "a");
            Assert.assertEquals(updateStars,5);

        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist stars '" + app.getStars() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestApplication=app;
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test
    public void getStars() throws Exception {
        ApplicationRelease app=new ApplicationRelease();
        try {
            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            int getStars=commentDAO.getStars("a");
            Assert.assertEquals(getStars,5);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist stars '" + app.getStars() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestApplication=app;
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test
    public void getRatedUser() throws Exception {
        ApplicationRelease app=new ApplicationRelease();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            int ratedUsers=commentDAO.getRatedUser("a");
            Assert.assertEquals(ratedUsers,1);

        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist rated users '" + app.getNoOfRatedUsers()+ "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestApplication=app;
        ConnectionManagerUtil.endDBTransaction();
    }

}