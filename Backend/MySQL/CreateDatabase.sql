CREATE SCHEMA `financio2` ;
SET sql_mode='';

CREATE TABLE `financio2`.`user` (
  `userId` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `city` VARCHAR(45) NULL,
  `state` VARCHAR(45) NULL,
  `zip` INT NULL,
  `dateCreated` DATETIME NULL,
  `dateLocked` DATETIME NULL,
  `firstname` VARCHAR(45) NULL,
  `middleinitial` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `dateOfBirth` DATETIME NULL,
  `gender` VARCHAR(45) NULL,
  `picturePath` VARCHAR(135) NULL,
  `email` VARCHAR(45) NULL,
  UNIQUE INDEX `usersId_UNIQUE` (`userId` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);
  
  CREATE TABLE `financio2`.`expenses` (
  `expenseId` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `resturant` DOUBLE UNSIGNED NULL,
  `subscriptions` DOUBLE UNSIGNED NULL,
  `essentials` DOUBLE UNSIGNED NULL,
  `grocery` DOUBLE UNSIGNED NULL,
  `gas` DOUBLE UNSIGNED NULL,
  `alcohol` DOUBLE UNSIGNED NULL,
  `other` DOUBLE UNSIGNED NULL,
  `dateCreated` DATETIME NULL,
  `dateModified` DATETIME NULL,
  `dateRemoved` VARCHAR(45) NULL,
  `userId` INT UNSIGNED NULL,
  UNIQUE INDEX `expenseId_UNIQUE` (`expenseId` ASC) VISIBLE, 
  
  CONSTRAINT `userToExpense`
    FOREIGN KEY (`userId`)
    REFERENCES `financio2`.`user` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE `financio2`.`reported_expense` (
  `reported_expenseId` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `resturant` DOUBLE UNSIGNED NULL,
  `subscriptions` DOUBLE UNSIGNED NULL,
  `essentials` DOUBLE UNSIGNED NULL,
  `grocery` DOUBLE UNSIGNED NULL,
  `gas` DOUBLE UNSIGNED NULL,
  `alcohol` DOUBLE UNSIGNED NULL,
  `other` DOUBLE UNSIGNED NULL,
  `dateCreated` DATETIME NULL,
  `expenseId` INT UNSIGNED NULL,
  UNIQUE INDEX `reported_expenseId_UNIQUE` (`reported_expenseId` ASC) VISIBLE,
  CONSTRAINT `expenseToReportedExpense`
    FOREIGN KEY (`expenseId`)
    REFERENCES `financio2`.`expenses` (`expenseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;  
