# 🏎️ Grand Prix Companion

**Grand Prix Companion** is a Java Swing desktop application designed to display real-time race telemetry, driver standings, and tire strategy progression parsed directly from event CSV data. It includes a user authentication system for secure login and session management.

**Project by**
* Nur A Afsanur Shreya
* Yusha Nurullah Ayan
* Tawfique Omar Tazwar
---

## ✨ Features

* **User Authentication:** 
  * Simple user registration and login workflow.
  * Local file-based credential storage
  * Password hashing and session context management.
   
* **Race Telemetry & Live Standings:**
  * Real-time race simulation driven by CSV telemetry parsing (`race_data.csv`).
  * Dynamic leaderboards tracking driver positions, lap times, tire compounds, and gaps.
  * Live lap progression and tire strategy simulation (pit stops, compound changes).

* **Modular Swing Architecture:**
  * Clean MVC-inspired component separation using Swing panels (`WelcomePanel`, `LoginFormPanel`, `RegisterFormPanel`, `RaceUI`).

---

## 🛠️ Tech Stack & Requirements

* **Language:** Java 8 or higher
* **GUI Framework:** Java Swing (`javax.swing`)
* **Persistence:** Local text & CSV file handling
* **Build Tool / IDE:** Compatible with standard JDK (`javac` / `java`), IntelliJ IDEA, Eclipse, or NetBeans.

---

## 📁 Project Structure

```text
com.gpcompanion
├── App.java                   # Main entry point and screen flow controller
├── auth/
│   ├── AuthController.java    # Handles UI interaction for auth
│   ├── AuthService.java       # Credential validation & password hashing
│   ├── FileUserCredentialStore.java # Reads/writes user credentials to users.txt
│   ├── SessionContext.java    # Manages currently logged-in user state
│   └── UserAccount.java       # User entity model
├── race/
│   ├── LapRecord.java         # Data model for individual lap entries
│   ├── RaceEngine.java       # State machine managing lap progression & standings
│   └── RaceLoader.java        # CSV parser for race data
├── exceptions/
│   └── RaceDataException.java # Custom exception handling for corrupt/missing race files
└── ui/
    ├── LoginFormPanel.java    # UI for user login
    ├── RegisterFormPanel.java # UI for user registration
    ├── WelcomePanel.java      # Landing screen UI
    └── RaceUI.java            # Main race telemetry dashboard UI
