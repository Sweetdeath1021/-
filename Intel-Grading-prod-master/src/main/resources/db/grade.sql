-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: intel_grading
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `grd_answer_sheet`
--

DROP TABLE IF EXISTS `grd_answer_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_answer_sheet` (
  `sheet_id` varchar(50) NOT NULL,
  `paper_id` varchar(50) DEFAULT NULL,
  `student_number` varchar(50) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`sheet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_answer_sheet`
--

LOCK TABLES `grd_answer_sheet` WRITE;
/*!40000 ALTER TABLE `grd_answer_sheet` DISABLE KEYS */;
INSERT INTO `grd_answer_sheet` VALUES ('1769287197103554562','1769287181140033537','7030223005','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/answersheet/西部第一高级中学/高三1班/27bbf73d-c066-419b-b3c6-6293e0f78b7e.png','2024-03-17 16:58:34',0),('1769287204766547970','1769287181140033537','7030223001','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/answersheet/西部第一高级中学/高三1班/79f57844-63e9-4219-af87-2150aa6f4726.png','2024-03-17 16:58:36',0),('1769287209883598849','1769287181140033537','7030223003','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/answersheet/西部第一高级中学/高三1班/eabb82b9-3388-49a3-b04d-2afa9973f13c.png','2024-03-17 16:58:37',0),('1769287215193587714','1769287181140033537','7030223006','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/answersheet/西部第一高级中学/高三1班/0d581cc5-b7c7-4512-97ba-33c4df5cb38e.png','2024-03-17 16:58:39',0),('1769287221208219649','1769287181140033537','7030223004','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/answersheet/西部第一高级中学/高三1班/7afe9f13-3b9b-451f-ac37-50c9d34f792c.png','2024-03-17 16:58:40',0),('1769287226845364226','1769287181140033537','7030223002','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/answersheet/西部第一高级中学/高三1班/fb1571bc-6f00-42ed-9b3d-e926273b038f.png','2024-03-17 16:58:41',0);
/*!40000 ALTER TABLE `grd_answer_sheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_content`
--

DROP TABLE IF EXISTS `grd_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_content` (
  `content_id` varchar(50) NOT NULL,
  `paper_id` varchar(50) DEFAULT NULL,
  `content_score` int DEFAULT NULL,
  `content_advice` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_content`
--

LOCK TABLES `grd_content` WRITE;
/*!40000 ALTER TABLE `grd_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `grd_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_group`
--

DROP TABLE IF EXISTS `grd_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_group` (
  `group_id` varchar(50) NOT NULL,
  `paper_id` varchar(50) NOT NULL,
  `group_name` varchar(100) DEFAULT NULL,
  `group_request` varchar(1000) DEFAULT NULL,
  `group_total_num` int DEFAULT NULL,
  `group_not_num` int DEFAULT NULL,
  `group_create_time` datetime DEFAULT NULL,
  `group_end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_group`
--

LOCK TABLES `grd_group` WRITE;
/*!40000 ALTER TABLE `grd_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `grd_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_make`
--

DROP TABLE IF EXISTS `grd_make`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_make` (
  `make_id` varchar(50) NOT NULL,
  `paper_id` varchar(50) DEFAULT NULL,
  `task_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`make_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_make`
--

LOCK TABLES `grd_make` WRITE;
/*!40000 ALTER TABLE `grd_make` DISABLE KEYS */;
/*!40000 ALTER TABLE `grd_make` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_menu`
--

DROP TABLE IF EXISTS `grd_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_menu` (
  `menu_id` varchar(50) NOT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_perms` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_menu`
--

LOCK TABLES `grd_menu` WRITE;
/*!40000 ALTER TABLE `grd_menu` DISABLE KEYS */;
INSERT INTO `grd_menu` VALUES ('1','任务分配','sys/grade/task'),('2','测试','sys/grade/test');
/*!40000 ALTER TABLE `grd_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_paper_image`
--

DROP TABLE IF EXISTS `grd_paper_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_paper_image` (
  `paper_image_id` varchar(50) NOT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `paper_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`paper_image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_paper_image`
--

LOCK TABLES `grd_paper_image` WRITE;
/*!40000 ALTER TABLE `grd_paper_image` DISABLE KEYS */;
INSERT INTO `grd_paper_image` VALUES ('1769287184017326082','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/testpaper/西部第一高级中学/da305725-7a98-4563-b432-8c12351442fe.png','1769287181140033537');
/*!40000 ALTER TABLE `grd_paper_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_region`
--

DROP TABLE IF EXISTS `grd_region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_region` (
  `region_id` varchar(50) NOT NULL,
  `sheet_id` varchar(50) DEFAULT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `region_number` int DEFAULT NULL,
  `region_score` int DEFAULT NULL,
  `is_graded` tinyint(1) DEFAULT '0',
  `region_image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_region`
--

LOCK TABLES `grd_region` WRITE;
/*!40000 ALTER TABLE `grd_region` DISABLE KEYS */;
INSERT INTO `grd_region` VALUES ('1769287608547999745','1769287197103554562',NULL,1,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223005/31b519ab-9e48-4323-9c4f-8b24807f6ee0.png'),('1769287612016689153','1769287197103554562','1765738364620836865',2,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223005/0d354575-69f1-400b-aeb0-4e63445684a6.png'),('1769287615548293121','1769287197103554562',NULL,3,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223005/787a9f9e-3340-4c92-8e4d-394251492528.png'),('1769287619180560386','1769287197103554562',NULL,4,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223005/955a3b95-9d4e-4dec-9ffe-5a13e01296de.png'),('1769287622477283330','1769287197103554562',NULL,5,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223005/c39f9dae-a9a9-4662-bb9e-959be1f982f6.png'),('1769287626390568962','1769287204766547970',NULL,1,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223001/1457df94-b6f3-4c75-b3f4-037390601036.png'),('1769287629607600130','1769287204766547970','1765738364620836865',2,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223001/785efecd-b5cf-4536-aef9-2281fee43917.png'),('1769287632753328130','1769287204766547970',NULL,3,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223001/6e5eb5a3-d67e-4432-a51a-9bc52495e295.png'),('1769287635974553601','1769287204766547970',NULL,4,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223001/8cad587a-1cd6-4840-8990-14c21b6e224c.png'),('1769287639346774018','1769287204766547970',NULL,5,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223001/ed375efc-3814-4a0d-a859-0197c7180d24.png'),('1769287642945486849','1769287209883598849',NULL,1,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223003/05e79879-f5b4-438a-a55e-273aae1c8857.png'),('1769287646292541442','1769287209883598849','1765738364620836865',2,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223003/973d876b-d5b0-4ddf-9d49-57213a33ec4c.png'),('1769287649580875777','1769287209883598849',NULL,3,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223003/e02bd65b-cab7-4067-837f-df573a40e353.png'),('1769287652802101250','1769287209883598849',NULL,4,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223003/a9b7644c-e411-45fc-9d4f-3f34233ce53a.png'),('1769287656077852673','1769287209883598849',NULL,5,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223003/c143c4b6-e11d-498c-b282-bd3997ad606e.png'),('1769287659827560449','1769287215193587714',NULL,1,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223006/c98feb3b-5ab4-47f1-a2cc-b4653bd971ec.png'),('1769287663107506177','1769287215193587714','1765738364620836865',2,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223006/fbe5aefa-cb3d-4cb6-91d3-8cf8f2493f2a.png'),('1769287666383257602','1769287215193587714',NULL,3,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223006/cd348616-c51b-4e39-a811-1c8538109f56.png'),('1769287669533179905','1769287215193587714',NULL,4,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223006/d73e6fab-1ec2-49cb-8c4a-0a5ff83dd14a.png'),('1769287672750211073','1769287215193587714',NULL,5,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223006/01af1818-4031-4d90-8eba-730ad625f2e0.png'),('1769287676336340993','1769287221208219649',NULL,1,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223004/03c6b005-426b-468d-adc4-14c23a842a90.png'),('1769287679700172801','1769287221208219649','1764630211879501826',2,1,1,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223004/c7e063c1-37b3-4572-a377-d9ebf602f7da.png'),('1769287683055616001','1769287221208219649',NULL,3,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223004/2a9cbbf9-fed5-4614-99f1-2b3505909f1e.png'),('1769287686146818050','1769287221208219649',NULL,4,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223004/f5100c9b-28a5-40aa-9cb3-bf48bbd7388d.png'),('1769287689359654913','1769287221208219649',NULL,5,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223004/2887eb7f-c700-4354-b0e8-554444ad74d2.png'),('1769287692824150017','1769287226845364226',NULL,1,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223002/1f38a6f4-d1ad-4614-90aa-8127f2ed547c.png'),('1769287695906963457','1769287226845364226','1764630211879501826',2,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223002/1bfc47d6-437e-425d-98da-8d997a706678.png'),('1769287698872336386','1769287226845364226',NULL,3,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223002/5ccccb1b-c757-49df-b189-52c587c8ddfa.png'),('1769287702064201730','1769287226845364226',NULL,4,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223002/6988f86b-6449-4abb-84dc-dfddc9dd928a.png'),('1769287705855852546','1769287226845364226',NULL,5,0,0,'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/region/西部第一高级中学/思想政治/7030223002/13eabb4b-f79d-4483-a975-2c4582f9b64e.png');
/*!40000 ALTER TABLE `grd_region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_role`
--

DROP TABLE IF EXISTS `grd_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_role` (
  `role_id` varchar(50) NOT NULL,
  `role_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_role`
--

LOCK TABLES `grd_role` WRITE;
/*!40000 ALTER TABLE `grd_role` DISABLE KEYS */;
INSERT INTO `grd_role` VALUES ('1','管理员'),('2','老师'),('3','学生');
/*!40000 ALTER TABLE `grd_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_role_menu`
--

DROP TABLE IF EXISTS `grd_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_role_menu` (
  `role_id` varchar(50) NOT NULL,
  `menu_id` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_role_menu`
--

LOCK TABLES `grd_role_menu` WRITE;
/*!40000 ALTER TABLE `grd_role_menu` DISABLE KEYS */;
INSERT INTO `grd_role_menu` VALUES ('1','1'),('1','2'),('2','2');
/*!40000 ALTER TABLE `grd_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_student`
--

DROP TABLE IF EXISTS `grd_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_student` (
  `user_id` varchar(50) NOT NULL,
  `student_number` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_student`
--

LOCK TABLES `grd_student` WRITE;
/*!40000 ALTER TABLE `grd_student` DISABLE KEYS */;
INSERT INTO `grd_student` VALUES ('1767195500102041602','7030220610'),('1772616099582476289','7030220610'),('1772617215816884226','7030220610');
/*!40000 ALTER TABLE `grd_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_task`
--

DROP TABLE IF EXISTS `grd_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_task` (
  `task_id` varchar(50) NOT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `paper_id` varchar(50) DEFAULT NULL,
  `region_number` int DEFAULT NULL,
  `task_name` varchar(50) DEFAULT NULL,
  `task_num` int DEFAULT NULL,
  `completed_num` int DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_task`
--

LOCK TABLES `grd_task` WRITE;
/*!40000 ALTER TABLE `grd_task` DISABLE KEYS */;
INSERT INTO `grd_task` VALUES ('1771560359082106881','1765738364620836865','1769287181140033537',2,'高三第二次大联考阅卷任务',4,0,'2024-03-08 23:23:18'),('1771560359224713217','1764630211879501826','1769287181140033537',2,'高三第二次大联考阅卷任务',2,0,'2024-03-08 23:23:18');
/*!40000 ALTER TABLE `grd_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_teacher`
--

DROP TABLE IF EXISTS `grd_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_teacher` (
  `user_id` varchar(50) NOT NULL,
  `teacher_class` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_teacher`
--

LOCK TABLES `grd_teacher` WRITE;
/*!40000 ALTER TABLE `grd_teacher` DISABLE KEYS */;
INSERT INTO `grd_teacher` VALUES ('1764630211879501826','[\"class1\",\"class2\",\"class3\"]','[\"语文\",\"思想政治\"]'),('1765738364620836865','[\"class1\",\"class2\"]','[\"思想政治\"]');
/*!40000 ALTER TABLE `grd_teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_template`
--

DROP TABLE IF EXISTS `grd_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_template` (
  `tmp_id` varchar(50) NOT NULL,
  `paper_id` varchar(50) DEFAULT NULL,
  `tmp_image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`tmp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_template`
--

LOCK TABLES `grd_template` WRITE;
/*!40000 ALTER TABLE `grd_template` DISABLE KEYS */;
INSERT INTO `grd_template` VALUES ('1769287626893885442','1769287181140033537','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/template/1769287181140033537/a06c8c2f-7ed5-4e5c-8680-d7aa189b41c5.png');
/*!40000 ALTER TABLE `grd_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_test_paper`
--

DROP TABLE IF EXISTS `grd_test_paper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_test_paper` (
  `paper_id` varchar(50) NOT NULL,
  `school_name` varchar(50) NOT NULL,
  `paper_name` varchar(50) DEFAULT NULL,
  `paper_subject` varchar(50) DEFAULT NULL,
  `sheet_num` int DEFAULT NULL,
  `completed_num` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `paper_status` int DEFAULT NULL,
  `sheet_status` int DEFAULT NULL,
  PRIMARY KEY (`paper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_test_paper`
--

LOCK TABLES `grd_test_paper` WRITE;
/*!40000 ALTER TABLE `grd_test_paper` DISABLE KEYS */;
INSERT INTO `grd_test_paper` VALUES ('1769287181140033537','西部第一高级中学','第一次联考','思想政治',6,0,'2024-03-17 16:58:30',1,1);
/*!40000 ALTER TABLE `grd_test_paper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_user`
--

DROP TABLE IF EXISTS `grd_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_user` (
  `user_id` varchar(50) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `user_sex` int DEFAULT NULL,
  `user_phone` varchar(50) DEFAULT NULL,
  `user_password` varchar(500) DEFAULT NULL,
  `user_email` varchar(100) DEFAULT NULL,
  `user_school` varchar(50) DEFAULT NULL,
  `user_image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_user`
--

LOCK TABLES `grd_user` WRITE;
/*!40000 ALTER TABLE `grd_user` DISABLE KEYS */;
INSERT INTO `grd_user` VALUES ('1757698577720782850','亚瑟·摩根',1,'13017131175','$2a$10$1iOdieBwv4zXl0txSfR0JezUtIzEijSRjw3qnGwTQlPtFCSTSAHYy','2484792357@qq.com','瓦伦丁大学','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/a0359955-62c2-4827-9dc2-3171d59e99f1.webp'),('1757698734822633473','何西阿·马修斯',1,'17613130876','$2a$10$jQcpoTnq9ShAQmcVyE2p/e2oXiLeEu5jo55PkeJT9PoEq.A69llea','modoxlixin@outlook.com','西部第一高级中学','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/ed063c00-0bbb-4c01-9fdb-497e89db7288.png'),('1757757450439675905','老杨',1,'19373767717','$2a$10$w5T4669V7quYQrva.GRrlOaFkDXWjcYvfQdr6D01P9.iZM1rx2XaK','2601834442@qq.com','西部第一高级中学','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/0189d3c5-cf76-46d5-b470-e409e46940c5.png'),('1764630211879501826','达奇·范德林德',1,'15194452167','$2a$10$daMkW.tBeRXgZ18HeiyeRelTtwxe.AAwIIgaWhdDsxv4WTzzrv.BS','modoxlixin@outlook.com','西部第一高级中学','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/f742ea8e-26e0-4602-9b96-e62ab917aa56.png'),('1765738364620836865','苏珊·格里姆肖',0,'09876543211','$2a$10$OvI2M7lIut9auKStbpXuKe6vafAlUtUix5roDd2Iiz5xfmJpB1Qmi','modoxlixin@outlook.com','西部第一高级中学','https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/f216725e-5304-45e9-a14b-2bc44b9afea3.png');
/*!40000 ALTER TABLE `grd_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_user_role`
--

DROP TABLE IF EXISTS `grd_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_user_role` (
  `user_id` varchar(50) NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_user_role`
--

LOCK TABLES `grd_user_role` WRITE;
/*!40000 ALTER TABLE `grd_user_role` DISABLE KEYS */;
INSERT INTO `grd_user_role` VALUES ('1757698577720782850',1),('1757698734822633473',1),('1757757450439675905',1),('1764630211879501826',2),('1765738364620836865',2);
/*!40000 ALTER TABLE `grd_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grd_work`
--

DROP TABLE IF EXISTS `grd_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grd_work` (
  `work_id` varchar(50) NOT NULL,
  `teacher_id` varchar(50) DEFAULT NULL,
  `task_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grd_work`
--

LOCK TABLES `grd_work` WRITE;
/*!40000 ALTER TABLE `grd_work` DISABLE KEYS */;
/*!40000 ALTER TABLE `grd_work` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-28 16:32:47
