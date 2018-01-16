package org.wso2.carbon.device.application.mgt.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.wso2.carbon.device.application.mgt.common.*;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;
import org.wso2.carbon.device.application.mgt.common.exception.CommentManagementException;
import org.wso2.carbon.device.application.mgt.core.dao.common.ApplicationManagementDAOFactory;
import org.wso2.carbon.device.application.mgt.core.util.ConnectionManagerUtil;
import org.wso2.carbon.device.mgt.core.dao.DeviceManagementDAOFactory;
import org.wso2.carbon.device.mgt.core.dao.DeviceTypeDAO;
import org.wso2.carbon.device.mgt.core.dto.DeviceType;

import java.util.ArrayList;
import java.util.List;

public class CommentDAOTest extends BaseCommentManagementTest {

    CommentDAO commentDAO;
    ApplicationReleaseDAO applicationReleaseDAO;
    DeviceTypeDAO deviceTypeDAO;
    ApplicationDAO applicationDAO;
    private static final Log log = LogFactory.getLog(CommentDAOTest.class);

    int tenantId=TestDataHolder.SUPER_TENANT_ID;
    DeviceType deviceType=TestDataHolder.generateDummyDeviceType();
    //        DeviceIdentifier deviceIdentifier=TestDataHolder.generateDeviceTypeData("abc");
    Comment comment=TestDataHolder.generateDummyCommentData(TestDataHolder.TEST_COMMENT);
    Application app=TestDataHolder.generateApplicationDummyData(TestDataHolder.initialDeviceIdentifier);
    ApplicationRelease appRelease=TestDataHolder.generateApplicationReleaseDummyData(1);

    @BeforeClass
    public void init() throws Exception {
        initDataSource();
        commentDAO = ApplicationManagementDAOFactory.getCommentDAO();
        applicationReleaseDAO=ApplicationManagementDAOFactory.getApplicationReleaseDAO();
        deviceTypeDAO=ApplicationManagementDAOFactory.getDeviceTypeDAO();
        applicationDAO=ApplicationManagementDAOFactory.getApplicationDAO();
    }

