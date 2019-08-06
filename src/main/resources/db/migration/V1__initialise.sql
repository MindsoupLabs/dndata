CREATE SCHEMA IF NOT EXISTS `dndata`;

CREATE TABLE `dndata`.`users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` TINYTEXT NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` TINYTEXT NOT NULL,
  `active` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email`));

CREATE TABLE `dndata`.`user_rights` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `right` ENUM('EDIT', 'REVIEW', 'PUBLISH', 'MANAGE_USERS') NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_rights_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `dndata`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `dndata`.`books` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` TINYTEXT NOT NULL,
  `publisher` TINYTEXT NOT NULL,
  `isbn` BIGINT UNSIGNED NOT NULL,
  `game` ENUM('PF2') NOT NULL DEFAULT 'PF2',
  PRIMARY KEY (`id`),
  INDEX `game_index` (`game`));

CREATE TABLE `dndata`.`objects` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `revision` INT UNSIGNED NOT NULL DEFAULT 1,
  `json` JSON NOT NULL,
  `book_id` BIGINT UNSIGNED NOT NULL,
  `type` ENUM('CREATURE', 'ITEM', 'SPELL', 'FEAT') NOT NULL,
  `status` ENUM('EDITING', 'AWAITING_REVIEW', 'REVIEWED') NOT NULL DEFAULT 'EDITING',
  `schema_version` INT NOT NULL DEFAULT 1,
  `editor_user_id` BIGINT UNSIGNED NOT NULL,
  `comment` TEXT(4096) NOT NULL,
  PRIMARY KEY (`id`, `revision`),
  UNIQUE INDEX `id_revision_UNIQUE` (`id`, `revision`),
  INDEX `type_index` (`type`),
  INDEX `status_index` (`status`),
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

CREATE TABLE `dndata`.`changesets` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `published_date` DATE NOT NULL,
  `publisher_user_id` BIGINT UNSIGNED NOT NULL,
  `comment` TEXT(4096) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_changesets_user_id`
    FOREIGN KEY (`publisher_user_id`)
    REFERENCES `dndata`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `dndata`.`changes` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `changeset_id` BIGINT UNSIGNED NOT NULL,
  `object_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_changes_changeset_id`
    FOREIGN KEY (`changeset_id`)
    REFERENCES `dndata`.`changesets` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_changes_object_id`
    FOREIGN KEY (`object_id`)
    REFERENCES `dndata`.`objects` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
