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
-- Table structure for table `conference_history`
--

DROP TABLE IF EXISTS `conference_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conference_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `endedTime` datetime(6) DEFAULT NULL,
  `insertedTime` datetime(6) DEFAULT NULL,
  `memoOriginName` varchar(45) DEFAULT NULL,
  `memoPath` text,
  `memoSavedName` varchar(45) DEFAULT NULL,
  `sttOriginName` varchar(45) DEFAULT NULL,
  `sttPath` text,
  `sttSavedName` varchar(45) DEFAULT NULL,
  `client_id` bigint DEFAULT NULL,
  `conference_id` bigint DEFAULT NULL,
  `counselor_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5mohtygd96evafsyfhexddufv` (`client_id`),
  KEY `FKssbbjsjtm53iy43b5vwxoxrlc` (`conference_id`),
  KEY `FKaxl9kgmi9w60lp5tqsmwb9119` (`counselor_id`),
  CONSTRAINT `FK5mohtygd96evafsyfhexddufv` FOREIGN KEY (`client_id`) REFERENCES `Member` (`id`),
  CONSTRAINT `FKaxl9kgmi9w60lp5tqsmwb9119` FOREIGN KEY (`counselor_id`) REFERENCES `Member` (`id`),
  CONSTRAINT `FKssbbjsjtm53iy43b5vwxoxrlc` FOREIGN KEY (`conference_id`) REFERENCES `Conference` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conference_history`
--

LOCK TABLES `conference_history` WRITE;
/*!40000 ALTER TABLE `conference_history` DISABLE KEYS */;
INSERT INTO `conference_history` VALUES (1,NULL,'2023-02-16 20:05:55.053844',NULL,NULL,NULL,NULL,NULL,NULL,22,24,14),(2,NULL,'2023-02-16 20:15:34.624114',NULL,NULL,NULL,NULL,NULL,NULL,15,31,14),(3,NULL,'2023-02-16 20:47:27.476334',NULL,NULL,NULL,NULL,NULL,NULL,22,41,14),(4,NULL,'2023-02-16 20:59:22.109125',NULL,NULL,NULL,NULL,NULL,NULL,22,42,14);
/*!40000 ALTER TABLE `conference_history` ENABLE KEYS */;
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
