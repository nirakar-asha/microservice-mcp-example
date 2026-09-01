# 🤖 Microservice MCP (Model Context Protocol) Example

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://jdk.java.net/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.7-green.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring_AI-2.0.0-blue.svg)](https://spring.io/projects/spring-ai)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.1.2-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![Gradle](https://img.shields.io/badge/Gradle-8.14-0298c7.svg)](https://gradle.org/)

A production-ready reference implementation demonstrating **Model Context Protocol (MCP)** integrated into a reactive Spring Boot microservice architecture. 

This project shows how an **AI Assistant Service (MCP Client)** dynamically discovers and invokes business tools exposed by a backend **User Service (MCP Server)** over **Streamable HTTP** via a **Spring Cloud API Gateway** and **Netflix Eureka Service Discovery**, while leveraging **JToon (TOON format)** for AI token optimization and a full **OpenTelemetry Observability Stack**.

---

## 📋 Table of Contents

- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Services Overview](#-services-overview)
  - [1. Eureka Server](#1-eureka-server-port-8070)
  - [2. Gateway Server](#2-gateway-server-port-8072)
  - [3. User Service (MCP Server)](#3-user-service-mcp-server-port-8081)
  - [4. AI Assistant Service (MCP Client)](#4-ai-assistant-service-mcp-client-port-9999)
- [AI Token Optimization (JToon)](#-ai-token-optimization-jtoon)
- [Prerequisites](#-prerequisites)
- [Configuration & Environment Variables](#-configuration--environment-variables)
- [Getting Started & Startup Order](#-getting-started--startup-order)
- [Usage & Sample Queries](#-usage--sample-queries)
- [Observability Stack](#-observability-stack)
- [License](#-license)

---

## ✨ Key Features

- 🔗 **Stateless MCP over Streamable HTTP**: Asynchronous Model Context Protocol client-server communication routed seamlessly via API Gateway.
- 🧩 **Dynamic Tool Discovery**: Automatic registration of Spring AI `@Tool` methods on the server and dynamic discovery/binding by the client at runtime.
- 🌐 **Microservices Ecosystem**: Service Registration & Discovery with **Netflix Eureka** and Routing with **Spring Cloud Gateway**.
- 🗜️ **Token Efficient AI Responses**: Structured data is serialized using **JToon (TOON format)** to significantly decrease token consumption during LLM function execution.
- 💬 **Interactive Streaming Chat UI**: Web-based Thymeleaf chat frontend powered by Reactive WebFlux streaming (`Flux<String>`) and OpenAI API (`gpt-5.4-mini`).
- 📊 **Full Observability Stack**: Complete telemetry setup including OpenTelemetry Java Agent, OTEL Collector, Prometheus, Loki, Tempo, and Grafana.

---

## 🛠 Tech Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Java 17 | Core programming language |
| **Framework** | Spring Boot 4.0.7 | Microservice application framework |
| **AI Protocol** | Spring AI 2.0.0 (MCP Server & Client) | Model Context Protocol integration |
| **Cloud Infrastructure** | Spring Cloud 2025.1.2 | Eureka Discovery & Gateway Server |
| **Data Encoding** | JToon (`dev.toonformat:jtoon:1.0.9`) | Token-Oriented Object Notation (TOON) for AI token reduction |
| **Build Tool** | Gradle | Multi-module build management |
| **LLM Provider** | OpenAI API | Models such as `gpt-5.4-mini` |
| **Observability** | OpenTelemetry, Prometheus, Loki, Tempo, Grafana | Distributed tracing, log aggregation, and metrics |

---

## 📁 Project Structure

```
microservice-mcp-example/
├── build.gradle                 # Root Gradle build configuration
├── settings.gradle              # Multi-project setup definition
├── observability.md             # Detailed guide for setup of OTEL, Grafana, Loki, Tempo
├── eureka-server/               # Netflix Eureka Service Registry (Port 8070)
├── gateway-server/              # Spring Cloud Gateway Server (Port 8072)
├── user-service/                # MCP Server microservice exposing financial & user tools (Port 8081)
│   └── src/main/java/com/nirakar/example/transaction/
│       ├── controller/          # REST Endpoints
│       ├── dto/                 # Data Models (User, Transaction, Address)
│       ├── service/             # UserService & TransactionService annotated with @Tool
│       └── UserApplication.java # Configures MethodToolCallbackProvider
└── ai-assistant-service/        # MCP Client microservice providing Chat UI (Port 9999)
    └── src/main/java/com/nirakar/example/ai/assistant/
        ├── controller/          # ChatController & Streaming WebFlux endpoints
        ├── McpToolRegistry.java # AsyncMcpToolCallbackProvider client setup
        └── AIAssistantApplication.java
```

---

## ⚙️ Services Overview

### 1. Eureka Server (Port: `8070`)
- **Role**: Centralized Service Registry.
- **Key Config**: `eureka.client.registerWithEureka: false`.
- **URL**: `http://localhost:8070/`

### 2. Gateway Server (Port: `8072`)
- **Role**: Single entry point for client requests & inter-service routing.
- **Routes**:
  - `/myapp/aiassistant/**` $\rightarrow$ `lb://AI-ASSISTANT-SERVICE`
  - `/myapp/user/**` $\rightarrow$ `lb://USER-SERVICE`

### 3. User Service (MCP Server) (Port: `8081`)
- **Role**: MCP Server providing domain data and MCP tools over Streamable HTTP at `/api/mcp`.
- **Registered MCP Tools**:
  - `fetch_user(name)`: Retrieves profile details of a user by name (encoded in TOON format).
  - `fetch_n_transaction(number)`: Retrieves the last $N$ transaction records.
  - `fetch_transaction_by_id(transactionId)`: Retrieves details for a given transaction ID.
  - `fetch_transaction_by_user(userName)`: Retrieves all transaction records associated with a specific user.

### 4. AI Assistant Service (MCP Client) (Port: `9999`)
- **Role**: MCP Client & Web Application.
- **MCP Connection**: Connects asynchronously via Gateway endpoint `http://localhost:8072/myapp/user/api/mcp`.
- **User Interface**: Web-based interactive chat interface (`chat.html`) at `http://localhost:9999/` or via Gateway at `http://localhost:8072/myapp/aiassistant/`.

---

## 🗜 AI Token Optimization (JToon)

The `user-service` uses **JToon** (`dev.toonformat:jtoon`) to encode objects into **TOON (Token-Oriented Object Notation)** format before passing results back to the LLM. 

Compared to standard verbose JSON payloads, TOON reduces whitespace and structural overhead, resulting in significantly **lower token consumption and faster response times** when AI models process domain tool outputs.

---

## 🔑 Configuration & Environment Variables

Configure environment variables before launching `ai-assistant-service`:

| Environment Variable | Description | Default / Example |
| :--- | :--- | :--- |
| `OPENAI_API_KEY` | Your OpenAI API key (**Required**) | `sk-proj-...` |
| `OPENAI_BASE_URL` | Custom OpenAI base URL | `https://api.openai.com/v1` |
| `OPENAI_CHAT_MODEL` | OpenAI Chat model | `gpt-5.4-mini` |
| `STREAMABLE_HTTP_CONNECTIONS_URL` | Gateway URL for User MCP Server connection | `http://localhost:8072/myapp/user` |

---

## 🚀 Getting Started & Startup Order

### Prerequisites
- **JDK 17** or higher installed and configured in `JAVA_HOME`.
- An active **OpenAI API Key**.

### Startup Sequence

To ensure proper service registration and MCP client connection, start the microservices in the following exact order:

1. **Start Eureka Server**
   ```bash
   ./gradlew :eureka-server:bootRun
   ```
   *Verify Eureka Dashboard at `http://localhost:8070/`*

2. **Start Gateway Server**
   ```bash
   ./gradlew :gateway-server:bootRun
   ```
   *Runs on port `8072`*

3. **Start User Service (MCP Server)**
   ```bash
   ./gradlew :user-service:bootRun
   ```
   *Runs on port `8081` and registers MCP endpoint at `/api/mcp`*

4. **Start AI Assistant Service (MCP Client)**
   *(Ensure `OPENAI_API_KEY` is set in your terminal)*
   ```bash
   export OPENAI_API_KEY="your-openai-api-key"
   ./gradlew :ai-assistant-service:bootRun
   ```
   *Runs on port `9999`*

---

## 💬 Usage & Sample Queries

### 1. Web Chat Interface
Navigate to `http://localhost:9999/` (or `http://localhost:8072/myapp/aiassistant/`) in your browser to open the chat interface.

### 2. REST / Streaming Endpoint
You can interact directly via the streaming chat endpoint:
```bash
curl -N "http://localhost:8072/myapp/aiassistant/chat?message=give%20me%20last%203%20transactions"
```

### 3. Example Prompts
Try asking the AI Assistant queries that trigger the backend MCP tools:

- 👤 **Fetch User Profile**:
  > *"give me 'John Smith' employee details"*
- 💳 **Fetch Recent Transactions**:
  > *"give me last 3 transactions"*
- 🔍 **Fetch Transactions by User**:
  > *"show me transactions for Jane Doe"*
- 🧾 **Fetch Transaction by ID**:
  > *"get details for transaction TXN1005"*

---

## 📊 Observability Stack

The repository includes documentation for setting up an OpenTelemetry observability stack. Refer to [`observability.md`](file:///d:/dev-workspace/microservice-mcp-example/observability.md) for full instructions.

### Stack Overview
- **OpenTelemetry Java Agent**: Automatically instruments JVM services for metrics, logs, and traces.
- **Prometheus** (Port `9090`): Metrics scraper & storage.
- **Loki** (Port `3100`): Log aggregation system.
- **Tempo** (Port `3200`): Distributed tracing backend.
- **OTEL Collector** (Ports `14317` / `14318`): Receives OTLP data and routes to Prometheus, Loki, and Tempo.
- **Grafana**: Unified dashboard for visualization.

---

## 📄 License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.
