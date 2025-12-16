# Course Enrollment and Classroom Scheduling System Project

**Welcome** to our Group 3 project. We are 4th year students of the GIC department, working on a course enrollment and classroom scheduling system. Let's introduce the members:

| Members        | ID        | Roles       |
| -------------- | --------- | ----------- |
| Ang Panha      | e20221707 | Team Leader |
| Et Anchhy      | e20220608 | Member      |
| Chhon Pheakdey | e2022xxxx | Member      |
| Chi Savmoeng   | e2022xxxx | Member      |

## Project Requirements

Our project uses the **Spring Boot Framework** as the foundation.

- **Build Tool**: Maven
- **Java Version**: 21 LTS
- **Springboot Version**: 3.x
- **Spring Initializr Dependencies**:
  - Spring Web
  - Thymeleaf
  - Spring Data JPA
  - Spring Security
  - Validation
  - Mysql Driver
  - Flyway
  - Lombok
  - Springboot Dev Tools

## Getting Started For Collaborators

### 1. Clone the repo

```bash
git clone https://github.com/Ascendant-7/course_enrollment.git
cd course_enrollment
```

---

### 2. Create your own branch (mandatory)

```bash
git checkout -b feature/<your-name>
```

Example:

```bash
git checkout -b feature/panha
```

---

### 3. Run the project

```bash
./mvnw spring-boot:run
```

or from IDE:

- Run `Application.java`

Then open:

```bash
http://localhost:8080/login
```

---

### 4. Login credentials (important)

Spring Security default behavior:

- Username: **anything**
- Password: **the generated password in console**

Example:

```bash
Using generated security password: xxxxxxxx
```

Later, this will be replaced by real users.

---

## Collaboration Rules

### Branching & Workflow

1. The `main` branch is **protected**.
   Direct pushes are **not allowed**. All changes must go through a Pull Request.

2. Every member must work on **their own feature branch**, named after themselves:

   ```bash
   feature/<name>
   ```

3. All work must be merged into `main` **via Pull Request**.
   Only the **team leader** is allowed to approve and merge PRs.

---

### Contribution Guidelines

- One task = one GitHub issue
- Commit often with **clear, meaningful commit messages**
- Push **only** your own branch:

  ```bash
  git push origin feature/<name>
  ```

- Open a **Pull Request → `main`**
- Do **not** merge your own PR
- Do **not** rebase `main`
- Wait for leader review and merge

---

### Do Not Modify

- The `main` branch
- Project setup or configuration files unless explicitly required by your task
- Security configuration unless you are assigned to the Security task

---

### If Something Breaks

Do **not** fix it silently.

Instead:

1. Comment on the related GitHub issue, or
2. Ask in the group chat, or
3. Open a **draft Pull Request** explaining the problem

---

That’s the process.
Follow it strictly.
