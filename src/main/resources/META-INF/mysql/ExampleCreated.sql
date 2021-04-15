DROP DATABASE IF EXISTS example_database;
CREATE DATABASE example_database;
USE example_database;

DROP TABLE IF EXISTS Number;
CREATE TABLE NumberType(
    id INT PRIMARY KEY AUTO_INCREMENT, 
    column1 TINYINT(5) ZEROFILL DEFAULT 1,
    column2 SMALLINT(5) UNSIGNED DEFAULT 2,
    column3 MEDIUMINT(5)  DEFAULT 3,
    column4 INT(5) DEFAULT 4,
    column5 BIGINT(5) DEFAULT 8,
    column6 DECIMAL(10, 5) DEFAULT 10,
    column7 NUMERIC(10, 5) DEFAULT 10,
    column8 FLOAT(10,5) DEFAULT 23,
    column9 DOUBLE(10,5) DEFAULT 53,
    column10 BIT(10) DEFAULT b'1101'
);

INSERT 
INTO NumberType (column1,column2,column3,column4,column5,column6,column7,column8,column9)
VALUES      (255, 65535,8388607,-2147483648,-9223372036854775808

, -99999.99999, -99999.99999, -99999.99999, -99999.99999 );

SELECT * FROM NumberType;



