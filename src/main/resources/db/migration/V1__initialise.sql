CREATE SCHEMA IF NOT EXISTS `dndata`;

CREATE TABLE `dndata`.`books` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` TINYTEXT NOT NULL,
  `publisher` TINYTEXT NOT NULL,
  `isbn` TINYTEXT NOT NULL,
  `game` ENUM('PF2') NOT NULL DEFAULT 'PF2',
  PRIMARY KEY (`id`),
  INDEX `game_index` (`game`));

CREATE TABLE `dndata`.`objects` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `revision` INT UNSIGNED NOT NULL DEFAULT 1,
  `json` JSON NOT NULL,
  `schema_version` INT NOT NULL DEFAULT 1,
  `name` VARCHAR(255) NOT NULL,
  `type` ENUM('CREATURE', 'FEAT', 'ITEM', 'SPELL') NOT NULL,
  `book_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`, `revision`),
  UNIQUE INDEX `id_revision_UNIQUE` (`id`, `revision`),
  INDEX `type_index` (`type`),
  INDEX `name_index` (`name`),
  CONSTRAINT `fk_objects_book_id`
	FOREIGN KEY (`book_id`)
	REFERENCES `dndata`.`books` (`id`)
	ON DELETE RESTRICT
	ON UPDATE CASCADE);

CREATE TABLE `dndata`.`users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` TINYTEXT NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` TINYTEXT NOT NULL,
  `active` TINYINT NOT NULL DEFAULT 1,
  `claim_object_id` BIGINT UNSIGNED NULL,
  `claim_date` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email`),
  INDEX `claim_index` (`claim_object_id`),
  CONSTRAINT `fk_user_claim_object_id`
      FOREIGN KEY (`claim_object_id`)
      REFERENCES `dndata`.`objects` (`id`)
      ON DELETE SET NULL
      ON UPDATE CASCADE);

CREATE TABLE `dndata`.`user_rights` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `role` ENUM('ROLE_PF2_EDIT', 'ROLE_PF2_REVIEW', 'ROLE_PF2_BOOKS', 'ROLE_PF2_PUBLISH', 'ROLE_PF2_MANAGE_USERS') NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_rights_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `dndata`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `dndata`.`object_status` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`status` ENUM('CREATED', 'EDITING', 'AWAITING_REVIEW', 'REVIEWED', 'PUBLISHED', 'DELETED') NOT NULL DEFAULT 'CREATED',
	`comment` TEXT(4096) NOT NULL,
	`editor_id` BIGINT UNSIGNED NOT NULL,
	`object_id` BIGINT UNSIGNED NOT NULL,
	`object_revision` INT UNSIGNED NOT NULL,
	`status_date` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `id_UNIQUE` (`id`),
	CONSTRAINT `fk_object_status_object`
		FOREIGN KEY (`object_id`, `object_revision`)
		REFERENCES `dndata`.`objects` (`id`, `revision`)
		ON DELETE RESTRICT
		ON UPDATE CASCADE,
	CONSTRAINT `fk_object_status_editor`
		FOREIGN KEY (`editor_id`)
		REFERENCES `dndata`.`users` (`id`)
		ON DELETE RESTRICT
		ON UPDATE CASCADE);

CREATE TABLE `dndata`.`changesets` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `published_date` TIMESTAMP NOT NULL,
  `publisher_user_id` BIGINT UNSIGNED NOT NULL,
  `comment` TEXT(4096) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_changesets_user_id`
    FOREIGN KEY (`publisher_user_id`)
    REFERENCES `dndata`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);

CREATE TABLE `dndata`.`changes` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `changeset_id` BIGINT UNSIGNED NOT NULL,
  `object_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_changes_changeset_id`
    FOREIGN KEY (`changeset_id`)
    REFERENCES `dndata`.`changesets` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_changes_object_id`
    FOREIGN KEY (`object_id`)
    REFERENCES `dndata`.`objects` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE);
