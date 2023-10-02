CREATE DATABASE IF NOT EXISTS state_register;

USE state_register;

CREATE TABLE citizen_model
(
    age         INT,
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_capable  BIT,
    is_prisoner BIT,
    passport_id INT
);

INSERT INTO citizen_model (passport_id, age, is_capable, is_prisoner)
VALUES (1001, 10, true, false),
       (1002, 30, true, false),
       (1003, 19, true, true),
       (1004, 45, false, false),
       (1005, 2, true, false),
       (1006, 35, true, true),
       (1007, 11, false, true),
       (1008, 22, true, false);