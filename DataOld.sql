CREATE DATABASE  IF NOT EXISTS `chat` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `chat`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: chat
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `amis`
--

DROP TABLE IF EXISTS `amis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amis` (
  `uuid_first_user` varchar(200) NOT NULL,
  `uuid_second_user` varchar(200) NOT NULL,
  PRIMARY KEY (`uuid_first_user`,`uuid_second_user`),
  KEY `fk_uuid_second_user_AMIS` (`uuid_second_user`),
  CONSTRAINT `fk_uuid_first_user_AMIS` FOREIGN KEY (`uuid_first_user`) REFERENCES `user` (`uuid_user`),
  CONSTRAINT `fk_uuid_second_user_AMIS` FOREIGN KEY (`uuid_second_user`) REFERENCES `user` (`uuid_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amis`
--

LOCK TABLES `amis` WRITE;
/*!40000 ALTER TABLE `amis` DISABLE KEYS */;
/*!40000 ALTER TABLE `amis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `connected`
--

DROP TABLE IF EXISTS `connected`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `connected` (
  `uuid_user` varchar(200) NOT NULL,
  `uuid_room` varchar(200) NOT NULL,
  PRIMARY KEY (`uuid_user`,`uuid_room`),
  KEY `fk_uuid_room_connected` (`uuid_room`),
  CONSTRAINT `fk_uuid_room_connected` FOREIGN KEY (`uuid_room`) REFERENCES `room` (`uuid_room`),
  CONSTRAINT `fk_uuid_user_connected` FOREIGN KEY (`uuid_user`) REFERENCES `user` (`uuid_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `connected`
--

LOCK TABLES `connected` WRITE;
/*!40000 ALTER TABLE `connected` DISABLE KEYS */;
/*!40000 ALTER TABLE `connected` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messageroom`
--

DROP TABLE IF EXISTS `messageroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messageroom` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `uuid_room` varchar(200) NOT NULL,
  `uuid_user` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `message_date` date NOT NULL DEFAULT (curdate()),
  `isDelated` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`message_id`),
  KEY `fk_uuid_room_messageroom` (`uuid_room`),
  KEY `fk_uuid_user_messageroom` (`uuid_user`),
  CONSTRAINT `fk_uuid_room_messageroom` FOREIGN KEY (`uuid_room`) REFERENCES `room` (`uuid_room`),
  CONSTRAINT `fk_uuid_user_messageroom` FOREIGN KEY (`uuid_user`) REFERENCES `user` (`uuid_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messageroom`
--

LOCK TABLES `messageroom` WRITE;
/*!40000 ALTER TABLE `messageroom` DISABLE KEYS */;
/*!40000 ALTER TABLE `messageroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messageto`
--

DROP TABLE IF EXISTS `messageto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messageto` (
  `uuid_reciver` varchar(200) NOT NULL,
  `uuid_sender` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `message_date` date NOT NULL DEFAULT (curdate()),
  `isDelated` tinyint(1) NOT NULL DEFAULT '0',
  KEY `fk_uuid_reciver` (`uuid_reciver`),
  KEY `fk_uuid_sender` (`uuid_sender`),
  CONSTRAINT `fk_uuid_reciver` FOREIGN KEY (`uuid_reciver`) REFERENCES `user` (`uuid_user`),
  CONSTRAINT `fk_uuid_sender` FOREIGN KEY (`uuid_sender`) REFERENCES `user` (`uuid_user`),
  CONSTRAINT `check_unique` CHECK ((`uuid_sender` <> `uuid_reciver`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messageto`
--

LOCK TABLES `messageto` WRITE;
/*!40000 ALTER TABLE `messageto` DISABLE KEYS */;
/*!40000 ALTER TABLE `messageto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `uuid_room` varchar(200) NOT NULL,
  `roomname` varchar(50) NOT NULL,
  `image` varchar(100) NOT NULL,
  PRIMARY KEY (`uuid_room`),
  UNIQUE KEY `roomname` (`roomname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `uuid_user` varchar(200) NOT NULL,
  `email` varchar(100) NOT NULL,
  `image` varchar(100) NOT NULL,
  `username` varchar(60) NOT NULL,
  `hashpasword` varchar(100) NOT NULL,
  `isadmin` tinyint(1) NOT NULL,
  PRIMARY KEY (`uuid_user`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-17 13:29:31