    @Test
    public void addComment() throws Exception {

        try {
//            DeviceManagementDAOFactory.beginTransaction();
            ApplicationManagementDAOFactory.init(getDataSource());
            DeviceManagementDAOFactory.openConnection();
            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
             int deviceTypeId=deviceType.getId();

//            ConnectionManagerUtil.rollbackDBTransaction();
            applicationDAO.createApplication(app);

            applicationDAO.getApplication("a",tenantId,"abc");
            applicationReleaseDAO.createRelease(appRelease);
            int apAppCommentId=commentDAO.addComment(123,comment,"abc",1,"abc");
            Assert.assertEquals(apAppCommentId,1,"comment is added");
            ConnectionManagerUtil.endDBTransaction();

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

    @Test(dependsOnMethods = {"addComment"})
    public void updateComment() throws Exception {
        Comment newComment= new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();

            ApplicationManagementDAOFactory.init(getDataSource());
//            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
//            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
            int deviceTypeId=deviceType.getId();
            applicationDAO.createApplication(app);
            applicationDAO.getApplication("a",tenantId,"abc");
            applicationReleaseDAO.createRelease(appRelease);
            int apAppCommentId=commentDAO.addComment(123,comment,"abc",1,"a");

            newComment=commentDAO.updateComment("a", apAppCommentId, "abc", "a", null);
            Assert.assertEquals(newComment.getComment(),"abc");
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

    @Test(dependsOnMethods = {"addComment"})
    public void getComment() throws Exception {
        Comment newComment = new Comment();
        try {

/**            ConnectionManagerUtil.beginDBTransaction();
            ApplicationManagementDAOFactory.init(getDataSource());
            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
            int deviceTypeId=deviceType.getId();
            applicationDAO.createApplication(app);
            applicationDAO.getApplication("a",tenantId,"abc");
            applicationReleaseDAO.createRelease(appRelease);
            int apAppCommentId=commentDAO.addComment(123,comment,"abc",1,"a");
            newComment=commentDAO.getComment(apAppCommentId);
            Assert.assertEquals(newComment.getComment(),"Test");**/ //unit test when comment is added.

//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            newComment=commentDAO.getComment(1);
            Assert.assertEquals(newComment.getComment(),null);
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
    @Test(dependsOnMethods = {"addComment"})
    public void getAllComments() throws Exception {
        List<Comment> commentList = new ArrayList<>();
        List<Comment> testList=new ArrayList<>();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();
            PaginationRequest request=new PaginationRequest(1,5);

 /**           ApplicationManagementDAOFactory.init(getDataSource());
//            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
//            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
            int deviceTypeId=deviceType.getId();
            applicationDAO.createApplication(app);
            applicationDAO.getApplication("a",tenantId,"abc");
            applicationReleaseDAO.createRelease(appRelease);
            int apAppCommentId=commentDAO.addComment(123,comment,"abc",1,"a");

            ApplicationManagementDAOFactory.init(getDataSource());
//            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
//            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
            int deviceTypeId2=2;
            applicationDAO.createApplication(TestDataHolder.generateApplicationDummyData(2));
            applicationDAO.getApplication("a",tenantId,"abc");
            applicationReleaseDAO.createRelease(TestDataHolder.generateApplicationReleaseDummyData(2));
            int apAppCommentId2=commentDAO.addComment(123,TestDataHolder.generateDummyCommentData("Test2"),"abc",1,"a");**/ //When comment list size is 2

            commentList=commentDAO.getAllComments("a", request);

         /**   Comment comment1=new Comment();
            comment1.setId(2);
            comment1.setTenantId(123);
            comment1.setCreatedBy("abc");
            comment1.setComment("Test2");
            comment1.setParent(0);
            testList.add(comment1);
            testList.add(comment);**/ //When comment list size is 2

            Assert.assertEquals(commentList,testList);
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

    @Test(dependsOnMethods = {"addComment"})
    public void getCommentCount() throws Exception {
        Comment comment = new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
/**
            ApplicationManagementDAOFactory.init(getDataSource());
//            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
//            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
            int deviceTypeId=deviceType.getId();
            applicationDAO.createApplication(app);
            applicationDAO.getApplication("a",tenantId,"abc");
            applicationReleaseDAO.createRelease(appRelease);
            int apAppCommentId=commentDAO.addComment(123,comment,"abc",1,"a");
            int commentCount=commentDAO.getCommentCount("a");
            Assert.assertEquals(commentCount,1);**/ //when one comment is added.

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

    @Test(dependsOnMethods = {"addComment"})
    public void deleteComment() throws Exception {
        Comment comment = new Comment();
        try {
//            ApplicationManagementDAOFactory.initDatabases();

            /**
             ApplicationManagementDAOFactory.init(getDataSource());
             //            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
             //            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
             int deviceTypeId=deviceType.getId();
             applicationDAO.createApplication(app);
             applicationDAO.getApplication("a",tenantId,"abc");
             applicationReleaseDAO.createRelease(appRelease);
             int apAppCommentId=commentDAO.addComment(123,comment,"abc",1,"a");
             int commentCount=commentDAO.getCommentCount("a");
             Assert.assertEquals(commentCount,1);**/ //when one comment is added.

            ConnectionManagerUtil.beginDBTransaction();
            commentDAO.deleteComment(1);
            Assert.assertEquals(commentDAO.getComment(1).getId(),0);
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
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test
    public void updateStars() throws Exception {
        ApplicationRelease newAppRelease=new ApplicationRelease();
        try {
//            ApplicationManagementDAOFactory.initDatabases();

            ConnectionManagerUtil.beginDBTransaction();

           /** ApplicationManagementDAOFactory.init(getDataSource());
            //            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
            //            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
            int deviceTypeId=deviceType.getId();
            applicationDAO.createApplication(app);
            applicationDAO.getApplication("a",tenantId,"abc");
            applicationReleaseDAO.createRelease(appRelease);**/

            int updateStars=commentDAO.updateStars(5, "a");
            Assert.assertEquals(updateStars,0);

        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist stars '" + newAppRelease.getStars() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
//            resultSet = stmt.executeQuery(sql);
//            if (resultSet != null) {
//                resultSet.getInt("STARS");
//            }
//            int numORows=resultSet.getRow();
        TestDataHolder.initialTestApplication=newAppRelease;
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test(dependsOnMethods = {"updateStars"})
    public void getStars() throws Exception {
        ApplicationRelease app=new ApplicationRelease();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();

            /** ApplicationManagementDAOFactory.init(getDataSource());
             //            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
             //            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
             int deviceTypeId=deviceType.getId();
             applicationDAO.createApplication(app);
             applicationDAO.getApplication("a",tenantId,"abc");
             applicationReleaseDAO.createRelease(appRelease);**/

            int getStars=commentDAO.getStars("a");
            Assert.assertEquals(getStars,0);
        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist stars '" + app.getStars() + "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestApplication=app;
        ConnectionManagerUtil.endDBTransaction();
    }

    @Test(dependsOnMethods = {"updateStars"})
    public void getRatedUser() throws Exception {
        ApplicationRelease newAppRelease=new ApplicationRelease();
        try {
//            ApplicationManagementDAOFactory.initDatabases();
            ConnectionManagerUtil.beginDBTransaction();

             /**ApplicationManagementDAOFactory.init(getDataSource());
             //            DeviceTypeDAO deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
             //            deviceTypeDAO.addDeviceType(deviceType, -1234, true);//connection is null
             int deviceTypeId=deviceType.getId();
             applicationDAO.createApplication(app);
             applicationDAO.getApplication("a",tenantId,"abc");
             applicationReleaseDAO.createRelease(appRelease);**/

            int ratedUsers=commentDAO.getRatedUser("a");
            Assert.assertEquals(ratedUsers,0);

        } catch (ApplicationManagementException e) {
            String msg = "Error occurred while initiating transaction to persist rated users '" + newAppRelease.getNoOfRatedUsers()+ "'";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        TestDataHolder.initialTestApplication=newAppRelease;
        ConnectionManagerUtil.endDBTransaction();
    }

}