CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR (14) NOT NULL UNIQUE,
  email VARCHAR (255) NOT NULL UNIQUE,
  first_name VARCHAR (50) NOT NULL,
  last_name VARCHAR (50) NOT NULL,
  bio TEXT,
  phone VARCHAR (20),
  address VARCHAR (255),
  password_hash VARCHAR (255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  isActive BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT chk_users_username_length CHECK (
    char_length (username) BETWEEN 6 AND 14
  ),
  -- CONSTRAINT chk_users_username_format CHECK (username REGEXP '^[A-Za-z0-9._-]{6,14}$'),
  -- -- RFC 5322
  -- CONSTRAINT chk_users_email_format CHECK (
  --   email REGEXP '^[A-Za-z0-9]+([._%+-][A-Za-z0-9]+)*@[A-Za-z0-9]+([.-][A-Za-z0-9]+)*\.[A-Za-z]{2,}$'
  -- ) CONSTRAINT chk_users_first_name_format CHECK (first_name REGEXP "^[A-Za-z'-]+$"),
  -- CONSTRAINT chk_users_last_name_format CHECK (first_name REGEXP "^[A-Za-z'-]+$"),
  -- CONSTRAINT chk_users_phone_format CHECK (phone REGEXP '^[+0-9 ]*$')
);
CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR (10) NOT NULL UNIQUE
);
create table permissions ();
create table roles_permissions ();
CREATE TABLE users_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (role_id) REFERENCES roles (id)
);
CREATE TABLE courses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR (10) NOT NULL UNIQUE,
  name VARCHAR (50) NOT NULL,
  description TEXT,
  credits TINYINT NOT NULL,
  capacity INT NOT NULL,
  isActive BOOLEAN NOT NULL DEFAULT TRUE CONSTRAINT chk
);
create table enrollments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON
UPDATE CURRENT_TIMESTAMP,
  Enrollment
);
CREATE TABLE classrooms (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id BIGINT NOT NULL,
  code VARCHAR (10) NOT NULL UNIQUE,
  startDate DATE NOT NULL,
  endDate DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_classroom_course FOREIGN KEY (course_id) REFERENCES courses (id)
);
create table attendance ();
create table schedule ();