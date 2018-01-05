





















-- MySQL Script generated by MySQL Workbench
-- 2017-11-20 14:33:46 +0530
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering
-- -----------------------------------------------------
-- Schema APP_MANAGER
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema APP_MANAGER
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `APP_MANAGER` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`DM_DEVICE_TYPE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DM_DEVICE_TYPE` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(300) NULL DEFAULT NULL,
  `DEVICE_TYPE_META` VARCHAR(20000) NULL DEFAULT NULL,
  `LAST_UPDATED_TIMESTAMP` TIMESTAMP NOT NULL,
  `PROVIDER_TENANT_ID` INT(11) NULL DEFAULT '0',
  `SHARED_WITH_ALL_TENANTS` TINYINT(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`));
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_APP`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_APP` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  `TYPE` VARCHAR(200) NOT NULL,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `APP_CATEGORY` VARCHAR(45) NULL DEFAULT NULL,
  `IS_FREE` INT(11) NOT NULL DEFAULT '1',
  `PAYMENT_CURRENCY` VARCHAR(45) NULL DEFAULT NULL,
  `RESTRICTED` INT(11) NOT NULL,
  `DM_DEVICE_TYPE_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `TENANT_ID_UNIQUE` (`TENANT_ID` ASC),
--  INDEX `fk_AP_APP_DM_DEVICE_TYPE1_idx` (`DM_DEVICE_TYPE_ID` ASC),
  CONSTRAINT `fk_AP_APP_DM_DEVICE_TYPE1`
    FOREIGN KEY (`DM_DEVICE_TYPE_ID`)
    REFERENCES `DM_DEVICE_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_APP_RELEASE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_APP_RELEASE` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `VERSION` VARCHAR(10) NOT NULL,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `UUID` VARCHAR(200) NOT NULL UNIQUE,
  `RELEASE_TYPE` VARCHAR(45) NOT NULL,
  `APP_PRICE` DECIMAL(6,2) NULL DEFAULT NULL,
  `STORED_LOCATION` VARCHAR(45) NOT NULL,
  `BANNER_LOCATION` VARCHAR(45) NOT NULL,
  `SC_1_LOCATION` VARCHAR(45) NOT NULL,
  `SC_2_LOCATION` VARCHAR(45) NULL DEFAULT NULL,
  `SC_3_LOCATION` VARCHAR(45) NULL DEFAULT NULL,
  `APP_HASH_VALUE` VARCHAR(1000) NOT NULL,
  `SHARED_WITH_ALL_TENANTS` INT(11) NULL DEFAULT NULL,
  `APP_META_INFO` VARCHAR(20000) NULL DEFAULT NULL,
  `CREATED_BY` VARCHAR(45) NOT NULL,
  `CREATED_AT` TIMESTAMP NOT NULL,
  `PUBLISHED_BY` VARCHAR(45) NULL DEFAULT NULL,
  `PUBLISHED_AT` TIMESTAMP NULL DEFAULT NULL,
  `STARS` INT(11) NULL DEFAULT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
   `NO_OF_RATED_USERS` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`, `AP_APP_ID`),
--  INDEX `fk_AP_APP_RELEASE_AP_APP1_idx` (`AP_APP_ID` ASC),
  CONSTRAINT `fk_AP_APP_RELEASE_AP_APP1`
    FOREIGN KEY (`AP_APP_ID`)
    REFERENCES `AP_APP` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_APP_COMMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_APP_COMMENT` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `COMMENT_TEXT` VARCHAR(250) NOT NULL,
  `CREATED_AT` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATED_BY` VARCHAR(45) NOT NULL,
  `MODEFIED_AT` TIMESTAMP NULL DEFAULT NULL,
  `MODEFIED_BY` VARCHAR(45) NULL DEFAULT NULL,
  `PARENT_ID` INT(11) NULL DEFAULT NULL,
  `AP_APP_RELEASE_ID` INT(11) NOT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
--  INDEX `fk_AP_APP_COMMENT_AP_APP_RELEASE1_idx` (`AP_APP_RELEASE_ID` ASC, `AP_APP_ID` ASC),
  CONSTRAINT `fk_AP_APP_COMMENT_AP_APP_RELEASE1`
    FOREIGN KEY (`AP_APP_RELEASE_ID` , `AP_APP_ID`)
    REFERENCES `AP_APP_RELEASE` (`ID` , `AP_APP_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_APP_LIFECYCLE_STATE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_APP_LIFECYCLE_STATE` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CURRENT_STATE` VARCHAR(45) NOT NULL,
  `PREVIOUSE_STATE` VARCHAR(45) NOT NULL,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `UPDATED_BY` VARCHAR(100) NOT NULL,
  `UPDATED_AT` TIMESTAMP NOT NULL,
  `AP_APP_RELEASE_ID` INT(11) NOT NULL,
  `AP_APP_RELEASE_AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`, `AP_APP_RELEASE_ID`, `AP_APP_RELEASE_AP_APP_ID`),
