CREATE TABLE IF NOT EXISTS account
(
  user_id VARCHAR(255) NOT NULL COMMENT 'email(member) or unique id',
  password_enc VARCHAR(64) NULL COMMENT 'password(member) or null',
  creation_time TIMESTAMP NULL DEFAULT now(),
  modification_time TIMESTAMP NULL DEFAULT now(),
  CONSTRAINT account_pk PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS diary
(
  write_day VARCHAR(8) NOT NULL,
  write_user_id VARCHAR(255) NOT NULL,
  title VARCHAR(200) NULL,
  content VARCHAR(2000) NOT NULL,
  CONSTRAINT diary_pk PRIMARY KEY (write_day, write_user_id)
);

CREATE TABLE IF NOT EXISTS letter
(
  write_day VARCHAR(6) NOT NULL,
  write_user_id VARCHAR(255) NOT NULL,
  read_user_id VARCHAR(255) NOT NULL,
  title VARCHAR(200) NULL,
  content VARCHAR(2000) NOT NULL,
  CONSTRAINT letter_pk PRIMARY KEY (write_day, write_user_id, read_user_id)
);