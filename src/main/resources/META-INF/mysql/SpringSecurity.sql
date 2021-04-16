DROP DATABASE IF EXISTS example_spring_security;
CREATE DATABASE example_spring_security;
USE  example_spring_security;

CREATE TABLE USERS(
    username VARCHAR(20)  NOT NULL PRIMARY KEY,
    password VARCHAR(20) NOT NULL,
    enabled boolean NOT NULL
);

CREATE TABLE AUTHORITIES(
    username VARCHAR(20),
    authoritiy ENUM("MEMBER", "MANAGER", "ADMIN") NOT NULL DEFAULT "MEMBER",
    FOREIGN KEY (username) REFERENCES USERS(username)
);

CREATE TABLE GROUPS(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE GROUP_MEMBERS(
    group_id BIGINT ,
    username VARCHAR(20),
    FOREIGN KEY(group_id) REFERENCES GROUPS(id),
    FOREIGN KEY(group_members) REFERENCES USERS(username)
);

CREATE TABLE GROUP_AUTHORITIES(
    group_id BIGINT,
    authoritiy VARCHAR(20),
    FOREIGN KEY(group_id) REFERENCES GROUPS(id)
);