--  INDEX `fk_AP_APP_LIFECYCLE_STATE_AP_APP_RELEASE1_idx` (`AP_APP_RELEASE_ID` ASC, `AP_APP_RELEASE_AP_APP_ID` ASC),
  CONSTRAINT `fk_AP_APP_LIFECYCLE_STATE_AP_APP_RELEASE1`
    FOREIGN KEY (`AP_APP_RELEASE_ID` , `AP_APP_RELEASE_AP_APP_ID`)
    REFERENCES `AP_APP_RELEASE` (`ID` , `AP_APP_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_APP_TAG`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_APP_TAG` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `TAG` VARCHAR(45) NOT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
--  INDEX `fk_AP_APP_TAGS_AP_APP1_idx` (`AP_APP_ID` ASC),
  CONSTRAINT `fk_AP_APP_TAGS_AP_APP1`
    FOREIGN KEY (`AP_APP_ID`)
    REFERENCES `AP_APP` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`DM_DEVICE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DM_DEVICE` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` TEXT NULL DEFAULT NULL,
  `NAME` VARCHAR(100) NULL DEFAULT NULL,
  `DEVICE_TYPE_ID` INT(11) NULL DEFAULT NULL,
  `DEVICE_IDENTIFICATION` VARCHAR(250) NULL DEFAULT NULL,
  `LAST_UPDATED_TIMESTAMP` TIMESTAMP NOT NULL,
  `TENANT_ID` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `uk_DM_DEVICE` (`DEVICE_TYPE_ID` ASC, `DEVICE_IDENTIFICATION` ASC, `TENANT_ID` ASC),
--  INDEX `fk_DM_DEVICE_DM_DEVICE_TYPE2` (`DEVICE_TYPE_ID` ASC),
  CONSTRAINT `fk_DM_DEVICE_DM_DEVICE_TYPE2`
    FOREIGN KEY (`DEVICE_TYPE_ID`)
    REFERENCES `DM_DEVICE_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_DEVICE_SUBSCRIPTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_DEVICE_SUBSCRIPTION` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `SUBSCRIBED_BY` VARCHAR(100) NOT NULL,
  `SUBSCRIBED_TIMESTAMP` TIMESTAMP NOT NULL,
  `UNSUBSCRIBED` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_BY` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
  `DM_DEVICE_ID` INT(11) NOT NULL,
  `AP_APP_RELEASE_ID` INT(11) NOT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
--  INDEX `fk_AM_DEVICE_SUBSCRIPTION_DM_DEVICE1_idx` (`DM_DEVICE_ID` ASC),
--  INDEX `fk_AP_DEVICE_SUBSCRIPTION_AP_APP_RELEASE1_idx` (`AP_APP_RELEASE_ID` ASC, `AP_APP_ID` ASC),
  CONSTRAINT `fk_AM_DEVICE_SUBSCRIPTION_DM_DEVICE1`
    FOREIGN KEY (`DM_DEVICE_ID`)
    REFERENCES `DM_DEVICE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AP_DEVICE_SUBSCRIPTION_AP_APP_RELEASE1`
    FOREIGN KEY (`AP_APP_RELEASE_ID` , `AP_APP_ID`)
    REFERENCES `AP_APP_RELEASE` (`ID` , `AP_APP_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--ENGINE = InnoDB
--DEFAULT CHARACTER SET = utf8;
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`DM_GROUP`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DM_GROUP` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` VARCHAR(100) NULL DEFAULT NULL,
  `DESCRIPTION` TEXT NULL DEFAULT NULL,
  `OWNER` VARCHAR(45) NULL DEFAULT NULL,
  `TENANT_ID` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`ID`));
