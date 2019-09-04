CREATE TABLE resume (
  uuid      VARCHAR(36) PRIMARY KEY NOT NULL,
  full_name TEXT                 NOT NULL
);

CREATE TABLE contact (
  id          SERIAL      PRIMARY KEY NOT NULL,
  resume_uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type        TEXT        NOT NULL,
  name        TEXT        NOT NULL,
  url         TEXT        NOT NULL
);

CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (resume_uuid, type);

CREATE TABLE section (
  section_id              SERIAL      PRIMARY KEY NOT NULL,
  resume_uuid VARCHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type        TEXT        NOT NULL,
  content     TEXT        NULL
);

CREATE UNIQUE INDEX section_uuid_type_index
  ON section (resume_uuid, type);

-- Added 04.09.2019

-- CREATE TABLE name_variants (
--  id primary key not null,
--  name varchar(32) not null,
--  type int -- 0-surname, 1-name, 2-patronymic
--);

--CREATE TABLE words (
--  id primary key not null,
--  word varchar(32) not null
--);