SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `wikt` ;
CREATE SCHEMA IF NOT EXISTS `wikt` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `wikt` ;

-- -----------------------------------------------------
-- Table `wikt`.`word`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`word` ;

CREATE TABLE IF NOT EXISTS `wikt`.`word` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataField` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wikt`.`lang`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`lang` ;

CREATE TABLE IF NOT EXISTS `wikt`.`lang` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `code` CHAR(6) NOT NULL,
  `abr` CHAR(6) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wikt`.`wordlang`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`wordlang` ;

CREATE TABLE IF NOT EXISTS `wikt`.`wordlang` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataField` VARCHAR(45) NULL,
  `word_id` INT NOT NULL,
  `lang_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_wordlang_word_idx` (`word_id` ASC),
  INDEX `fk_wordlang_lang1_idx` (`lang_id` ASC),
  CONSTRAINT `fk_wordlang_word`
    FOREIGN KEY (`word_id`)
    REFERENCES `wikt`.`word` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_wordlang_lang1`
    FOREIGN KEY (`lang_id`)
    REFERENCES `wikt`.`lang` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wikt`.`wordetym`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`wordetym` ;

CREATE TABLE IF NOT EXISTS `wikt`.`wordetym` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataField` VARCHAR(45) NULL,
  `wordlang_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_wordetym_wordlang1_idx` (`wordlang_id` ASC),
  CONSTRAINT `fk_wordetym_wordlang1`
    FOREIGN KEY (`wordlang_id`)
    REFERENCES `wikt`.`wordlang` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wikt`.`wordentry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`wordentry` ;

CREATE TABLE IF NOT EXISTS `wikt`.`wordentry` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataField` VARCHAR(45) NULL,
  `pos` VARCHAR(45) NOT NULL,
  `wordetym_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_wordentry_wordetym1_idx` (`wordetym_id` ASC),
  CONSTRAINT `fk_wordentry_wordetym1`
    FOREIGN KEY (`wordetym_id`)
    REFERENCES `wikt`.`wordetym` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wikt`.`etym`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`etym` ;

CREATE TABLE IF NOT EXISTS `wikt`.`etym` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataField` VARCHAR(45) NULL,
  `wordetym_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_etym_wordetym1_idx` (`wordetym_id` ASC),
  CONSTRAINT `fk_etym_wordetym1`
    FOREIGN KEY (`wordetym_id`)
    REFERENCES `wikt`.`wordetym` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wikt`.`sense`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`sense` ;

CREATE TABLE IF NOT EXISTS `wikt`.`sense` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataField` VARCHAR(4000) NULL,
  `wordentry_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_sense_wordentry1_idx` (`wordentry_id` ASC),
  CONSTRAINT `fk_sense_wordentry1`
    FOREIGN KEY (`wordentry_id`)
    REFERENCES `wikt`.`wordentry` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wikt`.`example`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`example` ;

CREATE TABLE IF NOT EXISTS `wikt`.`example` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataField` VARCHAR(4000) NULL,
  `sense_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_example_sense1_idx` (`sense_id` ASC),
  CONSTRAINT `fk_example_sense1`
    FOREIGN KEY (`sense_id`)
    REFERENCES `wikt`.`sense` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
