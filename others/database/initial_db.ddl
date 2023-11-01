-- ###############
--    create MySQL database , if need create, cancel the comment
-- ###############
-- create database if not exists oauth2_boot default character set utf8;
-- use oauth2_boot set default character = utf8;

-- ###############
--    grant privileges  to oauth2/oauth2
-- ###############
-- GRANT ALL PRIVILEGES ON oauth2.* TO oauth2@localhost IDENTIFIED BY "oauth2";

-- ###############
-- Domain:  User
-- ###############
Drop table if exists user_;
CREATE TABLE user_
(
    id              int(11)      NOT NULL auto_increment,
    guid            varchar(255) not null unique,
    create_time     datetime,
    archived        tinyint(1) default '0',
    updated_time    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    username        varchar(255) not null unique,
    password        varchar(255) not null,
    enabled         tinyint(1) default '1',
    phone           varchar(255),
    email           varchar(255),
    address         varchar(255),
    nickname        varchar(255),
    updated_at      int(15)    default 0,
    default_user    tinyint(1) default '0',
    last_login_time datetime,
    PRIMARY KEY (id),
    index idx_username (username)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20
  DEFAULT CHARSET = utf8;


-- ###############
-- Domain:  Privilege
-- ###############
Drop table if exists user_privilege;
CREATE TABLE user_privilege
(
    user_id   int(11),
    privilege varchar(255),
    KEY user_id_index (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

