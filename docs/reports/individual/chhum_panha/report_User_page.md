# Report: Implementation of the User Page (Student Dashboard and User Profile)

## 1. Overview
This report documents the creation and wiring of the user-facing pages intended for authenticated non-admin users, specifically:
- Student Dashboard: a landing page for students after successful login.
- User Profile: a page to view and update personal information.

## 2. Objectives
- Provide a dedicated post-login page for student users.
- Enforce access control so only authenticated users with the correct role can view these pages.
- Ensure a smooth redirect after authentication based on user role.

## 3. Scope
This report covers only the user page implementation: routing, security, templates, and validation of navigation/redirects. It excludes admin and teacher dashboards, course CRUD, and enrollment functionality.

## 4. Files Added/Used
- Controllers
  - `src/main/java/edu/itc/enrollment_scheduling_system/controller/StudentController.java`
  - `src/main/java/edu/itc/enrollment_scheduling_system/controller/UserController.java`
- Security
  - `src/main/java/edu/itc/enrollment_scheduling_system/config/SecurityConfig.java`
  - `src/main/java/edu/itc/enrollment_scheduling_system/config/RoleBasedSuccessHandler.java`
- Templates
  - `src/main/resources/templates/student-dashboard.html`
  - `src/main/resources/templates/user-profile.html`
- Support
  - `src/main/java/edu/itc/enrollment_scheduling_system/repository/UserRepository.java`
  - `src/main/java/edu/itc/enrollment_scheduling_system/model/User.java`
  - `src/main/java/edu/itc/enrollment_scheduling_system/dto/UserProfileForm.java`
  - `src/main/java/edu/itc/enrollment_scheduling_system/config/DataInitializer.java` (role seeding)


## 5. Functional Design
### 5.1 Routing
- `GET /student/dashboard`
  - Purpose: Default landing page for STUDENT users after login.
  - View: `templates/student-dashboard.html`
- `GET /user/profile`
  - Purpose: Display current authenticated user’s profile details.
  - View: `templates/user-profile.html`
- `POST /user/profile`
  - Purpose: Update profile fields with server-side validation using `UserProfileForm`.

### 5.2 Access Control
- Only authenticated users can access `/student/**` and `/user/**` routes.
- Role-based access ensures:
  - STUDENT role can access student dashboard and profile.
  - ADMIN/TEACHER users are redirected to their own dashboards via the success handler.

### 5.3 Post-Login Redirection
- `RoleBasedSuccessHandler` determines destination:
  - `ROLE_ADMIN` → `/admin/dashboard`
  - `ROLE_TEACHER` → `/teacher/dashboard`
  - `ROLE_STUDENT` (default user) → `/student/dashboard`
- `SecurityConfig` registers the success handler with `formLogin().successHandler(...)`.

## 6. Implementation Details
### 6.1 Controllers
- StudentController
  - Exposes `GET /student/dashboard` to return the `student-dashboard.html` view.
  - Ensures the authenticated user context is available for UI personalization.
- UserController
  - `GET /user/profile` populates the form with current user data (via the security principal/UserRepository).
  - `POST /user/profile` validates input (`UserProfileForm`), persists allowed fields, and returns feedback messages.

### 6.2 Templates
- `student-dashboard.html`
  - Provides a welcome section and quick links (e.g., course list, enrollment).
  - Displays user-specific info using model attributes.
- `user-profile.html`
  - Shows a profile form for editable fields (e.g., name, email) and readonly fields where appropriate (e.g., username).
  - Displays validation errors and success messages.

### 6.3 Security
- `SecurityConfig`
  - Secures endpoints: permits `/login` and `/register`; requires authentication for `/student/**` and `/user/**`.
  - Registers `RoleBasedSuccessHandler` for post-login routing.
- `RoleBasedSuccessHandler`
  - Extracts user roles from `Authentication` and redirects accordingly.
- `DataInitializer`
  - Seeds roles (`ROLE_STUDENT`, `ROLE_TEACHER`, `ROLE_ADMIN`) and optional test users to validate flows.

### 6.4 Data Access
- `UserRepository` used to fetch and update the authenticated user’s record.
- `User` model provides persistent fields mapped to profile form fields with basic constraints.

## 7. Testing and Verification
### 7.1 Preconditions
- Application running on `http://localhost:8080`.
- A user with `ROLE_STUDENT` exists (from registration flow or `DataInitializer`).
- Authentication works via `/login`.

### 7.2 Scenarios
- Successful Login Redirect
  - Login as a student → expect redirect to `/student/dashboard`.
  - Verify the dashboard template renders and shows the user identity.
- Direct Access
  - Navigate to `/student/dashboard` when authenticated → page loads.
  - Navigate to `/user/profile` when authenticated → page loads and shows profile data.
- Profile Update
  - Submit profile updates on `/user/profile`.
  - Verify validation errors for invalid input.
  - Verify success message and persisted changes for valid input.
- Unauthorized Access
  - Attempt `/student/dashboard` or `/user/profile` when not authenticated → expect redirect to `/login`.
  - Login as admin/teacher → verify redirection to their dashboards, not student dashboard.

## 8. Risks and Mitigations
- Risk: Exposing fields not intended to be edited by users.
  - Mitigation: Limit editable fields in `UserProfileForm` and ignore sensitive attributes (roles, username).
- Risk: Broken redirects if role names change.
  - Mitigation: Centralize role-to-URL mapping in `RoleBasedSuccessHandler`; keep role constants consistent.

## 9. Outcome
- Student users now have a clear, protected landing page at `/student/dashboard`.
- A functional user profile page at `/user/profile` enables basic self-service account updates.
- Post-login behavior is consistent and role-aware via `RoleBasedSuccessHandler`.

## 10. Future Enhancements
- Add avatar upload and password change flow (with current password verification).
- Add CSRF tokens to profile form and confirm they are enabled in `SecurityConfig`.
- Add integration tests for role-based redirects and controller access.
- Enhance dashboard with recent courses, enrollments, and notifications.
