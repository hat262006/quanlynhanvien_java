CREATE DATABASE  IF NOT EXISTS `db_qlnv` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_qlnv`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_qlnv
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `bangluong`
--

DROP TABLE IF EXISTS `bangluong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bangluong` (
  `ma_nhan_vien` varchar(10) NOT NULL,
  `ten_nv` varchar(50) DEFAULT NULL,
  `chuc_vu` varchar(30) DEFAULT NULL,
  `gio_Lam` double DEFAULT NULL,
  `tang_ca` double DEFAULT NULL,
  `hs_tang_ca` double DEFAULT NULL,
  `tong_luong` double DEFAULT NULL,
  PRIMARY KEY (`ma_nhan_vien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bangluong`
--

LOCK TABLES `bangluong` WRITE;
/*!40000 ALTER TABLE `bangluong` DISABLE KEYS */;
INSERT INTO `bangluong` VALUES ('n011','Linh Tây','Công nhân may',1,1,1,2),('n012','Nguyễn Thị Nhi','Nhân viên văn phòng',2,2,1,0.2);
/*!40000 ALTER TABLE `bangluong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cham_cong`
--

DROP TABLE IF EXISTS `cham_cong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cham_cong` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ma_nhan_vien` varchar(20) DEFAULT NULL,
  `ngay` date DEFAULT NULL,
  `gio_lam` float DEFAULT NULL,
  `gio_tang_ca` float DEFAULT NULL,
  `hs_tang_ca` float DEFAULT NULL,
  `trang_thai` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cham_cong_ibfk_1` (`ma_nhan_vien`),
  CONSTRAINT `cham_cong_ibfk_1` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_su` (`ma_nhan_vien`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cham_cong`
--

LOCK TABLES `cham_cong` WRITE;
/*!40000 ALTER TABLE `cham_cong` DISABLE KEYS */;
INSERT INTO `cham_cong` VALUES (39,'n012','2025-01-01',1,1,1,'Đi làm'),(40,'n012','2025-01-02',1,1,1,'Đi làm'),(42,'n011','2025-01-01',1,1,1,'Đi làm');
/*!40000 ALTER TABLE `cham_cong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhan_su`
--

DROP TABLE IF EXISTS `nhan_su`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhan_su` (
  `ma_nhan_vien` varchar(20) NOT NULL,
  `ho_ten` varchar(100) NOT NULL,
  `gioi_tinh` varchar(10) DEFAULT NULL,
  `chuc_vu` varchar(50) DEFAULT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `cccd` varchar(20) DEFAULT NULL,
  `ngay_vao_lam` date DEFAULT NULL,
  `trinh_do` varchar(50) DEFAULT NULL,
  `so_dien_thoai` varchar(15) DEFAULT NULL,
  `trang_thai` varchar(20) DEFAULT NULL,
  `anh` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ma_nhan_vien`),
  UNIQUE KEY `cccd` (`cccd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhan_su`
--

LOCK TABLES `nhan_su` WRITE;
/*!40000 ALTER TABLE `nhan_su` DISABLE KEYS */;
INSERT INTO `nhan_su` VALUES ('n011','Linh Tây','Nữ','Công nhân may','1970-01-01','098765432123','2007-01-01','không có','0123456789','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh2.jpeg'),('n012','Nguyễn Thị Nhi','Nam','Nhân viên văn phòng','2008-01-01','012347895676','2025-01-01','kỹ sư','0987654321','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh1.jpg'),('n013','Lan Xinh Yêu','Nam','Nhân viên văn phòng','1970-01-01','094563738123','2007-01-01','thạc sỹ','0126453782','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh3.jpg'),('n015','Liễu Như Yên','Nữ','Nhân viên văn phòng','1976-01-01','091345273287','2007-01-01','Cao Đẳng','0635467865','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh5.jpg'),('n017','Thẩm Ấu Sở','Nam','Nhân viên thời vụ','1976-01-01','091234234876','2007-01-01','Trung Cấp','0654378261','Thôi việc','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh7.jpg'),('n018','Trần Ngọc Diễm','Nữ','Công nhân may','2000-01-01','012654789354','2007-01-01','Sơ Cấp Bậc 2','0985673214','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh8.jpg'),('n020','Đỗ Rê Mon','Nam','Nhân viên văn phòng','2005-06-05','098767898765','2007-01-30','Tốt nghiệp cấp 3','0989098789','Thôi việc','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh10.jpg'),('n021','Nô Bi Ta','Nam','Công nhân may','2002-03-06','123432654786','2009-07-14','Đại học','7465745362','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh11.jpg'),('n022','Xi Su Ka','Nữ','Công nhân may','2002-12-31','456354786564','2013-07-14','Cấp 3','1235678764','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh12.jpg'),('n023','Su Ni Ô','Nữ','Nhân viên thời vụ','1976-10-25','134253465785','2013-07-14','Cao Đẳng','6475636547','Thôi việc','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh13.jpg'),('n024','Chai An','Nam','Nhân viên văn phòng','1973-10-25','123456539055','2013-07-14','Tiêns sĩ','1342365766','Nghỉ phép','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh14.jpeg'),('n025','Trần Ngọc Ánh','Nữ','Công nhân may','1972-10-25','555666777888','2013-07-14','Đại Học','3546787645','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh16.jpg'),('n026','Nguyễn Lan Anh','Nữ','Công nhân may','2005-10-27','647888777987','2025-07-14','Cấp 3','2435647689','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh17.jpg'),('n027','Lê Hương Giang','Nữ','Công nhân may','2005-09-20','675687487987','2021-07-14','Đại học','4675847364','Thôi việc','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh18.jpeg'),('n028','Võ Sơn Tùng','Nam','Công nhân may','1999-07-17','467587654564','2021-07-14','Tiến sĩ','7656765432','Nghỉ phép','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh19.jpg'),('n029','Nguyễn Phương Linh','Nữ','Công nhân may','2003-07-17','145432676588','2018-07-17','Không có','3565478763','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh20.jpg'),('n030','Trần Quốc Toàn','Nam','Công nhân may','1990-12-18','456768798654','2018-07-17','Thạc Sỹ','2345769876','Nghỉ phép','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh21.jpg'),('nv22','Nô Bi Tô','Nam','Công nhân may','1970-01-01','123332654786','2007-01-01','Đại học','7463345362','Đang làm việc ','C:\\Users\\x\\Downloads\\HAT\\Java_oop\\BAI_TAP_LON\\QLNV_MA\\src\\anh_nv\\anh25.jpg');
/*!40000 ALTER TABLE `nhan_su` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taikhoan`
--

DROP TABLE IF EXISTS `taikhoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taikhoan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `hoten` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `capdo` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan`
--

LOCK TABLES `taikhoan` WRITE;
/*!40000 ALTER TABLE `taikhoan` DISABLE KEYS */;
INSERT INTO `taikhoan` VALUES (1,'12345','123123','hâhah','hat252006@gmail.com',0),(4,'hat','123123','ahsdgsad','tuan262006@gmail.com',0),(5,'Hat262006','123123','Hoàng Anh Tuấn','Hat262006@gmail.com',1),(6,'111111','123123','HAT','dsfdsfds@gmail.com',0),(7,'jack97','phuongtuan','phương tuấn','j97@gmail.com',0),(8,'hat123','hat123456','phuongw tubs','hat123@gmail.com',0);
/*!40000 ALTER TABLE `taikhoan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-20  1:59:58
