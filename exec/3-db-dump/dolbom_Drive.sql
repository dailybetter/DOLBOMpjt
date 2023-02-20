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
-- Table structure for table `Drive`
--

DROP TABLE IF EXISTS `Drive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Drive` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `originName` varchar(45) DEFAULT NULL,
  `path` text,
  `savedName` varchar(45) DEFAULT NULL,
  `member_client_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpbel2fvy3bkhq4agphxst37kn` (`member_client_id`),
  CONSTRAINT `FKpbel2fvy3bkhq4agphxst37kn` FOREIGN KEY (`member_client_id`) REFERENCES `member_client` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Drive`
--

LOCK TABLES `Drive` WRITE;
/*!40000 ALTER TABLE `Drive` DISABLE KEYS */;
INSERT INTO `Drive` VALUES (1,'test.txt','drive/3/bfa253d8-e774-4ab6-920e-bf3e4ae3c20b.txt','bfa253d8-e774-4ab6-920e-bf3e4ae3c20b.txt',3),(3,'이충무.txt','drive/10/김선규/fa318a34-6ec4-4376-a698-094cce839060.txt','fa318a34-6ec4-4376-a698-094cce839060.txt',10),(4,'다운로드.jpg','drive/10/김선규/527331d1-1b7d-40b4-9b01-30f2adef479c.jpg','527331d1-1b7d-40b4-9b01-30f2adef479c.jpg',10),(5,'test.txt','drive/5/d1ac9258-3ae3-4372-b700-0f2d56aaf45e.txt','d1ac9258-3ae3-4372-b700-0f2d56aaf45e.txt',5),(6,'test.txt','drive/5/l/dccba235-b7c0-488e-bbd2-cc94229944d2.txt','dccba235-b7c0-488e-bbd2-cc94229944d2.txt',5),(7,'test.txt','drive/3/3073b3a0-62c6-4721-93bc-420b040601ab.txt','3073b3a0-62c6-4721-93bc-420b040601ab.txt',3),(8,'test.txt','drive/3/c6b0a47e-db88-4db0-9241-56bbe5c1abc1.txt','c6b0a47e-db88-4db0-9241-56bbe5c1abc1.txt',3);
/*!40000 ALTER TABLE `Drive` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-16 21:17:34
