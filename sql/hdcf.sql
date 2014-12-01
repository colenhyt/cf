/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : hdcf

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2014-12-01 11:26:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `accountid` int(11) NOT NULL auto_increment,
  `accountname` varchar(300) NOT NULL,
  `createtime` datetime default NULL,
  PRIMARY KEY  (`accountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------

-- ----------------------------
-- Table structure for `eventdata`
-- ----------------------------
DROP TABLE IF EXISTS `eventdata`;
CREATE TABLE `eventdata` (
  `eventid` int(11) NOT NULL,
  `type` tinyint(4) default NULL,
  `data` blob,
  `createtime` datetime default NULL,
  `version` int(11) default NULL,
  PRIMARY KEY  (`eventid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of eventdata
-- ----------------------------

-- ----------------------------
-- Table structure for `insure`
-- ----------------------------
DROP TABLE IF EXISTS `insure`;
CREATE TABLE `insure` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `prize` float default NULL,
  `profit` float default NULL,
  `period` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of insure
-- ----------------------------

-- ----------------------------
-- Table structure for `insuredata`
-- ----------------------------
DROP TABLE IF EXISTS `insuredata`;
CREATE TABLE `insuredata` (
  `insureid` int(11) NOT NULL auto_increment,
  `type` tinyint(4) default NULL,
  `data` blob,
  `createtime` datetime default NULL,
  `status` tinyint(4) default NULL,
  `version` float(11,3) default NULL,
  PRIMARY KEY  (`insureid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of insuredata
-- ----------------------------
INSERT INTO `insuredata` VALUES ('1', '2', 0x5B7B226E616D65223A22E9BB84E98791222C2274797065223A312C227072696365223A333030302C2270726F666974223A323030302C22706572696F64223A397D2C7B226E616D65223A22E799BDE993B6222C2274797065223A322C227072696365223A323030302C2270726F666974223A313030302C22706572696F64223A357D2C7B226E616D65223A22E6848FE5A496E999A9222C2274797065223A332C227072696365223A353030302C2270726F666974223A393030302C22706572696F64223A31307D5D, '2014-12-01 11:21:13', '1', '1.000');

-- ----------------------------
-- Table structure for `player`
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `playerid` int(11) NOT NULL auto_increment,
  `playername` varchar(300) NOT NULL COMMENT '名字',
  `pwd` varchar(300) NOT NULL,
  `accountid` int(11) NOT NULL COMMENT '账号id',
  `sex` tinyint(4) NOT NULL default '0',
  `exp` int(11) NOT NULL default '0' COMMENT '等级',
  `job` tinyint(4) NOT NULL default '1',
  `lastsignin` datetime NOT NULL default '0000-00-00 00:00:00',
  `cash` int(11) NOT NULL default '0' COMMENT '现金',
  `savings` blob COMMENT '存款',
  `base` blob,
  `event` blob COMMENT '随机事件',
  `insure` blob COMMENT '保险',
  `stock` blob COMMENT '股票',
  `title` blob COMMENT '称号',
  `quest` blob COMMENT '任务',
  `feelings` blob,
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `version` int(11) NOT NULL default '0' COMMENT '数据结构版本号',
  `versions` varchar(500) default NULL COMMENT '各模块数据版本号',
  PRIMARY KEY  (`playerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player
-- ----------------------------

-- ----------------------------
-- Table structure for `playerlog`
-- ----------------------------
DROP TABLE IF EXISTS `playerlog`;
CREATE TABLE `playerlog` (
  `playerid` int(11) NOT NULL,
  `logtime` datetime NOT NULL,
  `feeling` int(11) NOT NULL,
  PRIMARY KEY  (`playerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of playerlog
-- ----------------------------

-- ----------------------------
-- Table structure for `quest`
-- ----------------------------
DROP TABLE IF EXISTS `quest`;
CREATE TABLE `quest` (
  `id` int(11) NOT NULL,
  `type` tinyint(4) default NULL,
  `name` varchar(100) default NULL,
  `desc` varchar(200) default NULL,
  `need` blob,
  `cost` blob,
  `target` blob,
  `prize` blob,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quest
-- ----------------------------

-- ----------------------------
-- Table structure for `questdata`
-- ----------------------------
DROP TABLE IF EXISTS `questdata`;
CREATE TABLE `questdata` (
  `questid` int(11) NOT NULL auto_increment,
  `type` tinyint(4) default NULL,
  `data` blob,
  `createtime` datetime default NULL,
  `status` tinyint(4) default NULL,
  `version` float(11,3) default NULL,
  PRIMARY KEY  (`questid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of questdata
-- ----------------------------
INSERT INTO `questdata` VALUES ('1', '2', 0x5B7B226E616D65223A22E4B9B0E4B8AAE882A1E7A5A8222C2264657363223A22E696B0E8B4ADE4B9B0E4B880E694AFE882A1E7A5A8222C2274797065223A312C226E656564223A225B7B743A312C763A3130307D5D222C22636F7374223A225B7B743A312C763A317D5D222C22746172676574223A225B7B743A312C763A317D5D222C227072697A65223A225B7B743A312C763A3130307D2C7B743A322C763A3330307D5D227D2C7B226E616D65223A22E4B9B0E4B8AAE4BF9DE999A9222C2264657363223A22E696B0E8B4ADE4B9B0E4B880E6ACBEE4BF9DE999A9E79086E8B4A2E4BAA7E59381222C2274797065223A322C226E656564223A225B7B743A312C763A3130307D5D222C22636F7374223A225B7B743A312C763A317D5D222C22746172676574223A225B7B743A312C763A317D5D222C227072697A65223A225B7B743A312C763A3130307D2C7B743A322C763A3330307D5D227D5D, '2014-12-01 11:19:18', '1', '0.800');

-- ----------------------------
-- Table structure for `signin`
-- ----------------------------
DROP TABLE IF EXISTS `signin`;
CREATE TABLE `signin` (
  `id` int(11) NOT NULL,
  `name` varchar(100) default NULL,
  `day` int(11) default NULL,
  `desc` varchar(200) default NULL,
  `type` int(11) default NULL,
  `value` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of signin
-- ----------------------------

-- ----------------------------
-- Table structure for `signindata`
-- ----------------------------
DROP TABLE IF EXISTS `signindata`;
CREATE TABLE `signindata` (
  `signinid` int(11) NOT NULL auto_increment,
  `type` tinyint(4) NOT NULL,
  `data` blob NOT NULL,
  `createtime` datetime NOT NULL,
  `status` tinyint(4) default NULL COMMENT '当前状态',
  `version` float(11,3) NOT NULL,
  PRIMARY KEY  (`signinid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of signindata
-- ----------------------------
INSERT INTO `signindata` VALUES ('3', '2', 0x5B7B226E616D65223A312C2274797065223A302C2276616C7565223A3130307D2C7B226E616D65223A312C2274797065223A312C2276616C7565223A3330307D2C7B226E616D65223A322C2274797065223A302C2276616C7565223A3230307D2C7B226E616D65223A322C2274797065223A312C2276616C7565223A3530307D5D, '2014-11-23 15:29:27', '0', '0.900');
INSERT INTO `signindata` VALUES ('4', '2', 0x5B7B226E616D65223A312C2274797065223A302C2276616C7565223A3130307D2C7B226E616D65223A312C2274797065223A312C2276616C7565223A3330307D2C7B226E616D65223A322C2274797065223A302C2276616C7565223A3230307D2C7B226E616D65223A322C2274797065223A312C2276616C7565223A3530307D5D, '2014-11-23 15:29:47', '0', '1.000');
INSERT INTO `signindata` VALUES ('5', '2', 0x5B7B22646179223A312C2274797065223A302C2276616C7565223A3130302C2264657363223A22E9A696E697A5227D2C7B22646179223A312C2274797065223A312C2276616C7565223A3330302C2264657363223A22E9A696E697A5227D2C7B22646179223A322C2274797065223A31312C2276616C7565223A3230302C2264657363223A22E6ACA1E697A5227D2C7B22646179223A322C2274797065223A312C2276616C7565223A3530302C2264657363223A22E6ACA1E697A5227D5D, '2014-12-01 10:52:14', '1', '1.100');
INSERT INTO `signindata` VALUES ('6', '2', 0x5B7B22646179223A312C2274797065223A302C2276616C7565223A3130302C2264657363223A22E9A696E697A5227D2C7B22646179223A312C2274797065223A312C2276616C7565223A3330302C2264657363223A22E9A696E697A5227D2C7B22646179223A322C2274797065223A31312C2276616C7565223A3230302C2264657363223A22E6ACA1E697A5227D2C7B22646179223A322C2274797065223A312C2276616C7565223A3530302C2264657363223A22E6ACA1E697A5227D5D, '2014-12-01 10:53:22', '1', '1.100');
INSERT INTO `signindata` VALUES ('7', '2', 0x5B7B22646179223A312C2274797065223A302C2276616C7565223A3130302C2264657363223A22E9A696E697A5227D2C7B22646179223A312C2274797065223A312C2276616C7565223A3330302C2264657363223A22E9A696E697A5227D2C7B22646179223A322C2274797065223A31312C2276616C7565223A3230302C2264657363223A22E6ACA1E697A5227D2C7B22646179223A322C2274797065223A312C2276616C7565223A3530302C2264657363223A22E6ACA1E697A5227D5D, '2014-12-01 10:54:56', '1', '1.100');
INSERT INTO `signindata` VALUES ('8', '2', 0x5B7B22646179223A312C2274797065223A302C2276616C7565223A3130302C2264657363223A22E9A696E697A5227D2C7B22646179223A312C2274797065223A312C2276616C7565223A3330302C2264657363223A22E9A696E697A5227D2C7B22646179223A322C2274797065223A31312C2276616C7565223A3230302C2264657363223A22E6ACA1E697A5227D2C7B22646179223A322C2274797065223A312C2276616C7565223A3530302C2264657363223A22E6ACA1E697A5227D5D, '2014-12-01 10:55:06', '1', '1.100');

-- ----------------------------
-- Table structure for `title`
-- ----------------------------
DROP TABLE IF EXISTS `title`;
CREATE TABLE `title` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL COMMENT '名字',
  `desc` varchar(1000) default NULL,
  `exp` int(11) default NULL COMMENT '条件',
  `level` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of title
-- ----------------------------

-- ----------------------------
-- Table structure for `titledata`
-- ----------------------------
DROP TABLE IF EXISTS `titledata`;
CREATE TABLE `titledata` (
  `titleid` int(11) NOT NULL auto_increment,
  `type` tinyint(4) NOT NULL,
  `data` blob NOT NULL,
  `createtime` datetime NOT NULL,
  `status` tinyint(4) default NULL,
  `version` float(11,3) NOT NULL,
  PRIMARY KEY  (`titleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of titledata
-- ----------------------------
INSERT INTO `titledata` VALUES ('1', '2', 0x5B7B226E616D65223A22E5889DE5A78BE5B08FE59C9FE8B186222C2264657363223A22E5BD93E5A4A9E8B4A2E5AF8CE5A29EE995BFE8B685E8BF8735304B222C22657870223A3130302C226C6576656C223A317D2C7B226E616D65223A22E5B08FE59C9FE8B186222C2264657363223A22E69BBEE88EB7E5BE97E8BF87E8B4A2E5AF8CE6A69CE7ACACE4B880E5908D222C22657870223A3230302C226C6576656C223A327D2C7B226E616D65223A22E4B8ADE59C9FE8B186222C2264657363223A22E4B8ADE59C9FE8B186222C22657870223A3330302C226C6576656C223A337D5D, '2014-12-01 11:23:55', '1', '0.800');

-- ----------------------------
-- Table structure for `toplist`
-- ----------------------------
DROP TABLE IF EXISTS `toplist`;
CREATE TABLE `toplist` (
  `id` int(11) NOT NULL auto_increment,
  `type` int(11) default NULL,
  `roleid` int(11) default NULL,
  `score` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of toplist
-- ----------------------------
INSERT INTO `toplist` VALUES ('1', '1', '1', '1111');
INSERT INTO `toplist` VALUES ('2', '1', '2', '999');
INSERT INTO `toplist` VALUES ('3', '1', '33', '888');
