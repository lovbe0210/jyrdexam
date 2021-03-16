/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.16 : Database - exam
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`exam` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `exam`;

/*Table structure for table `exm_exam` */

DROP TABLE IF EXISTS `exm_exam`;

CREATE TABLE `exm_exam` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `NAME` varchar(32) DEFAULT NULL COMMENT '名称',
  `Exam_Duration` int(11) DEFAULT NULL COMMENT '考试时长：分钟',
  `max_exam_count` int(11) DEFAULT NULL COMMENT '考试次数',
  `PASS_SCORE` decimal(5,2) DEFAULT NULL COMMENT '及格分数',
  `DESCRIPTION` text COMMENT '描述',
  `START_TIME` datetime DEFAULT NULL COMMENT '开始时间',
  `END_TIME` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `STATE` int(11) DEFAULT NULL COMMENT '0：未发布；1：进行中；2：已结束',
  `PAPER_ID` int(11) DEFAULT NULL COMMENT '试卷ID',
  `catalog_id` int(11) DEFAULT NULL COMMENT '考试分类',
  PRIMARY KEY (`ID`),
  KEY `FK_Reference_19` (`PAPER_ID`),
  KEY `FK_Reference_22` (`catalog_id`),
  CONSTRAINT `FK_Reference_19` FOREIGN KEY (`PAPER_ID`) REFERENCES `exm_paper` (`ID`),
  CONSTRAINT `FK_Reference_22` FOREIGN KEY (`catalog_id`) REFERENCES `exm_exam_catalog` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*Data for the table `exm_exam` */

insert  into `exm_exam`(`ID`,`NAME`,`Exam_Duration`,`max_exam_count`,`PASS_SCORE`,`DESCRIPTION`,`START_TIME`,`END_TIME`,`create_time`,`update_time`,`STATE`,`PAPER_ID`,`catalog_id`) values (20,'发电部2020年安规考试',120,10,'85.00','','2021-03-07 02:58:03','2021-12-01 00:00:00','2021-03-07 02:58:24','2021-03-07 02:59:31',1,8,1),(21,'发电部2030年安规考试',90,10,'10.00','','2021-03-09 00:03:06','2021-03-31 23:55:00','2021-03-09 00:03:23','2021-03-09 00:03:26',1,8,1);

/*Table structure for table `exm_exam_catalog` */

DROP TABLE IF EXISTS `exm_exam_catalog`;

CREATE TABLE `exm_exam_catalog` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `NAME` varchar(32) DEFAULT NULL COMMENT '名称',
  `PARENT_ID` int(11) DEFAULT NULL COMMENT '父ID',
  `PARENT_SUB` varchar(512) DEFAULT NULL COMMENT '父子关系（格式：_父ID_子ID_子子ID_... ...）',
  `UPDATE_USER_ID` int(11) DEFAULT NULL COMMENT '修改人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `STATE` int(11) DEFAULT NULL COMMENT '0：删除；1：正常',
  `NO` int(11) DEFAULT NULL COMMENT '排序',
  `USER_IDS` varchar(1024) DEFAULT NULL COMMENT '用户权限。多用户用逗号分隔，如：,2,45,66,57,',
  `ORG_IDS` varchar(1024) DEFAULT NULL COMMENT '机构权限',
  `POST_IDS` varchar(1024) DEFAULT NULL COMMENT '岗位权限',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `exm_exam_catalog` */

