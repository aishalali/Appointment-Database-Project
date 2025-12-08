/*
Emergency Contact Rules:
- id is unique. It cannot be greater than 8 characters long.
- first_name and last_name are a unique combination.
- ONLY email and address can be null.
*/
CREATE TABLE IF NOT EXISTS emergencycontact (
id INT AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
phone BIGINT NOT NULL,
email VARCHAR(255),
address VARCHAR(255),
CONSTRAINT uq_name UNIQUE (first_name, last_name)
-- CONSTRAINT check_id CHECK (id <= 99999999). check_id cant refer to auto incremented column.
);