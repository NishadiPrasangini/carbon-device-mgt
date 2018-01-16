/*
* Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.device.application.mgt.core.dao;

import org.wso2.carbon.device.application.mgt.common.*;
import org.wso2.carbon.device.mgt.common.EnrolmentInfo;
import org.wso2.carbon.device.mgt.common.group.mgt.DeviceGroup;
import org.wso2.carbon.device.mgt.common.type.mgt.DeviceTypeMetaDefinition;
import org.wso2.carbon.device.mgt.core.dto.DeviceType;


import java.util.Date;
import java.util.Properties;

public class TestDataHolder {

    public final static String TEST_COMMENT = "Test";
    public final static Integer SUPER_TENANT_ID = -1234;
    public final static String SUPER_TENANT_DOMAIN = "carbon.super";
    public final static Integer initialDeviceIdentifier = 1;
    public final static String CREATED_BY="abc";
    public final static String UUID="abc";
    public static Comment initialTestComment;
    public  static ApplicationRelease initialTestApplication;

    public static Comment generateDummyCommentData(String text){

        Comment comment = new Comment();
        Application app=new Application();
        app.setUuid(UUID);
        comment.setComment(TEST_COMMENT);
        comment.setCreatedBy(CREATED_BY);
        comment.setTenantId(SUPER_TENANT_ID);
        comment.setId(initialDeviceIdentifier);
        return comment;

//        device.setEnrolmentInfo(enrolmentInfo);
    }

    public static Comment generateDeviceTypeData(String comments){
        Comment comment = new Comment();
        comment.setCreatedBy(comments);
        return comment;
    }

    public static Application generateApplicationDummyData(int appIdentifier){

        Application application = new Application();
        Properties properties = new Properties();
        properties.setProperty("test1","testVal");
        application.setId(initialDeviceIdentifier);
        application.setUuid("a");
        application.setName("abc");
        application.setShortDescription("abc");
        application.setDescription("abc");
        application.setVideoName("abc");
        application.setScreenShotCount(1);
        User user=new User("abc",123);
        application.setUser(user);
        Date date=new Date();
        application.setCreatedAt(date);
        application.setModifiedAt(date);
        Category category=new Category();
        application.setCategory(category);
        Platform platform=new Platform();
        application.setPlatform(platform);
        Lifecycle lifecycle=new Lifecycle();
        application.setCurrentLifecycle(lifecycle);

//        application.setName("SimpleCalculator");
//        application.setCategory("TestCategory");
//        application.setId(appIdentifier);
//        application.set.setType("TestType");
//        application.se.setVersion("1.0.0");
//        application.setImageUrl("http://test.org/image/");
//        application.setLocationUrl("http://test.org/location/");
//        application.setAppProperties(properties);

        return application;
    }
    public static ApplicationRelease generateApplicationReleaseDummyData(int appIdentifier){

        ApplicationRelease applicationRelease = new ApplicationRelease();
        Properties properties = new Properties();
        properties.setProperty("test1","testVal");
        applicationRelease.setId(initialDeviceIdentifier);

        applicationRelease.setVersionName("abc");
        applicationRelease.setResource("abc");
//        applicationRelease.setReleaseChannel("abc");
        applicationRelease.setReleaseDetails("abc");
        Date date=new Date();
        applicationRelease.setCreatedAt(date);
        Application application=new Application();
        applicationRelease.setApplication(application);
//        applicationRelease.isDefault();

//        application.setName("SimpleCalculator");
//        application.setCategory("TestCategory");
//        application.setId(appIdentifier);
//        application.set.setType("TestType");
//        application.se.setVersion("1.0.0");
//        application.setImageUrl("http://test.org/image/");
//        application.setLocationUrl("http://test.org/location/");
//        application.setAppProperties(properties);
        return applicationRelease;
    }
    public static DeviceType generateDummyDeviceType(){
        DeviceType deviceType=new DeviceType();
        deviceType.setName("abc");
        DeviceTypeMetaDefinition deviceTypeMetaDefinition=new DeviceTypeMetaDefinition();
        deviceType.setDeviceTypeMetaDefinition(deviceTypeMetaDefinition);
        deviceType.setId(1);
        return deviceType;
    }

//    public static DeviceGroup generateDummyGroupData() {
//        DeviceGroup deviceGroup = new DeviceGroup();
//        deviceGroup.setName("Test device group");
//        deviceGroup.setDescription("Test description");
//        deviceGroup.setOwner(OWNER);
//        return deviceGroup;
//    }
}
