--CREATE DATABASE diary;

CREATE TABLE IF NOT EXISTS author
(
  author_id VARCHAR(255) NOT NULL COMMENT 'unique id',
  author_code VARCHAR(16) NOT NULL COMMENT 'authentication, code for use on single device',
  nickname VARCHAR(50) NOT NULL,
  description VARCHAR(50) NOT NULL,
  chocolates INT(11) NOT NULL,
  is_deleted TINYINT NOT NULL COMMENT '0: not deleted, 1: deleted' DEFAULT 0,
  creation_time TIMESTAMP NOT NULL DEFAULT now(),
  modification_time TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT author_pk PRIMARY KEY (author_id)
);

CREATE TABLE IF NOT EXISTS author_nickname_history
(
  author_id VARCHAR(255) NOT NULL,
  sequence BIGINT NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  creation_time TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT author_nickname_history_pk PRIMARY KEY (author_id, sequence)
);

CREATE TABLE IF NOT EXISTS author_description_history
(
  author_id VARCHAR(255) NOT NULL,
  sequence BIGINT NOT NULL,
  description VARCHAR(50) NOT NULL,
  creation_time TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT author_description_history_pk PRIMARY KEY (author_id, sequence)
);

CREATE TABLE IF NOT EXISTS author_chocolates_history
(
  author_id VARCHAR(255) NOT NULL,
  sequence BIGINT NOT NULL,
  chocolates_changed INT(11) NOT NULL,
  chocolates_result INT(11) NOT NULL,
  details VARCHAR(50) NOT NULL,
  creation_time TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT author_chocolates_history_pk PRIMARY KEY (author_id, sequence)
);

CREATE TABLE IF NOT EXISTS author_analyzed
(
  author_id VARCHAR(255) NOT NULL,
  language VARCHAR(10) NOT NULL,
  country VARCHAR(10) NOT NULL,
  time_zone_id VARCHAR(30) NOT NULL,
  CONSTRAINT author_analyzed_pk PRIMARY KEY (author_id)
);

CREATE TABLE IF NOT EXISTS author_diary
(
  author_id VARCHAR(255) NOT NULL,
  diary_date VARCHAR(8) NOT NULL,
  title VARCHAR(80) NULL,
  content VARCHAR(3000) NULL,
  language VARCHAR(10) NOT NULL,
  country VARCHAR(10) NOT NULL,
  time_zone_id VARCHAR(30) NOT NULL,
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
  letter_id VARCHAR(300) NOT NULL COMMENT 'from_author_id + written_time',
  letter_type INT(11) NOT NULL COMMENT '0: normal, 1: invitation',
  from_author_id VARCHAR(255) NOT NULL,
  from_author_nickname VARCHAR(50) NOT NULL COMMENT 'from author nickname at the time of writing',
  to_author_id VARCHAR(255) NOT NULL,
  to_author_nickname VARCHAR(50) NOT NULL COMMENT 'to author nickname at the time of writing',
  content VARCHAR(3000) NULL,
  written_time TIMESTAMP NULL DEFAULT now(),
  CONSTRAINT letter_pk PRIMARY KEY (letter_id)
);

CREATE TABLE IF NOT EXISTS diary_group
(
  diary_group_id BIGINT NOT NULL AUTO_INCREMENT,
  diary_group_name VARCHAR(255) NULL,
  host_author_id VARCHAR(255) NOT NULL,
  keyword VARCHAR(80) NULL,
  max_author_count INT(11) NOT NULL,
  language VARCHAR(10) NOT NULL,
  country VARCHAR(10) NOT NULL,
  time_zone_id VARCHAR(30) NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  CONSTRAINT diary_group_pk PRIMARY KEY (diary_group_id)
);

CREATE TABLE IF NOT EXISTS diary_group_author
(
  diary_group_id BIGINT NOT NULL,
  author_id VARCHAR(255) NOT NULL,
  author_status INT(11) NOT NULL COMMENT '0: invite, 1: accept, 2: refuse',
  CONSTRAINT diary_group_author_pk PRIMARY KEY (diary_group_id, author_id)
);
