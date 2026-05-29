# 🏛️ Distributed Systems Implementation

**Academic Project Submission**

**Course:** Advanced Programming and Paradigms

**Student:** Marco Cavalazzi

**Professor:** Omicini Andrea

---

## 🌐 Project Context & Overview

This repository houses the complete codebase and analytical artifacts for a capstone project focusing on **Distributed Systems**. The project simulates the core principles of distributed computing, utilizing the **JaDE Agent Development Framework** for agent interaction and messaging.

> **Required Documentation:** For a comprehensive understanding of the system's design, state machine flow, and architectural decisions, please refer to the attached formal report.
> **Key Documentation Links:**
> *   **Local Report:** `JaDE software/doc/DSProject.pdf`
> *   **External Reference:** (http://apice.unibo.it/xwiki/bin/view/Courses/SdLm1213Projects-SkiddingController)

## 📁 Directory Structure Breakdown

The repository is organized logically to separate boilerplate code, core implementation, and documentation:

*   **`JaDE software/`**: **(CORE IMPLEMENTATION)** Contains the functional source code, compiled agents, and the necessary framework dependencies (e.g., `jade.jar`, `commons-codec`). This is where all functional testing occurs.
    *   `bin/`: Contains compiled `.class` files and the `.java` source code for agents (e.g., `BatteryAgent`, `CentralAgent`).
    *   `doc/`: Contains the project report documentation files.
    *   `lib/`: Houses necessary external JAR dependencies (e.g., communication utilities).

## ✨ Core Theoretical Concepts Demonstrated

The project successfully implements and analyzes key distributed systems concepts:

1.  **Agent-Based Modeling (FIPA Compliance):** Using the JaDE framework to model independent, interacting entities (Agents).
2.  **Messaging and Interoperability:** Implementing reliable message passing and understanding the role of the Agent Communication Language (ACL) messages.
3.  **System Scaling & Communication:** Utilizing the concept of a central Agent Platform Container for managing communication within a closed distributed environment.
4.  **Fault Simulation:** The framework is designed to test resilience by simulating agent failures and network degradation.

## 🚀 Getting Started (Execution Guide)

### 1. Prerequisites
*   **JDK:** Java Development Kit (JDK) **[Specify Version]**
*   **Framework:** The JaDE framework (version details are found in `JaDE software/README.txt`).
*   **Build Tool:** Maven or Gradle (depending on project build requirements).

### 2. Build Process
Follow the instructions provided in `JaDE software/README.txt` and the build scripts within the `JaDE software/bin/` folder to compile the agents and containers.

### 3. Execution
Execute the system launch sequence using the standard JaDE command pattern, which typically involves:
```bash
java jade.Boot [options] [Agent list]
```

## 🗺️ Advanced Notes for the Reviewer

*   **Complexity:** The system's complexity lies in coordinating the `AgentPlatform` container with various agent containers, simulating real-world network topologies.
*   **Scalability:** The architecture supports expansion by adding new agent definitions and adjusting the container launch sequence.

---

### 🤝 Contribution & Academic Integrity

This project is a substantial academic submission. Any contributions are appreciated but must be accompanied by detailed documentation explaining the necessity of the change relative to the course objectives.

### 📄 License

This software is **not** licensed under a standard open-source license. It is dual-licensed under the following terms:

* **Commercial Use:** Requires a paid Commercial License. You **must** purchase a license if you intend to use this software, in any setting, or to generate revenue.