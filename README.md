# Ace's Springboot Code Generator
[![version](https://img.shields.io/github/v/tag/upsxace/aces-springboot-code-gen?label=version)](https://github.com/upsxace/aces-springboot-code-gen/releases)


A lightweight REST API that converts Postgres SQL statements into Spring Boot `@Entity` classes and Repository interfaces.

> 🏗️ Designed to accelerate backend development by generating JPA entities and repositories from your SQL migration scripts.

## 🚀 Features

- 🏛️ Parses Postgres SQL `CREATE TABLE` and `ALTER TABLE` statements
- 🔍 Detects:
  - Table names
  - Column names and types
- 🔧 Ignores unrelated SQL statements
- ⚙️ Generates:
  - Java Spring Boot `@Entity` classes
  - Repository interfaces (`JpaRepository`)
- 📦 Returns generated code as JSON response
<!--
  Detects:
  - Primary keys
  - Foreign keys (relationships)
-->
## 🗺️ Project Architecture
UML diagrams and detailed design documentation are available in the /docs folder.
- [Documentation](./docs)

## 📡 API Endpoint

| Method | URL               |
|--------|-------------------|
| POST   | `/sql-to-code`    |

### 🔸 Request

- **Content-Type:** `application/json`
- **Body:**

```json
{
  "sql": "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255));"
}
```

### 🔸 Response Example
```json
[
  {
    "table": "users",
    "entity": "@Entity\n@Table(name = \"users\")\npublic class User {\n    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    @Column(name = \"id\")\n    private Long id;\n\n    @Column(name = \"name\")\n    private String name;\n}",
    "repository": "public interface UserRepository extends JpaRepository<User, Long> {}"
  }
]
```

## 🛠️ Installation & Usage

### 🔥 Run with Jar
Download the latest version in [releases](https://github.com/UPSxACE/aces-springboot-code-gen/releases)
```bash
# Run in production mode
SPRING_PROFILES_ACTIVE=prod java -jar ./aces-springboot-code-gen.jar --server.port=8080

# Run in development mode (with access to swagger-ui)
SPRING_PROFILES_ACTIVE=dev java -jar ./aces-springboot-code-gen.jar --server.port=8080
```

## 🌐 Access the API
Once running, the API will be available at:

```bash
http://localhost:8080/sql-to-code
```

## 🚀 Development Setup

Follow these steps to set up the project for local development.

1. Clone the repository
```bash
git clone https://github.com/your-username/aces-springboot-code-gen.git
cd aces-springboot-code-gen
```
2. Copy the .env.example file(rename it to .env) and configure it
3. Run the app locally with the help of your IDE or using the CLI
```bash
# linux/macos
./mvnw spring-boot:run

# windows
./mvnw.cmd spring-boot:run
```
4. Once running, you’ll be able to access the Swagger UI at:
```bash
http://localhost:8080/swagger-ui/index.html
```

## 📦 Tech Stack
- Java 24
- Spring Boot
- Maven

## 🔭 Roadmap
Future features planned:
- 🪲 Show the line/statement where parsing fails
- 🔄 Generate DTO mappers
- 🗺️ Return the in-memory tree as JSON snapshot
- ♻️ Accept a tree snapshot, modify it with additional SQL, and regenerate code
- 🕵️ Return a detailed explanation of how each SQL statement was parsed

## 🤝 Contributing
Contributions are welcome! Please open an issue or pull request.

## 📜 License
This project is licensed under the [MIT License](LICENSE).

## ✨ Acknowledgments
- Built with ❤️ by Ace