# Context-Aware Test Case Generation

This project automates **Cucumber test case generation** for a **Java Spring Boot-based banking application** with a **React frontend**. It utilizes a Python script powered by an **LLM (Large Language Model)** to analyze application functionality and generate **Gherkin** test cases dynamically.

## Features

- **Automated Test Case Generation**
- **Context-Aware Test Updates**
- **Supports Cucumber & Gherkin Format**
- **REST API for Test Generation**

## Project Overview

### Banking Application Functionalities

The banking system includes:

- **Account Management:** Create account
- **Transactions:** Deposit, Withdraw, Money Transfer
- **Balance Inquiry:** Check balance

### Test Case Generation Workflow

The system operates in three modes:

#### 1. Get Task Input & Check Type
- Determines whether the request is for **test generation** or **test update**.

#### 2. Generate Tests
1. **Generate Code Context** â€“ Extracts relevant functionality from source code.
2. **Parse Context Files** â€“ Reads application structure.
3. **Create Functionality Map** â€“ Maps functionalities to potential test scenarios.
4. **Generate Test Cases** â€“ Creates Cucumber-compatible test cases.
5. **Generate Test Summary** â€“ Provides an overview of generated tests.

#### 3. Update Tests
1. **Generate Code Context** â€“ Re-extracts the latest application functionality.
2. **Update Context with Existing Test Case Context** â€“ Merges new updates with previous test cases.
3. **Create Functionality Map** â€“ Updates mapping for feature changes.
4. **Find Updated Functionalities** â€“ Detects new, modified, or deprecated functionalities.
5. **Update Test Cases** â€“ Modifies test cases accordingly.
6. **Generate Test Summary** â€“ Summarizes updates.

#### 4. Other
- **Invalid Task Type** â€“ Handles incorrect requests.

## Setup & Usage

### Backend (Spring Boot)

1. Clone the repository:
   ```sh
   git clone <repo-url>
   cd <project-folder>
   ```
2. Build and run the Spring Boot application:
   ```sh
   mvn clean install  
   mvn spring-boot:run  
   ```
3. The backend API will be available at `http://localhost:8080/`.

### Frontend (React)

1. Navigate to the frontend directory:
   ```sh
   cd frontend  
   ```
2. Install dependencies and start the React app:
   ```sh
   npm install  
   npm start  
   ```

### Test Case Generation (Python LLM Script)

1. Install required dependencies:
   ```sh
   pip install -r requirements.txt  
   ```
2. Run the test case generator:
   Run the jupyter notebook _SourceControlAndTestGenerationModel_Final.ipynb
   And input the task `GENERATE_TESTS` or `UPDATE_TESTS`

## Contributing

Feel free to raise issues, fork the repository, and submit pull requests.
