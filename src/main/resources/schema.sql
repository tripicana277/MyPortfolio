CREATE TABLE  IF NOT EXISTS `LOGIN` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `RECIPEMAIN` (
  `RECIPENAME` varchar(255) NOT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `NUMBER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`RECIPENAME`)
);

CREATE TABLE  IF NOT EXISTS IMAGE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_name VARCHAR(255),
    image_data LONGBLOB
);

CREATE TABLE IF NOT EXISTS `MATERIAL` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `RECIPENAME` varchar(255) NOT NULL,
  `MATERIAL` varchar(255) NOT NULL,
  `QUANTITY` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_recipe_material` (`RECIPENAME`),
  CONSTRAINT `fk_recipe_material` FOREIGN KEY (`RECIPENAME`) REFERENCES `RECIPEMAIN` (`RECIPENAME`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `HOWTOMAKE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `RECIPENAME` varchar(255) NOT NULL,
  `FILENAME2` varchar(255) NOT NULL,
  `HOWTOMAKE` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_recipe_howtomake` (`RECIPENAME`),
  CONSTRAINT `fk_recipe_howtomake` FOREIGN KEY (`RECIPENAME`) REFERENCES `RECIPEMAIN` (`RECIPENAME`) ON DELETE CASCADE
);

CREATE TABLE  IF NOT EXISTS IMAGES (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_name VARCHAR(255),
    image_data LONGBLOB
);

CREATE TABLE IF NOT EXISTS `INCOME` (
  `INCOME_DATE` varchar(255) NOT NULL,
  `INCOME_NAME` varchar(255) NOT NULL,
  `INCOME_COUNT` int NOT NULL,
  PRIMARY KEY (`INCOME_NAME`)
);