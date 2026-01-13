-- Active: 1761750070668@@127.0.0.1@3306@course_enrollment
CREATE TABLE classrooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_id BIGINT NOT NULL,
    course_id INT NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_classroom_teacher FOREIGN KEY (teacher_id) REFERENCES users (id),
    CONSTRAINT fk_classroom_course FOREIGN KEY (course_id) REFERENCES courses (id)
)