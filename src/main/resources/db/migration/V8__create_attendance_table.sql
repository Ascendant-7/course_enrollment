-- Active: 1761750070668@@127.0.0.1@3306@course_enrollment
CREATE TABLE attendance (
    student_id BIGINT NOT NULL,
    classroom_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id, classroom_id),
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES users (id),
    CONSTRAINT fk_attendance_classroom FOREIGN KEY (classroom_id) REFERENCES classrooms (id)
);
