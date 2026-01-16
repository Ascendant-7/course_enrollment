-- Active: 1761750070668@@127.0.0.1@3306@course_enrollment
CREATE TABLE courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    credits TINYINT NOT NULL,
    capacity INT NOT NULL,
    description TEXT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
)