# Smart Committer

A lightweight, zero-dependency CLI tool that leverages local LLMs to automatically analyze code changes and generate clear, structured Git commit messages adhering to the Conventional Commits format.

## Features

* **Privacy-First:** Processes your code locally using Ollama. No code is sent to third-party cloud APIs.
* **Zero Dependencies:** Written in pure Java 11+ using the native `HttpClient`. No Maven, Gradle, or external libraries required.
* **Fast Workflow:** Replaces manual commit writing with a single `aic` command.
* **Customizable:** Easily configure the AI model, API endpoint, and output language.
* **Cross-Platform:** Works on Windows, macOS, and Linux.

## Tech Stack

* **Language:** Java 11+
* **AI Engine:** Local Ollama API
* **Model:** `qwen2.5-coder` (Default, customizable)

## Prerequisites

1. [Java 11 or higher](https://adoptium.net/) installed.
2. [Ollama](https://ollama.com/) installed and running.
3. A code-focused local model pulled via Ollama:
   ```bash
   ollama run qwen2.5-coder
   ```

## Installation

**1. Clone the repository**
```bash
git clone https://github.com/YourUsername/smart-committer.git
cd smart-committer
```

**2. Setup Global Alias**

**For Windows:**
Add the cloned directory path to your system's `PATH` environment variable so the `aic.bat` script can be executed from anywhere.

**For Linux / macOS:**
Make the shell script executable and link it to your bin directory:
```bash
chmod +x aic
sudo ln -s $(pwd)/aic /usr/local/bin/aic
```

## Configuration

On the first run, the script will automatically generate a `config.properties` file in the project's root directory (based on `config.example.properties`). You can edit this file to customize the tool's behavior:

```properties
# Local or remote Ollama API endpoint
OLLAMA_URL=http://localhost:11434/api/generate

# The LLM model to use (e.g., qwen2.5-coder, llama3, gemma)
MODEL=qwen2.5-coder

# The output language for the commit message (e.g., English, Russian)
LANGUAGE=English
```
*Note: The `config.properties` file is ignored by Git to keep your local preferences private and prevent repository conflicts.*

## Usage

Stage your changes in any Git repository as usual:
```bash
git add .
```

Run the agent:
```bash
aic
```

The script will analyze the `git diff`, connect to your configured Ollama instance, and suggest a commit message:
```text
Анализ изменений (Модель: qwen2.5-coder, Язык: English)...

Предлагаемый коммит:
feat: add configuration file support and update README

Принять этот коммит? [Y/n/edit]:
```

**Interactive Prompts:**
* **`Y` (or simply press Enter):** Accepts the suggestion and automatically executes the `git commit` command.
* **`n` (or any unlisted key):** Cancels the process completely. Your files remain staged, allowing you to run `aic` again to generate a new variation if desired.
* **`edit`:** Pauses the process, prompting you to enter a custom text. You can copy the suggested message, modify it (e.g., append a Jira ticket number), and hit Enter to finalize the commit.