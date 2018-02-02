/*
SQLyog Community v12.11 (64 bit)
MySQL - 5.5.3-m3-log : Database - piloto
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`piloto` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `piloto`;

/*Table structure for table `participante` */

DROP TABLE IF EXISTS `participante`;

CREATE TABLE `participante` (
  `id` char(36) NOT NULL COMMENT 'ID do participante',
  `id_projeto` char(36) NOT NULL COMMENT 'ID do projeto piloto',
  `orgao` char(2) NOT NULL COMMENT 'Código da Sucursal',
  `corretor` char(4) DEFAULT NULL COMMENT 'Código do Corretor sem sucursal',
  `codigo_origem_calculo` char(2) DEFAULT NULL COMMENT 'Código da Origem de Cálculo',
  `data_inicio` date NOT NULL COMMENT 'Data de início da participação do Piloto',
  `data_fim` date NOT NULL COMMENT 'Data de fim de piloto',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_participante` (`id_projeto`,`orgao`,`corretor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `projeto` */

DROP TABLE IF EXISTS `projeto`;

CREATE TABLE `projeto` (
  `id` char(36) NOT NULL COMMENT 'ID do projeto',
  `descricao` varchar(50) DEFAULT NULL COMMENT 'Descrição do Projeto',
  `status` char(10) DEFAULT NULL COMMENT 'Status do projeto',
  `data_fim` date NOT NULL COMMENT 'Data de fim de piloto',
  PRIMARY KEY (`id`),
  KEY `fk_status` (`status`),
  KEY `idx_descricao` (`descricao`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `status` */

DROP TABLE IF EXISTS `status`;

CREATE TABLE `status` (
  `id` char(36) NOT NULL COMMENT 'PK',
  `descricao` varchar(50) DEFAULT NULL COMMENT 'Descrição',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
