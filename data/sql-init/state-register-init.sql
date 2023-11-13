CREATE DATABASE IF NOT EXISTS state_register;
USE state_register;

# create-drop mode for tables

# test data:
INSERT INTO citizen_model (passport_id, age, is_capable, is_prisoner, first_name, last_name)
VALUES (1001, 30, true, false, "Bob", "Black"),
       (1002, 31, true, false, "Sam", "Green"),
       (1003, 32, true, false, "Tomas", "Brown"),
       (1004, 33, true, false, "John", "Lo"),
       (1005, 34, false, false, "Kim", "Chan"),
       (1006, 10, true, false, "Sue", "Law"),
       (1007, 11, true, false, "Ed", "Doe"),
       (1008, 12, false, false, "Lia", "Lee");
