CREATE SCHEMA `dndata`;

CREATE TABLE `dndata`.`users` (
  `id` SERIAL,
  `name` TINYTEXT NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` TINYTEXT NOT NULL,
  `active` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

CREATE TABLE `dndata`.`user_rights` (
  `id` SERIAL,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `right` ENUM('EDIT', 'REVIEW', 'PUBLISH', 'MANAGE_USERS') NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_rights_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `dndata`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `dndata`.`books` (
  `id` SERIAL,
  `name` TINYTEXT NOT NULL,
  `publisher` TINYTEXT NOT NULL,
  `isbn` BIGINT UNSIGNED NOT NULL,
  `game` ENUM('PF2') NOT NULL DEFAULT 'PF2',
  PRIMARY KEY (`id`));

CREATE TABLE `dndata`.`objects` (
  `id` SERIAL,
  `json` JSON NOT NULL,
  `book_id` BIGINT UNSIGNED NOT NULL,
  `game` ENUM('PF2') NOT NULL DEFAULT 'PF2',
  `type` ENUM('CREATURE', 'ITEM', 'SPELL', 'FEAT', 'BOOK') NOT NULL,
  `status` ENUM('EDITING', 'AWAITING_REVIEW', 'REVIEWED') NOT NULL DEFAULT 'EDITING',
  `schema_version` INT NOT NULL DEFAULT 1,
  `editor_user_id` BIGINT UNSIGNED NOT NULL,
  `comment` TEXT(4096) NULL,
  PRIMARY KEY (`id`),
  INDEX `game_index` (`game` ASC) INVISIBLE,
  INDEX `type_index` (`type` ASC) VISIBLE,
  INDEX `status_index` (`status` ASC) VISIBLE,
  CONSTRAINT `fk_objects_editor_user_id`
    FOREIGN KEY (`editor_user_id`)
    REFERENCES `dndata`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_objects_book_id`
	FOREIGN KEY (`book_id`)
	REFERENCES `dndata`.`books` (`id`)
	ON DELETE CASCADE
	ON UPDATE CASCADE);

CREATE TABLE `dndata`.`publication_datasets` (
  `id` SERIAL,
  `date` DATE NOT NULL,
  `publisher_user_id` BIGINT UNSIGNED NOT NULL,
  `comment` TEXT(4096) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_publication_datasets_user_id`
    FOREIGN KEY (`publisher_user_id`)
    REFERENCES `dndata`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `dndata`.`publication_data` (
  `id` SERIAL,
  `publication_id` BIGINT UNSIGNED NOT NULL,
  `object_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_publication_data_publication_id`
    FOREIGN KEY (`publication_id`)
    REFERENCES `dndata`.`publication_datasets` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_publication_data_object_id`
    FOREIGN KEY (`object_id`)
    REFERENCES `dndata`.`objects` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
