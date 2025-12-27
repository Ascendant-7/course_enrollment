CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'ADMIN', 'TEACHER', 'STUDENT'
);


CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    teacher_id BIGINT,
    FOREIGN KEY (teacher_id) REFERENCES users(id)
);


CREATE TABLE classrooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT,
    room_number VARCHAR(20),
    schedule_period VARCHAR(100), 
    FOREIGN KEY (course_id) REFERENCES courses(id)
);


CREATE TABLE classroom_students (
    classroom_id BIGINT,
    student_id BIGINT,
    PRIMARY KEY (classroom_id, student_id),
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id),
    FOREIGN KEY (student_id) REFERENCES users(id)
);