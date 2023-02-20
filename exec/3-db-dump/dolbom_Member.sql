-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: i8c103.p.ssafy.io    Database: dolbom
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Member`
--

DROP TABLE IF EXISTS `Member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_at` datetime(6) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `content` text,
  `email` varchar(45) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9qv6yhjqm8iafto8qk452gx8h` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Member`
--

LOCK TABLES `Member` WRITE;
/*!40000 ALTER TABLE `Member` DISABLE KEYS */;
INSERT INTO `Member` VALUES (1,NULL,NULL,'1996-01-11','','rth634@naver.com','박현우','$2a$10$8bQDS4gW/bpCO79Jyxvar.EnaWUfThYA78j5x3BXAC5DhY8vT3Upa','01040293865','COUNSELOR'),(3,NULL,NULL,NULL,NULL,'email@email.com','이충무','cssxukalvyhhjfggpxewitweuurespnujckbvbgegxzoh','01040293930','CLIENT'),(4,NULL,NULL,NULL,NULL,'ssss@naver.com','장원영','kcwhjynrtkqmdftlremffpdllhehxgwnygpkhpqtjqmvp','01012341234','CLIENT'),(5,NULL,NULL,NULL,NULL,'erg@naver.com','박종수','ilfgvwecedaijtfregasbdmgqnynrdvxmoszmcirsafsg','01029384938','CLIENT'),(6,NULL,NULL,NULL,NULL,'fsaji@naver.com','김철민','lyakbktyubffnpyoenhyktrozmghaugtiovehycrzriag','01012341243','CLIENT'),(7,NULL,NULL,'1996-12-04','ssafy8기 입니다!','ssafy@ssafy','ssafy','$2a$10$b.o7WLzGsbZ/0vbUcN2Fx.DNN2ZPX7hGrAIC0Gziq0CSUx7GL8/Ky','01032321212','COUNSELOR'),(8,NULL,NULL,NULL,NULL,'youngman@naver.com','김영한','ozkbfatetkkhzxnfmzgtkpilszmqlijqlwvhctrjgitrm','01038592459','CLIENT'),(9,'2023-02-16 13:59:01.099457','2023-02-16 13:59:01.099457',NULL,NULL,'cndan2768@naver.com','박승빈','juuotnrcyuaneilggraqotoaohnggjjftpuzbocbnsqrp','01071897902','CLIENT'),(10,'2023-02-16 14:14:00.511777','2023-02-16 14:14:00.511777',NULL,NULL,'carina@naver.com','카리나','xxagxiukobkxmcblbytlpjjdqentelvztoncfhgrcgpdh','01012341234','CLIENT'),(11,'2023-02-16 14:18:55.069229','2023-02-16 14:18:55.069229',NULL,NULL,'gold@giryun.com','금기륜','rgustdpdcgyeeottpvianzlgvmxuiuposglndvfyiuqpo','01059383940','CLIENT'),(12,'2023-02-16 14:21:12.851861','2023-02-16 14:21:12.851861',NULL,NULL,'big@jongsu.com','빅종수','wvougwvamezntaxktvsuyelypxpwvbnchbwrjlrmnwtnq','01099999999','CLIENT'),(13,'2023-02-16 14:24:59.555657','2023-02-16 14:24:59.555657',NULL,NULL,'dolbomcommon@naver.com','리재훈','tchtsppcpfnrshsouwxuazhndivqvbyhjjvaaevobkxlu','01071897902','CLIENT'),(14,'2023-02-16 14:35:18.595144','2023-02-16 14:35:18.595144','1996-12-27','','miunae@naver.com','이재훈','$2a$10$h7uKwXyzjiq7pnyXmJHJ1Ox8e4kaaq6VMsiEzTpCI7kLe48za0FCK','01023384248','COUNSELOR'),(15,'2023-02-16 14:36:59.310370','2023-02-16 14:36:59.310370',NULL,NULL,'bins95@naver.com','박승빈','tlbameduvhhxtpbvqpnistztofxtteedekvuyckzcszgr','01012341234','CLIENT'),(16,'2023-02-16 15:44:51.411037','2023-02-16 15:44:51.411037',NULL,NULL,'fsdaji@naver.com','리춘무','vppsvmmnmtczucqythkuseyggeypyquyntjapbwseokve','0102345822','CLIENT'),(17,'2023-02-16 17:47:48.787756','2023-02-16 17:47:48.787756',NULL,NULL,'small@jongsu.com','스몰종수','ebgufniggvjlfpisgowfbezaoccfesvhkiocixaufcxiq','01040293877','CLIENT'),(18,'2023-02-16 17:51:40.325398','2023-02-16 17:51:40.325398',NULL,NULL,'middle@jongsu.com','미들종수','ntesopwghcwwftmqjnsrymrjgowlflckutsyngumwdqsj','01049283763','CLIENT'),(20,'2023-02-16 19:34:10.053803','2023-02-16 19:34:10.053803',NULL,NULL,'','','oyfotmbmvcrkqqkohqjtbidughqxldpuhyqkumwvvlphm','','CLIENT'),(22,'2023-02-16 19:35:08.263418','2023-02-16 19:35:08.263418',NULL,NULL,'miunae123@gmail.com','테스트','ofdrrwdnrqpjvejmhpmjopnzwdjqsgqqumcbwojdnnvor','01011112222','CLIENT'),(23,'2023-02-16 19:45:47.325597','2023-02-16 19:45:47.325597','1995-11-11','','test11@naver.com','박승비','$2a$10$PNa66VZuDFLfZVqOtp5lxu4O7BZroSRImVFsn/sp0PCM3EoUvl2m6','01033972595','COUNSELOR');
/*!40000 ALTER TABLE `Member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-16 21:17:33
