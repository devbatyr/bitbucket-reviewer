Bitbucket Reviewer 🤖
======================

**Bitbucket Reviewer** is an automated code analysis tool for Bitbucket Server. It leverages PMD to analyze Java code in pull requests and provides AI-powered recommendations with contextual suggestions for improvements — all posted directly as comments on the pull request.

This tool was designed as a lightweight, centralized solution to avoid integrating PMD and linters into dozens (or hundreds) of existing projects. Instead of modifying each repository's build or CI pipeline, Bitbucket Reviewer offers a single point of analysis with consistent results.

🚀 Features
-----------
- Supports Bitbucket Server (REST API v1.0)
- Integrates with PMD 7.x
- AI-powered recommendations using **Ollama** (e.g., DeepSeek models)
- Posts formatted violations and suggestions as **PR comments**
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

# Ollama API
OLLAMA_API_URL=http://ollama.host:11434
OLLAMA_MODEL=deepseek-coder:6.7b
OLLAMA_TEMPERATURE=0.2
OLLAMA_PROMPT_PATH=/absolute/path/to/prompt-template.txt
OLLAMA_CONNECT_TIMEOUT=5000
OLLAMA_READ_TIMEOUT=60000

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

🛡️ Tech Stack
--------------

- Quarkus 3.5
- Java 21
- PMD 7.7.0
- MicroProfile REST Client

📄 License
----------

MIT © Batyrkhan Shakenov (https://github.com/devbatyr)