# Ansible Overview

## What is Ansible?

[Ansible](https://www.ansible.com/) is an open-source **IT automation tool** developed by Red Hat.
It lets you automate repetitive tasks — such as provisioning servers, deploying applications, and managing
configuration — using simple, human-readable YAML files.

Ansible is **agentless**: it connects to target machines over SSH (Linux/macOS) or WinRM (Windows) and
requires no additional software installed on those machines.

---

## Key Concepts

| Concept | Description |
|---|---|
| **Playbook** | A YAML file that defines a set of tasks to run on one or more hosts. This is the main unit of work in Ansible. |
| **Inventory** | A file (or dynamic source) that lists the target hosts Ansible will manage, optionally grouped (e.g. `web`, `db`). |
| **Role** | A reusable, self-contained unit of tasks, variables, templates, and handlers, organized in a standard directory layout. |
| **Task** | A single action inside a playbook (e.g. install a package, copy a file, restart a service). |
| **Handler** | A special task triggered only when another task reports a change (e.g. restart Nginx only if its config changed). |
| **Module** | A built-in or custom plugin that executes a specific action on a host (e.g. `apt`, `copy`, `service`, `mysql_db`). |
| **Variable** | A named value used to parameterize playbooks and roles, defined in files, inventory, or at the command line. |
| **Vault** | Ansible's built-in secret management — encrypts sensitive values (passwords, API keys) stored in YAML files. |

---

## How Ansible Could Be Used in This Repository

This project is a **Spring Boot** course-enrollment and classroom-scheduling system backed by a MySQL database
and managed with Flyway migrations.  Below are concrete ways Ansible could automate the infrastructure.

### 1. Provision the Database Server

An Ansible playbook can install and configure MySQL on a fresh server, create the application database and user,
and apply the correct privileges — replacing manual `mysql` commands.

```yaml
# playbooks/setup_db.yml
- name: Provision MySQL for course_enrollment
  hosts: db
  become: true
  roles:
    - role: geerlingguy.mysql   # community role from Ansible Galaxy
      vars:
        mysql_databases:
          - name: course_enrollment
        mysql_users:
          - name: app_user
            password: "{{ vault_db_password }}"
            priv: "course_enrollment.*:ALL"
```

### 2. Deploy the Application

After a successful Maven build, a playbook can copy the JAR to the server, write the
`application.properties` file from a template, and (re)start the Spring Boot service.

```yaml
# playbooks/deploy_app.yml
- name: Deploy Spring Boot application
  hosts: app
  become: true
  vars:
    app_version: "0.0.1-SNAPSHOT"   # set via -e app_version=x.y.z at deploy time
  tasks:
    - name: Copy JAR to server
      copy:
        src: "target/course_enrollment-{{ app_version }}.jar"
        dest: /opt/course_enrollment/app.jar

    - name: Render application properties
      template:
        src: templates/application.properties.j2
        dest: /opt/course_enrollment/application.properties
      notify: Restart app service

  handlers:
    - name: Restart app service
      service:
        name: course_enrollment
        state: restarted
```

### 3. Run Flyway Migrations

A playbook can trigger Flyway migrations as part of the deployment pipeline, ensuring the database schema
is always in sync with the application version.

```yaml
- name: Run Flyway migrations
  hosts: app
  vars:
    flyway_version: "10.0.0"   # pin to the version used by the project
  tasks:
    - name: Execute Flyway migrate
      command: >
        java -jar /opt/flyway/flyway-{{ flyway_version }}/flyway
        -url=jdbc:mysql://{{ db_host }}/course_enrollment
        -user={{ db_user }}
        -password={{ vault_db_password }}
        migrate
```

### 4. Manage Environment Configuration Securely

Sensitive values (database passwords, JWT secrets) are stored in an **Ansible Vault**-encrypted file and
referenced as variables, so they are never committed as plain text.

```bash
# Create an encrypted secrets file
ansible-vault create group_vars/all/vault.yml

# Run a playbook using the vault password
ansible-playbook playbooks/deploy_app.yml --ask-vault-pass
```

---

## Suggested Directory Layout

If you add Ansible automation to this repository, the following structure is recommended:

```
ansible/
├── inventory/
│   ├── hosts.ini          # Static inventory (dev / staging / prod groups)
│   └── group_vars/
│       └── all/
│           ├── vars.yml   # Non-sensitive variables
│           └── vault.yml  # Ansible Vault encrypted secrets
├── playbooks/
│   ├── setup_db.yml       # Provision and configure MySQL
│   ├── deploy_app.yml     # Deploy the Spring Boot JAR
│   └── run_migrations.yml # Execute Flyway migrations
└── roles/
    └── spring_boot_app/   # Reusable role for the application
        ├── tasks/
        ├── handlers/
        └── templates/
```

---

## Further Reading

- [Ansible Documentation](https://docs.ansible.com/)
- [Ansible Galaxy](https://galaxy.ansible.com/) — community roles and collections
- [Ansible Vault](https://docs.ansible.com/ansible/latest/vault_guide/index.html) — secrets management
- [Spring Boot Deployment Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html)
