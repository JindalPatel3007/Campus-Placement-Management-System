# Campus Placement Management System

A complete, professional, console-based Campus Placement Management System written in Core Java with MySQL database persistence using JDBC.

## 📌 Project Overview
This project is engineered for 2nd-year/3rd-year Computer Engineering students looking for a comprehensive portfolio project to showcase solid OOP design, relational database integration (JDBC), structured DAO architecture, and core data structures without reliance on modern enterprise frameworks like Spring Boot or Hibernate.

---

## 🛠️ Tech Stack & Requirements
* **Language:** Java (JDK 8 or higher)
* **Database:** MySQL Server (XAMPP / phpMyAdmin / Standalone MySQL Workbench)
* **JDBC Driver:** MySQL Connector/J (`mysql-connector-j-8.x.x.jar`)
* **IDE:** IntelliJ IDEA (Community or Ultimate Edition) / Eclipse / VS Code

---

## 🏗️ Architecture & Project Structure
```text
CampusPlacementManagementSystem/
│
├── database.sql                 # MySQL Schema definition & sample test data
├── README.md                    # Project documentation
│
└── src/
    ├── Main.java                # Console Application Entry Point & Menu Engine
    ├── database/
    │   └── DatabaseManager.java # Centralized JDBC Connection & Factory Provider
    ├── model/
    │   ├── Student.java         # Student Domain Model
    │   ├── Company.java         # Company Domain Model
    │   ├── PlacementDrive.java  # Placement Drive Domain Model
    │   └── Application.java    # Student Job Application Domain Model
    ├── dao/
    │   ├── StudentDAO.java      # Student CRUD Database Operations
    │   ├── CompanyDAO.java      # Company CRUD Database Operations
    │   ├── PlacementDriveDAO.java # Placement Drive Database Operations
    │   └── ApplicationDAO.java  # Application & Selection DB Operations
    ├── service/
    │   ├── EligibilityService.java # Rule evaluation for drive eligibility
    │   └── PlacementService.java # Higher-level business logic & Queue processing
    └── util/
        └── InputUtil.java       # User input scanner wrapper & exception-safe validator
```

---

## 💡 Practical Data Structures & OOP Concepts Used
1. **ArrayList (`java.util.List`):** Used across DAOs and Services to collect dynamic result sets from database queries.
2. **Queue (`java.util.LinkedList`):** Used in `PlacementService` to queue pending applications and batch-process shortlist/selection decisions sequentially.
3. **HashMap (`java.util.Map`):** Used for fast lookups (e.g., student skill validation) and aggregating placement performance statistics.
4. **Encapsulation:** All model properties are private with strictly controlled getters, setters, and constructors.
5. **DAO Pattern:** Clean separation of persistence logic from business rules.

---

## 🚀 Step-by-Step Setup & Execution

### 1. Database Setup (MySQL/XAMPP)
1. Start **Apache** and **MySQL** in XAMPP Control Panel.
2. Open **phpMyAdmin** (`http://localhost/phpmyadmin`).
3. Click on the **SQL** tab.
4. Open `database.sql` from this repository, copy all contents, paste into phpMyAdmin SQL editor, and click **Go**.
5. This creates the `campus_placement` database along with required tables and initial sample data.

### 2. IntelliJ IDEA Project Configuration
1. Open IntelliJ IDEA -> **File** -> **Open** -> Select the `CampusPlacementManagementSystem` directory.
2. Set SDK: **File** -> **Project Structure** -> **Project** -> Select Java 8 or higher.
3. Add MySQL Connector/J Driver:
   * **File** -> **Project Structure** -> **Libraries**.
   * Click **+** -> **Java**.
   * Select your downloaded `mysql-connector-j-8.x.x.jar` file.
   * Click **Apply** and **OK**.

### 3. Database Credentials Configuration
Open `src/database/DatabaseManager.java` and update credentials if needed:
```java
private static final String URL = "jdbc:mysql://localhost:3306/campus_placement?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = ""; // Set your MySQL password if non-empty
```

### 4. Running the Project
* Right-click `src/Main.java` -> Click **Run 'Main.main()'**.
* Interact via the interactive console menu!

---

## 📊 Core Features & Workflow
1. **Student Management:** Add, update, view, search, and soft-delete students with branch, CGPA, and skill sets.
2. **Company Management:** Track visiting recruiters, locations, and HR contacts.
3. **Placement Drives:** Post job drives with minimum CGPA criteria, required branch, skills, package, and date.
4. **Eligibility Engine:** Automatically checks if a student meets CGPA, branch, and skill requirements before allowing job application.
5. **Batch Application Processing:** Process applications using a FIFO Queue system.
6. **Placement Analytics:** View live metrics including total placements, top branch statistics, and selection ratios.
