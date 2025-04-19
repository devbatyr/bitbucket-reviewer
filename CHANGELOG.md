# Changelog

## [1.0.0] - 2025-04-16

### Added
- 🚀 Initial release of **Bitbucket Reviewer**
- 🧪 PMD integration to analyze Java code in Bitbucket pull requests
- 🔔 Webhook endpoint to trigger analysis automatically
- 🛠️ Bitbucket REST client for fetching PR details and posting comments
- 🚫 Support for suppression rules and custom ruleset
- ⚙️ Environment-based configuration (.env support)
- ✅ Unit tests for commit SHA extraction, diff parsing, and comment publishing

## [1.1.0] - 2025-04-19

### Added
- 🔥 AI integration using Ollama for generating recommendations based on PMD violations.
- 📦 Support for custom prompt templates (`prompt-template.txt`) with dynamic placeholder replacement.
- 🌡️ Configuration of Ollama model, temperature, and timeouts via `.env` and application config.
- 🧠 New `AiRecommendationService` for centralized interaction with the Ollama API.
- 🔁 Automatic deduplication of repeated PMD violations before sending them to AI.
- 📤 Extracted and isolated logic for publishing analysis results into `publishAnalysisResult(...)`.