insert  into `exm_exam_catalog`(`ID`,`NAME`,`PARENT_ID`,`PARENT_SUB`,`UPDATE_USER_ID`,`UPDATE_TIME`,`STATE`,`NO`,`USER_IDS`,`ORG_IDS`,`POST_IDS`) values (1,'--选择考试分类--',0,'_1_',1,'2017-08-01 22:31:43',1,1,NULL,NULL,NULL),(2,'安规考试',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'运规考试',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'三种人考试',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `exm_exam_dept` */

DROP TABLE IF EXISTS `exm_exam_dept`;

CREATE TABLE `exm_exam_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_id` int(11) DEFAULT NULL COMMENT '考试id',
  `dept_id` int(11) DEFAULT NULL COMMENT '参与考试的部门id',
  `dept_name` varchar(25) DEFAULT NULL COMMENT '参与考试的部门名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

/*Data for the table `exm_exam_dept` */

insert  into `exm_exam_dept`(`id`,`exam_id`,`dept_id`,`dept_name`) values (10,10,1,'发电部'),(11,10,2,'检修部'),(12,10,3,'白宫'),(13,11,1,'发电部'),(14,12,1,'发电部'),(15,13,1,'发电部'),(16,14,1,'发电部'),(17,15,1,'发电部'),(18,15,2,'检修部'),(19,16,1,'发电部'),(20,17,1,'发电部'),(21,18,1,'发电部'),(22,19,1,'发电部'),(23,20,1,'发电部'),(24,21,1,'发电部');

/*Table structure for table `exm_exam_user` */

DROP TABLE IF EXISTS `exm_exam_user`;

CREATE TABLE `exm_exam_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `EXAM_ID` int(11) DEFAULT NULL COMMENT '考试ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID',
  `MAX_SCORE` double(5,2) DEFAULT NULL COMMENT '最高分',
  `EXAMED_COUNT` int(11) DEFAULT NULL COMMENT '已考次数',
  `BEGIN_TIME` datetime DEFAULT NULL COMMENT '开始考试时间',
  `Exam_Duration` int(11) DEFAULT NULL COMMENT '考试时长，单位秒',
  `STATE` int(11) DEFAULT NULL COMMENT '1：未考试；2：考试中；3：已交卷；4：强制交卷；5：判卷中；6：未通过；7：已通过',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_Reference_23` (`EXAM_ID`),
  CONSTRAINT `FK_Reference_23` FOREIGN KEY (`EXAM_ID`) REFERENCES `exm_exam` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `exm_exam_user` */

/*Table structure for table `exm_exam_user_question` */

DROP TABLE IF EXISTS `exm_exam_user_question`;

CREATE TABLE `exm_exam_user_question` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `EXAM_USER_ID` int(11) DEFAULT NULL COMMENT '考试用户ID',
  `EXAM_ID` int(11) DEFAULT NULL COMMENT '考试ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID(没有考虑删除)',
  `QUESTION_ID` int(11) DEFAULT NULL COMMENT '试题ID',
  `QUESTION_TYPE` int(11) DEFAULT NULL COMMENT '2单选3多选4判断5填空6简答',
  `UPDATE_USER_ID` int(11) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `ANSWER` text COMMENT '答案',
  `SCORE` double(5,2) DEFAULT NULL COMMENT '得分',
  `single_Score` double(4,1) DEFAULT NULL COMMENT '单项得分',
  PRIMARY KEY (`ID`),
  KEY `FK_Reference_24` (`EXAM_USER_ID`),
  KEY `FK_Reference_25` (`QUESTION_ID`),
  CONSTRAINT `FK_Reference_24` FOREIGN KEY (`EXAM_USER_ID`) REFERENCES `exm_exam_user` (`ID`),
  CONSTRAINT `FK_Reference_25` FOREIGN KEY (`QUESTION_ID`) REFERENCES `exm_question` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4081 DEFAULT CHARSET=utf8;

/*Data for the table `exm_exam_user_question` */

/*Table structure for table `exm_paper` */

DROP TABLE IF EXISTS `exm_paper`;

CREATE TABLE `exm_paper` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(32) DEFAULT NULL COMMENT '名称',
  `DESCRIPTION` text COMMENT '描述',
  `TOTLE_SCORE` decimal(5,2) DEFAULT NULL COMMENT '总分数',
  `catalog_id` int(11) DEFAULT NULL COMMENT '试卷分类',
  `STATE` int(11) DEFAULT NULL COMMENT '0：未配置不能发布；1：已配置可发布',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  KEY `fk_paper_catalog` (`catalog_id`),
  CONSTRAINT `exm_paper_ibfk_1` FOREIGN KEY (`catalog_id`) REFERENCES `sys_catalog` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `exm_paper` */

insert  into `exm_paper`(`ID`,`NAME`,`DESCRIPTION`,`TOTLE_SCORE`,`catalog_id`,`STATE`,`create_time`,`update_time`) values (8,'发电部2021年安规考试','时长：90分钟，30道题，总分100分','100.00',1,1,'2021-02-21 00:43:27','2021-02-21 01:20:41'),(9,'2022年安规考试','没啥说的，就是为啥还带个br',NULL,1,0,'2021-02-21 01:02:34',NULL),(10,'2021年三种人考试','在线考试，7次答题机会，考试时长120分钟',NULL,3,0,'2021-03-06 14:14:38',NULL);

/*Table structure for table `exm_paper_config` */

DROP TABLE IF EXISTS `exm_paper_config`;

CREATE TABLE `exm_paper_config` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAPER_ID` int(11) NOT NULL COMMENT '试卷ID',
  `TOTLE_SCORE` double DEFAULT NULL COMMENT '总分数',
  `RADIO_COUNT` int(11) DEFAULT NULL COMMENT '单选题数',
  `RADIO_SCORE` double DEFAULT NULL COMMENT '单选每项得分',
  `CHECKBOX_COUNT` int(11) DEFAULT NULL COMMENT '多选题数',
  `CHECKBOX_SCORE` double DEFAULT NULL COMMENT '多选每项得分',
  `JUDGE_COUNT` int(11) DEFAULT NULL COMMENT '判断题数',
  `JUDGE_SCORE` double DEFAULT NULL COMMENT '判断每项得分',
  `SCANNER_COUNT` int(11) DEFAULT NULL COMMENT '填空题数',
  `SCANNER_SCORE` double DEFAULT NULL COMMENT '填空每项得分',
  `QANSWER_COUNT` int(11) DEFAULT NULL COMMENT '问答题数',
  `QANSWER_SCORE` double DEFAULT NULL COMMENT '问答每项得分',
  `UPDATE_USER_ID` int(11) DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `exm_paper_config` */

insert  into `exm_paper_config`(`ID`,`PAPER_ID`,`TOTLE_SCORE`,`RADIO_COUNT`,`RADIO_SCORE`,`CHECKBOX_COUNT`,`CHECKBOX_SCORE`,`JUDGE_COUNT`,`JUDGE_SCORE`,`SCANNER_COUNT`,`SCANNER_SCORE`,`QANSWER_COUNT`,`QANSWER_SCORE`,`UPDATE_USER_ID`,`UPDATE_TIME`) values (8,8,100,10,2,10,5,10,3,0,0,0,0,NULL,NULL);

/*Table structure for table `exm_question` */

DROP TABLE IF EXISTS `exm_question`;

CREATE TABLE `exm_question` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `question_type_id` int(11) DEFAULT NULL COMMENT '2：单选；3：多选；4：判断；5：填空；6：问答',
  `catalog_id` int(11) DEFAULT NULL COMMENT '1：安规；2：运规；3：三种人',
  `DIFFICULTY` int(11) DEFAULT NULL COMMENT '1：极易；2：简单；3：适中；4：困难；5：极难',
  `TITLE` text COMMENT '题干',
  `OPTION_A` text COMMENT '选项A',
  `OPTION_B` text COMMENT '选项B',
  `OPTION_C` text COMMENT '选项C',
  `OPTION_D` text COMMENT '选项D',
  `OPTION_E` text COMMENT '选项E',
  `OPTION_F` text COMMENT '选项F',
  `OPTION_G` text COMMENT '选项G',
  `ANSWER` text COMMENT '答案',
  `ANALYSIS` text COMMENT '解析',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `NO` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

/*Data for the table `exm_question` */

insert  into `exm_question`(`ID`,`question_type_id`,`catalog_id`,`DIFFICULTY`,`TITLE`,`OPTION_A`,`OPTION_B`,`OPTION_C`,`OPTION_D`,`OPTION_E`,`OPTION_F`,`OPTION_G`,`ANSWER`,`ANALYSIS`,`create_time`,`update_time`,`NO`) values (27,2,1,NULL,'安规单选第一题','1','2','3',NULL,NULL,NULL,NULL,'A','123',NULL,'2021-02-20 23:42:36',NULL),(28,2,1,NULL,'安规单选第二题','1','2','3','4',NULL,NULL,NULL,'B','1234',NULL,'2021-02-20 23:46:09',NULL),(29,2,1,NULL,'安规单选第三题','1','2','3',NULL,NULL,NULL,NULL,'C','1234',NULL,'2021-02-20 23:46:27',NULL),(31,2,1,NULL,'安规单选第4题','1','2',NULL,NULL,NULL,NULL,NULL,'B','12',NULL,'2021-02-20 23:47:52',NULL),(32,2,1,NULL,'安规单选第5题','1','2','3','4',NULL,NULL,NULL,'D','1234',NULL,'2021-02-20 23:48:10',NULL),(33,2,1,NULL,'安规单选第6题','1','2','3','4',NULL,NULL,NULL,'C','1234',NULL,'2021-02-20 23:48:32',NULL),(34,2,1,NULL,'安规单选第7题','1','2',NULL,NULL,NULL,NULL,NULL,'A','1234',NULL,'2021-02-20 23:48:48',NULL),(35,2,1,NULL,'安规单选第8题','12','21','123',NULL,NULL,NULL,NULL,'C','12344',NULL,'2021-02-20 23:49:05',NULL),(36,2,1,NULL,'安规单选第9题','121','211','985',NULL,NULL,NULL,NULL,'B','1234',NULL,'2021-02-20 23:49:29',NULL),(37,2,1,NULL,'安规单选第10题','21','12','11','22',NULL,NULL,NULL,'C','1231212',NULL,'2021-02-20 23:49:50',NULL),(38,3,1,NULL,'安规多选第1题','122','112','1111',NULL,NULL,NULL,NULL,'A,B','12122121',NULL,'2021-02-20 23:50:21',NULL),(39,3,1,NULL,'安规多选第2题','123','321','222','333',NULL,NULL,NULL,'C,D','1231231',NULL,'2021-02-20 23:50:44',NULL),(40,3,1,NULL,'安规多选第3题','123124','1412142','1231231','12312312','123123',NULL,NULL,'A,D,E','123123',NULL,'2021-02-20 23:51:09',NULL),(41,3,1,NULL,'安规多选第4题','111','222','333','444',NULL,NULL,NULL,'A,C,D','34234234',NULL,'2021-02-20 23:51:41',NULL),(42,3,1,NULL,'安规多选第5题','123','321','222','333',NULL,NULL,NULL,'B,C','123123',NULL,'2021-02-20 23:52:02',NULL),(43,3,1,NULL,'安规多选第6题','www','去去去','嗯嗯嗯','是是是',NULL,NULL,NULL,'C,D','亲亲我',NULL,'2021-02-20 23:52:37',NULL),(44,3,1,NULL,'安规多选第7题','123','31','32','12',NULL,NULL,NULL,'A,C,D','12312312',NULL,'2021-02-20 23:53:24',NULL),(45,3,1,NULL,'安规多选第8题','阿萨德','大神','阿萨德','是是是',NULL,NULL,NULL,'A,B,C,D','啊是打算打算多',NULL,'2021-02-20 23:53:55',NULL),(46,3,1,NULL,'安规多选第9题','二塔','热','尔特人','梵蒂冈',NULL,NULL,NULL,'B,C,D','豆腐干豆腐',NULL,'2021-02-20 23:54:26',NULL),(47,3,1,NULL,'安规多选第10题','奥术大师','是否嘎洒上','啊实打实','干粉订个蛋糕',NULL,NULL,NULL,'A,B,C,D','',NULL,'2021-02-20 23:54:59',NULL),(48,4,1,NULL,'安规判断第1题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'对','对',NULL,'2021-02-20 23:55:17',NULL),(49,4,1,NULL,'安规多判断第2题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'对','对',NULL,'2021-02-20 23:55:37',NULL),(50,4,1,NULL,'安规多判断第2题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'错','错',NULL,'2021-02-20 23:55:56',NULL),(51,4,1,NULL,'安规多判断第4题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'对','对',NULL,'2021-02-20 23:56:11',NULL),(52,4,1,NULL,'安规多判断第5题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'对','对',NULL,'2021-02-20 23:56:25',NULL),(53,4,1,NULL,'安规多判断第6题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'错','错',NULL,'2021-02-21 00:02:38',NULL),(54,4,1,NULL,'安规多判断第7题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'对','对',NULL,'2021-02-21 00:02:51',NULL),(55,4,1,NULL,'安规多判断第8题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'错','错',NULL,'2021-02-21 00:03:04',NULL),(56,4,1,NULL,'安规多判断第9题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'对','对',NULL,'2021-02-21 00:03:15',NULL),(57,4,1,NULL,'安规多判断第10题',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'错','错',NULL,'2021-02-21 00:03:31',NULL),(58,5,1,NULL,'我和我的祖国_____,<span style=\"background-color:#FFFFFF;color:#333333;font-family:Arial, sans-serif;font-size:13px;\">无论我走到哪里都流出一首赞歌。_____,______</span><span style=\"background-color:#FFFFFF;color:#333333;font-family:Arial, sans-serif;font-size:13px;\">，</span><span style=\"background-color:#FFFFFF;color:#333333;font-family:Arial, sans-serif;font-size:13px;\">袅袅炊烟小小村落路上一道辙。啦...啦...</span>',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'一刻也不能分割\n我歌唱每一座高山\n我歌唱每一条河','<p class=\"wa-musicsong-lyric-line\" style=\"text-align:left;color:#333333;font-family:Arial, sans-serif;font-size:13px;background-color:#FFFFFF;\">\r\n	我和我的祖国一刻也不能分割,<span>无论我走到哪里都流出一首赞歌,</span><span>我歌唱每一座高山 我歌唱每一条河,</span><span>袅袅炊烟小小村落路上一道辙,</span><span>啦……啦……</span>\r\n</p>\r\n<p class=\"wa-musicsong-lyric-line\" style=\"text-align:left;color:#333333;font-family:Arial, sans-serif;font-size:13px;background-color:#FFFFFF;\">\r\n	<span>你用你那母亲温情和我诉说,</span><span>我的祖国和我像海和浪花一朵,</span><span>浪是海的赤子海是那浪的依托</span><span>,每当大海在微笑我就是笑的旋涡,</span><span>我分担着海的忧愁分享着海的欢乐</span><span>.啦……啦……</span>\r\n</p>','2021-03-09 19:06:36','2021-03-09 19:06:36',NULL),(59,5,1,NULL,'试错成本太___了',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'高','<span>试错成本太高了</span>','2021-03-09 19:09:31','2021-03-09 19:09:31',NULL);

/*Table structure for table `exm_question_type` */

DROP TABLE IF EXISTS `exm_question_type`;

CREATE TABLE `exm_question_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `NAME` varchar(32) DEFAULT NULL COMMENT '名称  单选、多选、填空、判断、问答',
  `PARENT_ID` int(11) DEFAULT NULL COMMENT '父ID',
  `PARENT_SUB` varchar(512) DEFAULT NULL COMMENT '父子关系（格式：_父ID_子ID_子子ID_... ...）',
  `UPDATE_USER_ID` int(11) DEFAULT NULL COMMENT '修改人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `STATE` int(11) DEFAULT NULL COMMENT '0：删除；1：正常',
  `NO` int(11) DEFAULT NULL COMMENT '排序',
  `USER_IDS` varchar(1024) DEFAULT NULL COMMENT '用户权限。多用户用逗号分隔，如：,2,45,66,57,',
  `ORG_IDS` varchar(1024) DEFAULT NULL COMMENT '机构权限',
  `POST_IDS` varchar(1024) DEFAULT NULL COMMENT '岗位权限',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `exm_question_type` */

insert  into `exm_question_type`(`ID`,`NAME`,`PARENT_ID`,`PARENT_SUB`,`UPDATE_USER_ID`,`UPDATE_TIME`,`STATE`,`NO`,`USER_IDS`,`ORG_IDS`,`POST_IDS`) values (1,'选择试题类型',0,'_1_',1,'2017-08-01 22:31:43',1,1,NULL,NULL,NULL),(2,'单项选择题',1,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL),(3,'多项选择题',1,NULL,NULL,NULL,NULL,3,NULL,NULL,NULL),(4,'判断题',1,NULL,NULL,NULL,NULL,4,NULL,NULL,NULL),(5,'填空题',1,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL),(6,'简答题',1,NULL,NULL,NULL,NULL,6,NULL,NULL,NULL);

/*Table structure for table `sys_catalog` */

DROP TABLE IF EXISTS `sys_catalog`;

CREATE TABLE `sys_catalog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `catalog_name` varchar(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `sys_catalog` */

insert  into `sys_catalog`(`id`,`catalog_name`,`create_time`,`update_time`) values (1,'安规考试','2021-02-21 00:48:43',NULL),(2,'运规考试','2021-02-21 00:48:46',NULL),(3,'三种人考试','2021-02-21 00:48:48',NULL),(4,'检规考试','2021-02-21 00:48:51',NULL);

/*Table structure for table `sys_department` */

DROP TABLE IF EXISTS `sys_department`;

CREATE TABLE `sys_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(25) DEFAULT NULL COMMENT '部门名称',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `sys_department` */

insert  into `sys_department`(`id`,`dept_name`,`create_time`,`update_time`) values (1,'发电部',NULL,NULL),(2,'检修部',NULL,NULL),(3,'白宫',NULL,NULL),(4,'燃储部',NULL,NULL),(5,'人资部',NULL,NULL),(6,'生技部',NULL,NULL),(7,'安生部',NULL,NULL);

/*Table structure for table `sys_file` */

DROP TABLE IF EXISTS `sys_file`;

CREATE TABLE `sys_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(32) DEFAULT NULL COMMENT '前缀',
  `EXT_NAME` varchar(32) DEFAULT NULL COMMENT '后缀',
  `FILE_TYPE` varchar(128) DEFAULT NULL COMMENT '类型',
  `PATH` varchar(64) DEFAULT NULL COMMENT '路径',
  `IP` varchar(16) DEFAULT NULL COMMENT '上传ip',
  `STATE` int(11) DEFAULT NULL COMMENT '0：删除；1：正常',
  `UPDATE_USER_ID` int(11) DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_file` */

/*Table structure for table `sys_quartz_task` */

DROP TABLE IF EXISTS `sys_quartz_task`;

CREATE TABLE `sys_quartz_task` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '名称',
  `JOB_CLASS` varchar(50) DEFAULT NULL COMMENT '实现类',
  `CRON` varchar(50) DEFAULT NULL COMMENT 'cron表达式',
  `jobDetail_name` varchar(50) DEFAULT NULL COMMENT '创建的JobDetail名称',
  `cronTrigger_name` varchar(50) DEFAULT NULL COMMENT '创建的CronTriggrer名称',
  `STATE` int(11) DEFAULT NULL COMMENT '1：启动；2：停止；3：任务取消',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8 COMMENT='定时任务';

/*Data for the table `sys_quartz_task` */

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login_name` varchar(20) DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `real_name` varchar(10) DEFAULT NULL COMMENT '真实姓名',
  `work_num` int(4) DEFAULT NULL COMMENT '工号',
  `department_id` int(2) DEFAULT NULL COMMENT '部门ID',
  `department_name` varchar(10) DEFAULT NULL COMMENT '冗余字段：部门名称',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录',
  `avatar_url` varchar(100) DEFAULT NULL,
  `phone_num` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_Reference_3` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

/*Data for the table `sys_user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
