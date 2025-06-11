CREATE DATABASE telephonedb;
USE telephonedb;
CREATE TABLE directory (
    lastName VARCHAR(50),
    firstName VARCHAR(50),
    middleName VARCHAR(50),
    address VARCHAR(100),
    phoneNumber VARCHAR(15),
    PRIMARY KEY (lastName, firstName, middleName)
);
