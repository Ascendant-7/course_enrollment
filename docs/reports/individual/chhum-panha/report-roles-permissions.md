# Roles-Permissions System Implementation Report

## Project Overview
**Course Enrollment and Scheduling System**

## Implementation Date

## Objective
Implement a comprehensive roles-permissions system to replace the basic role-based access control with fine-grained permission management.

## Previous System
- Simple role-based access control (RBAC)
- 4 roles: ROLE_GUEST, ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN
- URL-based authorization only
- Single role per user
- No method-level security

## New System Architecture

### 1. Database Schema Changes

#### New Tables Created:
- **permissions**: Stores individual permissions
  - id (INT, PRIMARY KEY, AUTO_INCREMENT)
  - name (VARCHAR(50), UNIQUE, NOT NULL)
  - description (VARCHAR(255))

- **role_permissions**: Junction table for many-to-many relationship
  - role_id (INT, FOREIGN KEY → roles.id)
  - permission_id (INT, FOREIGN KEY → permissions.id)
  - PRIMARY KEY (role_id, permission_id)

#### Updated Tables:
- **roles**: Added permissions relationship
- **users**: Enhanced with proper constructors

### 2. Permission Definitions

| Permission Name | Description |
|----------------|-------------|
| USER_MANAGE | Manage users (create, edit, delete, approve) |
| COURSE_MANAGE | Manage courses (create, edit, delete) |
| COURSE_VIEW | View courses |
| COURSE_ENROLL | Enroll in courses |
| CLASSROOM_MANAGE | Manage classrooms and assignments |
| ENROLLMENT_MANAGE | Manage enrollments |
| REPORT_VIEW | View reports and analytics |
| SYSTEM_ADMIN | Full system administration |

### 3. Role-Permission Assignments

#### ADMIN Role
- **Permissions**: All permissions (USER_MANAGE, COURSE_MANAGE, COURSE_VIEW, COURSE_ENROLL, CLASSROOM_MANAGE, ENROLLMENT_MANAGE, REPORT_VIEW, SYSTEM_ADMIN)
- **Access Level**: Full system access

#### TEACHER Role
- **Permissions**: COURSE_MANAGE, COURSE_VIEW, CLASSROOM_MANAGE, ENROLLMENT_MANAGE, REPORT_VIEW
- **Access Level**: Course and classroom management, enrollment oversight

#### STUDENT Role
- **Permissions**: COURSE_VIEW, COURSE_ENROLL
- **Access Level**: View courses and enroll in them

#### GUEST Role
- **Permissions**: COURSE_VIEW
- **Access Level**: Browse courses only

### 4. Code Changes

#### New Classes Created:
1. **Permission.java** - JPA entity for permissions
2. **PermissionRepository.java** - Data access layer
3. **CustomPermissionEvaluator.java** - Spring Security permission evaluation

#### Modified Classes:
1. **Role.java** - Added permissions relationship
2. **SecurityConfig.java** - Enabled method security, registered permission evaluator
3. **UserManagementController.java** - Added permission checks
4. **AdminCourseController.java** - Added permission checks
5. **AdminController.java** - Added permission checks

#### Database Migrations:
- V11__create_permissions_table.sql
- V12__create_role_permissions_table.sql
- V13__insert_permissions.sql
- V14__assign_role_permissions.sql

### 5. Security Implementation

#### Method-Level Security
```java
@PreAuthorize("hasPermission('COURSE_MANAGE')")
public String createCourse(@ModelAttribute Course course) {
    // Only users with COURSE_MANAGE permission
}
```

#### Permission Evaluation Logic
- Custom evaluator loads user by username from database
- Checks if user has any role containing the required permission
- Supports dynamic permission checking

#### Backward Compatibility
- Existing URL-based security remains functional
- Role-based login redirection unchanged
- Existing user interface preserved
