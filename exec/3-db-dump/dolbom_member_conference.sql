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
-- Table structure for table `member_conference`
--

DROP TABLE IF EXISTS `member_conference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_conference` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conference_id` bigint DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5mdtk5hj3mygxa8j5b5vmph5o` (`conference_id`),
  KEY `FKef6hvxfv2c9cy88kf6pw9dqlv` (`member_id`),
  CONSTRAINT `FK5mdtk5hj3mygxa8j5b5vmph5o` FOREIGN KEY (`conference_id`) REFERENCES `Conference` (`id`),
  CONSTRAINT `FKef6hvxfv2c9cy88kf6pw9dqlv` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_conference`
--

LOCK TABLES `member_conference` WRITE;
/*!40000 ALTER TABLE `member_conference` DISABLE KEYS */;
INSERT INTO `member_conference` VALUES (1,1,7),(2,2,14),(3,3,14),(4,4,7),(5,5,14),(6,6,14),(7,7,14),(8,8,14),(9,9,14),(10,10,14),(11,11,14),(12,12,14),(13,13,14),(14,14,14),(15,15,14),(16,16,14),(17,17,14),(18,18,14),(19,19,14),(20,20,14),(21,21,14),(22,22,14),(23,23,14),(24,24,14),(25,25,14),(26,26,14),(27,27,14),(28,28,7),(29,29,14),(30,30,14),(31,24,22),(32,31,14),(33,31,15),(34,32,7),(35,33,7),(36,34,14),(37,35,14),(38,36,14),(39,37,7),(40,38,14),(41,39,7),(42,40,14),(43,41,14),(44,41,22),(45,42,14),(46,42,22),(47,43,7);
/*!40000 ALTER TABLE `member_conference` ENABLE KEYS */;
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
