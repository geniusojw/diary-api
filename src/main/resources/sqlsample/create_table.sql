CREATE TABLE IF NOT EXISTS author
(
  author_id VARCHAR(255) NOT NULL COMMENT 'unique id',
  author_code VARCHAR(16) NOT NULL COMMENT 'authentication, code for use on single device',
  nickname VARCHAR(50) NOT NULL,
  description VARCHAR(50) NOT NULL,
  is_deleted TINYINT NOT NULL COMMENT '0: not deleted, 1: deleted' DEFAULT 0,
  creation_time TIMESTAMP NOT NULL DEFAULT now(),
  modification_time TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT author_pk PRIMARY KEY (author_id)
);

CREATE TABLE IF NOT EXISTS author_diary
(
  author_id VARCHAR(255) NOT NULL,
  diary_date VARCHAR(8) NOT NULL,
  title VARCHAR(80) NULL,
  content VARCHAR(3000) NULL,
  language VARCHAR(10) NOT NULL,
  country VARCHAR(10) NOT NULL,
  CONSTRAINT author_diary PRIMARY KEY (author_id, diary_date)
);

CREATE TABLE IF NOT EXISTS account
(
  account_email VARCHAR(255) NOT NULL COMMENT 'email',
  password_enc VARCHAR(64) NOT NULL COMMENT 'password(member) or null',
  first_author_id VARCHAR(255) NOT NULL COMMENT 'unique id',
  last_author_id VARCHAR(255) NOT NULL COMMENT 'unique id',
  creation_time TIMESTAMP NULL DEFAULT now(),
  modification_time TIMESTAMP NULL DEFAULT now(),
  CONSTRAINT account_pk PRIMARY KEY (account_email)
);

CREATE TABLE IF NOT EXISTS account_diary
(
  account_email VARCHAR(255) NOT NULL,
  diary_date VARCHAR(8) NOT NULL,
  title VARCHAR(80) NULL,
  content VARCHAR(3000) NULL,
  CONSTRAINT account_diary PRIMARY KEY (account_email, diary_date)
);

CREATE TABLE IF NOT EXISTS letter
(
  letter_id VARCHAR(300) NOT NULL COMMENT 'author_id + sequence',
  from_author_id VARCHAR(255) NOT NULL,
  to_author_id VARCHAR(255) NOT NULL,
  title VARCHAR(80) NULL,
  content VARCHAR(3000) NULL,
  written_time TIMESTAMP NULL DEFAULT now(),
  CONSTRAINT letter_pk PRIMARY KEY (letter_id)
);