--ENGINE = InnoDB
--DEFAULT CHARACTER SET = utf8;
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_GROUP_SUBSCRIPTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_GROUP_SUBSCRIPTION` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `SUBSCRIBED_BY` VARCHAR(100) NOT NULL,
  `SUBSCRIBED_TIMESTAMP` TIMESTAMP NOT NULL,
  `UNSUBSCRIBED` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_BY` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
  `DM_GROUP_ID` INT(11) NOT NULL,
  `AP_APP_RELEASE_ID` INT(11) NOT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
--  INDEX `fk_AM_GROUP_SUBSCRIPTION_DM_GROUP1_idx` (`DM_GROUP_ID` ASC),
--  INDEX `fk_AP_GROUP_SUBSCRIPTION_AP_APP_RELEASE1_idx` (`AP_APP_RELEASE_ID` ASC, `AP_APP_ID` ASC),
  CONSTRAINT `fk_AM_GROUP_SUBSCRIPTION_DM_GROUP1`
    FOREIGN KEY (`DM_GROUP_ID`)
    REFERENCES `DM_GROUP` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AP_GROUP_SUBSCRIPTION_AP_APP_RELEASE1`
    FOREIGN KEY (`AP_APP_RELEASE_ID` , `AP_APP_ID`)
    REFERENCES `AP_APP_RELEASE` (`ID` , `AP_APP_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--ENGINE = InnoDB
--DEFAULT CHARACTER SET = utf8;
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_ROLE_SUBSCRIPTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_ROLE_SUBSCRIPTION` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `ROLE_NAME` VARCHAR(100) NOT NULL,
  `SUBSCRIBED_BY` VARCHAR(100) NOT NULL,
  `SUBSCRIBED_TIMESTAMP` TIMESTAMP NOT NULL,
  `UNSUBSCRIBED` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_BY` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
  `AP_APP_RELEASE_ID` INT(11) NOT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
--  INDEX `fk_AP_ROLE_SUBSCRIPTION_AP_APP_RELEASE1_idx` (`AP_APP_RELEASE_ID` ASC, `AP_APP_ID` ASC),
  CONSTRAINT `fk_AP_ROLE_SUBSCRIPTION_AP_APP_RELEASE1`
    FOREIGN KEY (`AP_APP_RELEASE_ID` , `AP_APP_ID`)
    REFERENCES `AP_APP_RELEASE` (`ID` , `AP_APP_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--ENGINE = InnoDB
--DEFAULT CHARACTER SET = utf8;
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_UNRESTRICTED_ROLES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_UNRESTRICTED_ROLES` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `ROLE` VARCHAR(45) NOT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
--  INDEX `fk_AP_APP_VISIBILITY_AP_APP1_idx` (`AP_APP_ID` ASC),
  CONSTRAINT `fk_AP_APP_VISIBILITY_AP_APP1`
    FOREIGN KEY (`AP_APP_ID`)
    REFERENCES `AP_APP` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--ENGINE = InnoDB
--DEFAULT CHARACTER SET = utf8;
-- -----------------------------------------------------
-- Table `APP_MANAGER`.`AP_USER_SUBSCRIPTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AP_USER_SUBSCRIPTION` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` VARCHAR(45) NOT NULL,
  `USER_NAME` VARCHAR(100) NOT NULL,
  `SUBSCRIBED_BY` VARCHAR(100) NOT NULL,
  `SUBSCRIBED_TIMESTAMP` TIMESTAMP NOT NULL,
  `UNSUBSCRIBED` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_BY` INT(11) NULL DEFAULT NULL,
  `UNSUBSCRIBED_TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
  `AP_APP_RELEASE_ID` INT(11) NOT NULL,
  `AP_APP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
--  INDEX `fk_AP_USER_SUBSCRIPTION_AP_APP_RELEASE1_idx` (`AP_APP_RELEASE_ID` ASC, `AP_APP_ID` ASC),
  CONSTRAINT `fk_AP_USER_SUBSCRIPTION_AP_APP_RELEASE1`
    FOREIGN KEY (`AP_APP_RELEASE_ID` , `AP_APP_ID`)
    REFERENCES `AP_APP_RELEASE` (`ID` , `AP_APP_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--ENGINE = InnoDB
--DEFAULT CHARACTER SET = utf8;
