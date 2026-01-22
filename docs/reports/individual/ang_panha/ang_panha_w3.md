# Individual Project Report - Ang Panha

**Week**: 3-4\
**Period**: 28 Dec 2025 - 6 Jan 2026

**Group**: I4 GIC C\
**Team**: 3\
**Project**: Course Enrollment and Scheduling System

## Weekly Summary

This week (week 4), I took coding in order to rework the project entirely. I rework the flyway, model, repository, service, and controller. A few teammates help me with this. The thymeleaf templates haven't been reworked so most routes fail. Week 3, I mainly deal with task assignment and integrating the work.

## Task Log

### Task 1: PlantUML Diagrams

- **status**: in progress
- **description**: create diagrams to plan the rework
- **proof**: [PlantUML Diagrams](https://github.com/Ascendant-7/course_enrollment/commit/7d27b25c6d56073603c520a7501b404e2a0bd440)
- **reflection**: used diamond decision-making nodes, clasroom assignment looks aspirational, need more diagrams.

### Task 2: Flyway Rework

- **status**: done
- **description**: rework existing flyway migrations with script and manually. set tasks for new migrations to members. implemented .env variables. used custom flyway scripts coupled with .env, because default flyway scripts require boilerplate-like options.
- **proof**:
  - [Flyway Rework](https://github.com/Ascendant-7/course_enrollment/commit/232b8b34fbfc6a6e5c1dbdbacac8121ecacc2a75)
  - [Flyway Rework 2 + Scripting](https://github.com/Ascendant-7/course_enrollment/commit/53a18c93b80adfc0c55391e863738b5b3f5dd6f0)
- **reflection**: learning .env is worth it. existing schema are reworked and newer planned schema are given as tasks to member, though only some manage to complete it. I also liked learning on how to make scripts and find them very useful.

### Task 3: Model Rework

- **status**: done
- **description**: rework the entity model, implement lombok, remove unnecessary validations, implement transience, split user entity into two (profile & account) for security, add a string util.
- **proof**:
  - [User Entity Rework](https://github.com/Ascendant-7/course_enrollment/commits/main/)
  - [Role Entity Rework](https://github.com/Ascendant-7/course_enrollment/commit/916990c2f42bfa1c0d9c5f22bbe8e73a40c268a9)
  - [Multiple Entities Rework + Migration Rework](https://github.com/Ascendant-7/course_enrollment/commit/6dc07204f9c82bab3f5b94dc3ea90ae75df76594)
  - [User Split - Creation of Account and Profile Entity + Enrollment Entity Addition](https://github.com/Ascendant-7/course_enrollment/commit/bcebc17512f656ac7c39b9fb0f8e22e15fa2cff3)
  - [Multiple Entities Rework 2 + User Split Implementation (Profile & Account)](https://github.com/Ascendant-7/course_enrollment/commit/36cbef444fd2c1f7daaa65ef46e25d17c045946a)
  - [Multiple Entities Rework 3 + Util](https://github.com/Ascendant-7/course_enrollment/commit/48786613c6366010d470cb0ea686bc5098976556)
- **reflection**: learning .env is worth it. existing schema are reworked and newer planned schema are given as tasks to member, though only some manage to complete it. I also liked learning on how to make scripts and find them very useful. I also learn how to sign commits, add code owner, and follow commit message convention.

### Task 4: Service Rework

- **status**: done
- **description**: rework the service layer,
- **proof**:
  - [Multiple Service Rework](https://github.com/Ascendant-7/course_enrollment/commit/1796130785be64374148c842976ea567e08ab9cd)
  - [Transfer 2](https://github.com/Ascendant-7/course_enrollment/commit/9cfda1c285c7cd64b70b09f1bf61ad91db0a38e9)
- **reflection**: my commit looks clean, I sure hope nothing will happen.

### Task 5: Config Rework

- **status**: done
- **description**: rework the config.
- **proof**:
  - [Transfer to Laptop](https://github.com/Ascendant-7/course_enrollment/commit/5213786fa7be8de471a6087c0cbb4dca1b670f46)
  - [Transfer 2](https://github.com/Ascendant-7/course_enrollment/commit/9cfda1c285c7cd64b70b09f1bf61ad91db0a38e9)
- **reflection**: nevermind, I guess my commit now contains everything. I learn `git stash` to prevent future troubles, but the damage is done. goodbye, my clean commit history.

### Task 6: Controller Rework

- **status**: done
- **description**: rework the controllers.
- **proof**:
  - [Transfer to Laptop](https://github.com/Ascendant-7/course_enrollment/commit/5213786fa7be8de471a6087c0cbb4dca1b670f46)
  - [Transfer 2](https://github.com/Ascendant-7/course_enrollment/commit/9cfda1c285c7cd64b70b09f1bf61ad91db0a38e9)
- **reflection**: merging my teammate's commits blindly really bit me in the back. To think they made so many controllers for each module.

### Task 7: DTO Rework

- **status**: done
- **description**: rework the DTO.
- **proof**:
  - [Transfer to Laptop](https://github.com/Ascendant-7/course_enrollment/commit/5213786fa7be8de471a6087c0cbb4dca1b670f46)
  - [Transfer 2](https://github.com/Ascendant-7/course_enrollment/commit/9cfda1c285c7cd64b70b09f1bf61ad91db0a38e9)
- **reflection**: at least I'm proud of learning and using Record classes. It helps a bunch, and works well as DTOs.

### Task 8: Repository Rework

- **status**: done
- **description**: rework the DTO.
- **proof**:
  - [Transfer to Laptop](https://github.com/Ascendant-7/course_enrollment/commit/5213786fa7be8de471a6087c0cbb4dca1b670f46)
  - [Transfer 2](https://github.com/Ascendant-7/course_enrollment/commit/9cfda1c285c7cd64b70b09f1bf61ad91db0a38e9)
- **reflection**: this has got to be the most confusing one. Either I use automatic parsing or use JSQL. I can't decide. I picked the easiest to implement.

### Task 9: Security Rework

- **status**: done
- **description**: rework the Security.
- **proof**: [Transfer 2](https://github.com/Ascendant-7/course_enrollment/commit/9cfda1c285c7cd64b70b09f1bf61ad91db0a38e9)
- **reflection**: security was initially good, rework it, didn't work, revert, rework again.
