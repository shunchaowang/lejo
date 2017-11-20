-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema lejo
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `lejo` ;

-- -----------------------------------------------------
-- Schema lejo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `lejo` DEFAULT CHARACTER SET utf8 ;
USE `lejo` ;

-- -----------------------------------------------------
-- Table `lejo`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`user` ;

CREATE TABLE IF NOT EXISTS `lejo`.`user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `first_name` VARCHAR(32) NOT NULL,
  `last_name` VARCHAR(32) NOT NULL,
  `profile_image` BLOB NULL,
  `description` TEXT NULL,
  `date_created` TIMESTAMP NOT NULL,
  `last_updated` TIMESTAMP NULL,
  `active` TINYINT NOT NULL,
  `version` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `USER_ID_UNIQUE` (`id` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lejo`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`role` ;

CREATE TABLE IF NOT EXISTS `lejo`.`role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `description` TEXT NULL,
  `active` TINYINT NOT NULL,
  `version` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ROLE_ID_UNIQUE` (`id` ASC),
  UNIQUE INDEX `ROLE_NAME_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lejo`.`menu_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`menu_category` ;

CREATE TABLE IF NOT EXISTS `lejo`.`menu_category` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `index` INT NOT NULL,
  `version` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `MENU_CATEGORY_ID_UNIQUE` (`id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lejo`.`menu_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`menu_item` ;

CREATE TABLE IF NOT EXISTS `lejo`.`menu_item` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `index` INT UNSIGNED NOT NULL,
  `target` VARCHAR(128) NOT NULL,
  `version` BIGINT UNSIGNED NULL,
  `menu_category_id` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `MENU_ITEM_ID_UNIQUE` (`id` ASC),
  INDEX `FK_MNTM_MNCG_ID_idx` (`menu_category_id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  CONSTRAINT `fk_menu_item_menu_category`
    FOREIGN KEY (`menu_category_id`)
    REFERENCES `lejo`.`menu_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lejo`.`permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`permission` ;

CREATE TABLE IF NOT EXISTS `lejo`.`permission` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `description` TEXT NULL,
  `active` TINYINT NOT NULL,
  `version` BIGINT UNSIGNED NULL,
  `menu_item_id` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `PRMS_ID_UNIQUE` (`id` ASC),
  UNIQUE INDEX `PRMS_NAME_UNIQUE` (`name` ASC),
  INDEX `FK_PRMS_MENU_ID_idx` (`menu_item_id` ASC),
  CONSTRAINT `fk_menu_item`
    FOREIGN KEY (`menu_item_id`)
    REFERENCES `lejo`.`menu_item` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lejo`.`role_permission_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`role_permission_mapping` ;

CREATE TABLE IF NOT EXISTS `lejo`.`role_permission_mapping` (
  `role_id` BIGINT UNSIGNED NOT NULL,
  `permission_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`),
  INDEX `fk_RPMP_PRMS_idx` (`permission_id` ASC),
  INDEX `fk_RPMP_ROLE_idx` (`role_id` ASC),
  CONSTRAINT `fk_rpmp_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `lejo`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rpmp_permission`
    FOREIGN KEY (`permission_id`)
    REFERENCES `lejo`.`permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lejo`.`user_role_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`user_role_mapping` ;

CREATE TABLE IF NOT EXISTS `lejo`.`user_role_mapping` (
  `user_id` BIGINT UNSIGNED NOT NULL,
  `role_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX `FK_ROLE_idx` (`role_id` ASC),
  CONSTRAINT `fk_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `lejo`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `lejo`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lejo`.`user_permission_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lejo`.`user_permission_mapping` ;

CREATE TABLE IF NOT EXISTS `lejo`.`user_permission_mapping` (
  `user_id` BIGINT UNSIGNED NOT NULL,
  `permission_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `permission_id`),
  INDEX `FK_URMP_ROLE_idx` (`permission_id` ASC),
  CONSTRAINT `fk_urmp_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `lejo`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_urmp_permission`
    FOREIGN KEY (`permission_id`)
    REFERENCES `lejo`.`permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `lejo`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `lejo`;
INSERT INTO `lejo`.`user` (`id`, `username`, `password`, `first_name`, `last_name`, `profile_image`, `description`, `date_created`, `last_updated`, `active`, `version`) VALUES (1, 'admin', '$2a$10$Ce2HJja0Trha0ee3.rMqQewIzJMVe87.jNi5zF5gDdsyvHjJsnwOm', 'Admin', 'Admin', NULL, NULL, now(), NULL, 1, NULL);
INSERT INTO `lejo`.`user` (`id`, `username`, `password`, `first_name`, `last_name`, `profile_image`, `description`, `date_created`, `last_updated`, `active`, `version`) VALUES (2, 'user', '$2a$10$kwTBVDaG5KyoFVed1wWItOSxeyehmtqy2TZ2vvcEzqpXGEoYlRu7W', 'User', 'User', NULL, NULL, now(), NULL, 1, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `lejo`.`role`
-- -----------------------------------------------------
START TRANSACTION;
USE `lejo`;
INSERT INTO `lejo`.`role` (`id`, `name`, `description`, `active`, `version`) VALUES (1, 'ROLE_ADMIN', 'System Admin', 1, NULL);
INSERT INTO `lejo`.`role` (`id`, `name`, `description`, `active`, `version`) VALUES (2, 'ROLE_USER', 'Normal User', 1, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `lejo`.`menu_category`
-- -----------------------------------------------------
START TRANSACTION;
USE `lejo`;
INSERT INTO `lejo`.`menu_category` (`id`, `name`, `index`, `version`) VALUES (1, 'userManagement', 1, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `lejo`.`menu_item`
-- -----------------------------------------------------
START TRANSACTION;
USE `lejo`;
INSERT INTO `lejo`.`menu_item` (`id`, `name`, `index`, `target`, `version`, `menu_category_id`) VALUES (1, 'manageUser', 11, 'user/index', NULL, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `lejo`.`permission`
-- -----------------------------------------------------
START TRANSACTION;
USE `lejo`;
INSERT INTO `lejo`.`permission` (`id`, `name`, `description`, `active`, `version`, `menu_item_id`) VALUES (1, 'manageUser', NULL, 1, NULL, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `lejo`.`role_permission_mapping`
-- -----------------------------------------------------
START TRANSACTION;
USE `lejo`;
INSERT INTO `lejo`.`role_permission_mapping` (`role_id`, `permission_id`) VALUES (1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `lejo`.`user_role_mapping`
-- -----------------------------------------------------
START TRANSACTION;
USE `lejo`;
INSERT INTO `lejo`.`user_role_mapping` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `lejo`.`user_role_mapping` (`user_id`, `role_id`) VALUES (2, 2);

COMMIT;

