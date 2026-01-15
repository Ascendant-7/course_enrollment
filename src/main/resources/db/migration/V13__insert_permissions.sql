-- Insert default permissions
INSERT INTO permissions (name, description) VALUES
('USER_MANAGE', 'Manage users (create, edit, delete, approve)'),
('COURSE_MANAGE', 'Manage courses (create, edit, delete)'),
('COURSE_VIEW', 'View courses'),
('COURSE_ENROLL', 'Enroll in courses'),
('CLASSROOM_MANAGE', 'Manage classrooms and assignments'),
('ENROLLMENT_MANAGE', 'Manage enrollments'),
('REPORT_VIEW', 'View reports and analytics'),
('SYSTEM_ADMIN', 'Full system administration');