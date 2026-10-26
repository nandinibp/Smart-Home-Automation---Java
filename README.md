# Smart Home Automation Using Java

## Project Overview

Smart Home Automation is a Java-based desktop application that provides a graphical interface to manage smart home devices such as lights and fans. The application uses Java Swing for the user interface and MySQL for storing device information.

The project demonstrates Object-Oriented Programming, collections and generics, multithreading, synchronization, exception handling, and JDBC database operations.

## Features

- Graphical dashboard built using Java Swing.
- Turn devices ON and OFF.
- Add, search, view, and remove devices.
- Store and retrieve device information using MySQL.
- Update device status in the database.
- Background device monitoring using a Java thread.
- Exception handling and modular code organization.

## Technologies Used

- Java (JDK 21)
- Java Swing
- MySQL Server
- MySQL Connector/J (JDBC driver)
- NetBeans IDE
- Git and GitHub

## Project Structure

```text
SmartHomeAutomation/
├── src/
│   └── smarthome/
│       ├── Main.java
│       ├── database/
│       │   ├── DBConnection.java
│       │   ├── DeviceDAO.java
│       │   └── TestConnection.java
│       ├── exception/
│       │   └── DeviceNotFoundException.java
│       ├── model/
│       │   ├── Device.java
│       │   ├── Light.java
│       │   ├── Fan.java
│       │   └── Switchable.java
│       ├── monitor/
│       │   └── DeviceMonitor.java
│       ├── repository/
│       │   └── DeviceRepository.java
│       └── service/
│           └── DeviceManager.java
├── README.md
└── .gitignore
```

## Requirements

1. Install JDK 21 or a compatible Java version.
2. Install NetBeans IDE or another Java IDE.
3. Install and start MySQL Server.
4. Install MySQL Workbench for database administration (optional).
5. Add the MySQL Connector/J JAR to the project's classpath.

## Database Setup

Open MySQL Workbench and execute:

```sql
CREATE DATABASE IF NOT EXISTS SmartHomeDB;
USE SmartHomeDB;

CREATE TABLE IF NOT EXISTS Devices (
    device_id INT AUTO_INCREMENT PRIMARY KEY,
    device_name VARCHAR(100) NOT NULL UNIQUE,
    device_type VARCHAR(50) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'OFF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Configuration

Configure the database connection in `DBConnection.java` with your local MySQL connection settings.

Do not commit database passwords or other secrets to GitHub. Use a local environment variable or another secure configuration method for credentials.

## How to Run

1. Clone or download this repository.
2. Open the project in NetBeans.
3. Configure the MySQL database and create the `SmartHomeDB` database and `Devices` table.
4. Add MySQL Connector/J to the project classpath.
5. Configure the database credentials locally.
6. Run `TestConnection.java` to verify database connectivity.
7. Run `Main.java` to launch the Smart Home Dashboard.
8. Use the dashboard buttons to manage devices and check the saved records in MySQL Workbench.

## Concepts Demonstrated

- **OOP:** Interfaces, inheritance, abstraction, and polymorphism.
- **Collections and Generics:** Managing devices through a generic repository.
- **Exception Handling:** Handling missing devices and database errors.
- **Multithreading and Synchronization:** Background monitoring and safe access to shared device data.
- **JDBC:** Connecting to MySQL and performing insert, select, update, and delete operations.

## Future Enhancements

- Support additional smart home devices.
- Add user authentication and role-based access.
- Improve synchronization between the dashboard and database.
- Integrate physical IoT devices such as ESP32.

## Author

Developed as a Java GUI-based Smart Home Automation academic project.
