SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `wikt` ;
CREATE SCHEMA IF NOT EXISTS `wikt` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `wikt` ;

-- -----------------------------------------------------
-- Table `wikt`.`etymology`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`etymology` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`etymology` (
  `idetymology` INT NOT NULL AUTO_INCREMENT ,
  `dataField` VARCHAR(45) NULL ,
  PRIMARY KEY (`idetymology`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `wikt`.`example`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`example` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`example` (
  `idexample` INT NOT NULL AUTO_INCREMENT ,
  `dataField` VARCHAR(4000) NULL ,
  PRIMARY KEY (`idexample`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `wikt`.`sense`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`sense` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`sense` (
  `idsense` INT NOT NULL AUTO_INCREMENT ,
  `example_idexample` INT NULL ,
  `dataField` VARCHAR(4000) NULL ,
  PRIMARY KEY (`idsense`) ,
  CONSTRAINT `fk_sense_example1`
    FOREIGN KEY (`example_idexample` )
    REFERENCES `wikt`.`example` (`idexample` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_sense_example1_idx` ON `wikt`.`sense` (`example_idexample` ASC) ;


-- -----------------------------------------------------
-- Table `wikt`.`wordpos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`wordpos` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`wordpos` (
  `idpos` INT NOT NULL AUTO_INCREMENT ,
  `sense_idsense` INT NULL ,
  `dataField` VARCHAR(45) NULL ,
  PRIMARY KEY (`idpos`) ,
  CONSTRAINT `fk_wordpos_sense1`
    FOREIGN KEY (`sense_idsense` )
    REFERENCES `wikt`.`sense` (`idsense` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_wordpos_sense1_idx` ON `wikt`.`wordpos` (`sense_idsense` ASC) ;


-- -----------------------------------------------------
-- Table `wikt`.`wordentry`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`wordentry` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`wordentry` (
  `identry` INT NOT NULL AUTO_INCREMENT ,
  `wordpos_idpos` INT NULL ,
  `dataField` VARCHAR(45) NULL ,
  PRIMARY KEY (`identry`) ,
  CONSTRAINT `fk_wordentry_wordpos1`
    FOREIGN KEY (`wordpos_idpos` )
    REFERENCES `wikt`.`wordpos` (`idpos` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_wordentry_wordpos1_idx` ON `wikt`.`wordentry` (`wordpos_idpos` ASC) ;


-- -----------------------------------------------------
-- Table `wikt`.`wordetymology`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`wordetymology` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`wordetymology` (
  `idwordetymology` INT NOT NULL AUTO_INCREMENT ,
  `etymology_idetymology` INT NULL ,
  `wordentry_identry` INT NULL ,
  `dataField` VARCHAR(45) NULL ,
  PRIMARY KEY (`idwordetymology`) ,
  CONSTRAINT `fk_wordetymology_etymology1`
    FOREIGN KEY (`etymology_idetymology` )
    REFERENCES `wikt`.`etymology` (`idetymology` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_wordetymology_wordentry1`
    FOREIGN KEY (`wordentry_identry` )
    REFERENCES `wikt`.`wordentry` (`identry` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_wordetymology_etymology1_idx` ON `wikt`.`wordetymology` (`etymology_idetymology` ASC) ;

CREATE INDEX `fk_wordetymology_wordentry1_idx` ON `wikt`.`wordetymology` (`wordentry_identry` ASC) ;


-- -----------------------------------------------------
-- Table `wikt`.`language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`language` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`language` (
  `idlanguage` INT NOT NULL AUTO_INCREMENT ,
  `langname` VARCHAR(45) NULL ,
  `langcode` CHAR(6) NULL ,
  PRIMARY KEY (`idlanguage`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `wikt`.`wordlanguage`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`wordlanguage` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`wordlanguage` (
  `idwordlanguage` INT NOT NULL AUTO_INCREMENT ,
  `wordetymology_idwordetymology` INT NULL ,
  `dataField` VARCHAR(45) NULL ,
  `language_idlanguage` INT NULL ,
  PRIMARY KEY (`idwordlanguage`) ,
  CONSTRAINT `fk_wordlanguage_wordetymology1`
    FOREIGN KEY (`wordetymology_idwordetymology` )
    REFERENCES `wikt`.`wordetymology` (`idwordetymology` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_wordlanguage_language1`
    FOREIGN KEY (`language_idlanguage` )
    REFERENCES `wikt`.`language` (`idlanguage` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_wordlanguage_wordetymology1_idx` ON `wikt`.`wordlanguage` (`wordetymology_idwordetymology` ASC) ;

CREATE INDEX `fk_wordlanguage_language1_idx` ON `wikt`.`wordlanguage` (`language_idlanguage` ASC) ;


-- -----------------------------------------------------
-- Table `wikt`.`word`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`word` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`word` (
  `idword` INT NOT NULL AUTO_INCREMENT ,
  `wordlanguage_idwordlanguage` INT NULL ,
  `dataField` VARCHAR(45) NULL ,
  PRIMARY KEY (`idword`) ,
  CONSTRAINT `fk_word_wordlanguage1`
    FOREIGN KEY (`wordlanguage_idwordlanguage` )
    REFERENCES `wikt`.`wordlanguage` (`idwordlanguage` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_word_wordlanguage1_idx` ON `wikt`.`word` (`wordlanguage_idwordlanguage` ASC) ;


-- -----------------------------------------------------
-- Table `wikt`.`synset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wikt`.`synset` ;

CREATE  TABLE IF NOT EXISTS `wikt`.`synset` (
  `idsynset` INT NOT NULL AUTO_INCREMENT ,
  `wordentry_identry` INT NULL ,
  `dataField` VARCHAR(45) NULL ,
  PRIMARY KEY (`idsynset`) ,
  CONSTRAINT `fk_synset_wordentry1`
    FOREIGN KEY (`wordentry_identry` )
    REFERENCES `wikt`.`wordentry` (`identry` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_synset_wordentry1_idx` ON `wikt`.`synset` (`wordentry_identry` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
