-- Assign permissions to roles
-- ADMIN gets all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN';

-- TEACHER permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_TEACHER'
AND p.name IN ('COURSE_MANAGE', 'COURSE_VIEW', 'CLASSROOM_MANAGE', 'ENROLLMENT_MANAGE', 'REPORT_VIEW');

-- STUDENT permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_STUDENT'
AND p.name IN ('COURSE_VIEW', 'COURSE_ENROLL');

-- GUEST permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_GUEST'
AND p.name IN ('COURSE_VIEW');