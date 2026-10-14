# Smart Committer

A lightweight, zero-dependency CLI tool that leverages local LLMs to automatically analyze code changes and generate clear, structured Git commit messages adhering to the Conventional Commits format.

## Features
* **Privacy-First:** Processes your code locally using Ollama. No code is sent to third-party cloud APIs.
* **Zero Dependencies:** Written in pure Java 11+ using the native `HttpClient`. No Maven, Gradle, or external libraries required.
* **Fast Workflow:** Replaces manual commit writing with a single `aic` command.
* **Cross-Platform:** Works on Windows, macOS, and Linux.

## Tech Stack
* **Language:** Java 11+
* **AI Engine:** Local Ollama API
* **Model:** `qwen2.5-coder` (Recommended for speed and code comprehension)

## ⚙Prerequisites
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

## Usage

Stage your changes in any Git repository as usual:
```bash
git add .
```

Run the agent:
```bash
aic
```

The script will analyze the `git diff`, connect to the local Ollama instance, and suggest a commit message:
```text
Анализ изменений...

Предлагаемый коммит:
feat: Added two new test tasks InDrive_FullStackQA and OzonBank_Easy with respective functionalities and tests

Принять этот коммит? [Y/n/edit]:
```
Press `Y` to automatically commit, or type `edit` to modify the message manually.