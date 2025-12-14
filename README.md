# HR Management & Payroll System

A comprehensive JavaFX desktop application for managing employee data, departments, and payroll processing with automated tax calculations and data persistence.

## Overview

This HR Management system demonstrates enterprise-level software design patterns and JavaFX GUI development. Built as a collaborative group project for COMP-2130, it showcases object-oriented programming principles, MVC architecture, and practical business logic implementation.

## Key Features

### Employee Management
- Complete employee data model with support for multiple employment types (Full-Time, Part-Time, Contract, Intern)
- Employee status tracking (Active, On Leave, Suspended, Terminated)
- CRUD operations for managing employee records

### Department Management
- Department structure with employee assignment capabilities
- Manager designation and tracking
- Department-level employee organization

### Payroll Processing
- Automated payroll calculations for both hourly and salaried employees
- Comprehensive tax deduction handling:
  - Federal tax (15%)
  - State tax (5%)
  - Social Security (6.2%)
  - Medicare (1.45%)
- Regular and overtime pay computation
- Year-to-date payroll summaries

### Data Persistence
- Automatic save/load functionality using Java serialization
- File-based data storage for employees, departments, and payroll records
- Singleton pattern implementation for centralized data management

## Technical Architecture

```
src/
├── Main.java                    # JavaFX application entry point
├── model/                       # Domain models
│   ├── Employee.java           # Employee entity with full metadata
│   ├── Department.java         # Department structure
│   └── Payroll.java           # Payroll record with calculations
└── service/                    # Business logic layer
    ├── DataManager.java       # Data persistence service (Singleton)
    └── PayrollCalculator.java # Payroll computation engine
```

## Technologies Used

- **Java** - Core programming language
- **JavaFX** - GUI framework
- **Java Serialization** - Data persistence
- **Design Patterns** - Singleton, MVC

## Skills Demonstrated

- Object-oriented design and implementation
- GUI development with JavaFX
- Design pattern implementation (Singleton, MVC)
- File I/O and data serialization
- Business logic implementation
- Tax calculation algorithms
- Collaborative software development

## How to Run

1. Ensure JavaFX SDK is installed and configured
2. Add JavaFX libraries to your project build path
3. Run `Main.java`
4. The application will automatically create a `data/` directory for persistence

## Project Context

Developed as a group project for **COMP-2130: Advanced Java Programming** at George Brown College. This project demonstrates practical application of advanced Java concepts including JavaFX GUI development, serialization, and enterprise design patterns.