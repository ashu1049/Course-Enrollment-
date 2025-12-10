# 🎓 Course-Enrollment-Manager — Core Java

A console-based Student Course Registration System built using **Core Java**, demonstrating clean architecture, OOP principles, collections, file handling, and persistence.

---

## 🚀 Features

### 👤 Student Management
- Add new students
- List all students
- Search students by name
- Delete students (with cascading enrollment removal)

### 📘 Course Management
- Add courses with optional capacity limits
- List all courses
- Delete courses (remove related enrollments)

### 📝 Enrollment Management
- Enroll students in courses
- Prevent duplicate enrollments
- Validate course capacity
- Unenroll students
- View all enrollments

### 💾 Data Persistence
- Saves to `data/registration_data.ser` using Java Serialization
- Automatically loads saved data when the program starts

### 🧱 Clean Architecture
- `domain` → Student, Course, Enrollment
- `service` → RegistrationManager
- `persistence` → DataStore
- `app` → MainApp (console UI)

---

## 📂 Project Structure

structure: |
  student-registration/
  ├─ src/
  │  ├─ domain/
  │  │  ├─ Student.java
  │  │  ├─ Course.java
  │  │  └─ Enrollment.java
  │  ├─ service/
  │  │  └─ RegistrationManager.java
  │  ├─ persistence/
  │  │  └─ DataStore.java
  │  └─ app/
  │     └─ MainApp.java
  ├─ data/
  │  └─ registration_data.ser
  └─ README.md

---

## ▶️ How to Run

### Compile
javac -d out src/domain/*.java src/service/*.java src/persistence/*.java src/app/*.java


RUN:
java -cp out app.MainApp

