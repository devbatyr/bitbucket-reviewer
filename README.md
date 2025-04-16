Bitbucket Reviewer 🤖
======================

**Bitbucket Reviewer** is an automated code analysis tool for Bitbucket Server. It leverages PMD to analyze Java code in pull requests and posts comments directly on the PR with relevant feedback.

This tool was designed as a lightweight, centralized solution to avoid integrating PMD and linters into dozens (or hundreds) of existing projects. Instead of modifying each repository's build or CI pipeline, Bitbucket Reviewer offers a single point of analysis with consistent results.

The current version provides a minimal integration — it runs PMD, formats the output, and posts results. However, the architecture allows further customization: filtering specific rules, modifying the output, or injecting custom analysis logic.

🚀 Features
-----------
- Supports Bitbucket Server (REST API v1.0)
- Integrates with PMD 7.x
- Posts violations as comments in the PR
- REST endpoint to receive webhooks
- Supports both:
    - Local development via `.env`
    - Docker (JVM and native-image)
- Configurable paths and tokens via environment variables

⚙️ Configuration
----------------
Copy `.env.example` to `.env` and update the values:

```bash
cp .env.example .env
```

```env
# Bitbucket Auth
BITBUCKET_TOKEN=base64encodedtoken
BITBUCKET_URL=https://bitbucket.example.com

# PMD Configuration
PMD_PATH=/absolute/path/to/pmd
PMD_RULESET=/absolute/path/to/ruleset.xml
PMD_SUPPRESSIONS=/absolute/path/to/suppressions.xml
```

🔧 Running Locally
------------------

```bash
./mvnw quarkus:dev
```

🐳 Docker (JVM)
---------------

```bash
docker build -t bitbucket-reviewer-jvm -f Dockerfile.jvm .
docker run --env-file .env bitbucket-reviewer-jvm
```

🐳 Docker (Native Image)
------------------------

```bash
docker build -t bitbucket-reviewer-native -f Dockerfile.native .
docker run --env-file .env bitbucket-reviewer-native
```

> ⚠️ Native image requires Linux with glibc ≥ 2.34 and `amd64` architecture.

📩 Webhook Endpoint
-------------------

Configure Bitbucket to send a webhook to:

```
POST http://your-host/api/webhook
Content-Type: application/json
```

Payload example:

```json
{
  "workspace": "your-workspace",
  "repoSlug": "your-repo",
  "prId": 42
}
```

📁 Project Structure
--------------------

```
.
├── src
│   ├── main
│   │   ├── java/com/shakenov/bitbucket/reviewer
│   │   └── resources
├── .env.example
├── Dockerfile.jvm
├── Dockerfile.native
└── README.md
```

🛡️ Tech Stack
--------------

- Quarkus 3.5
- Java 21
- PMD 7.7.0
- MicroProfile REST Client

📄 License
----------

MIT © Batyrkhan Shakenov (https://github.com/devbatyr)