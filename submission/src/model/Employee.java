package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an employee in the HR Management System
 * Implements Serializable for data persistence
 *
 * Group Members: [ADD YOUR NAMES HERE]
 */
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    // Basic Information
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;

    // Employment Details
    private Department department;
    private String position;
    private EmploymentType employmentType;
    private double hourlyRate;        // For hourly employees
    private double annualSalary;      // For salaried employees

    // Status
    private EmployeeStatus status;

    /**
     * Constructor for creating a new employee
     *
     * @param employeeId   Unique identifier for the employee
     * @param firstName    Employee's first name
     * @param lastName     Employee's last name
     * @param email        Employee's email address
     */
    public Employee(String employeeId, String firstName, String lastName, String email) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hireDate = LocalDate.now();
        this.status = EmployeeStatus.ACTIVE;
    }

    // Getters and Setters

    /**
     * Gets the employee's unique ID
     * @return The employee ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee's unique ID
     * @param employeeId The employee ID to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the employee's first name
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the employee's first name
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the employee's last name
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee's last name
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the employee's full name (first + last)
     * @return The full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s (%s)", employeeId, firstName, lastName, position);
    }

    /**
     * Employment type enumeration
     */
    public enum EmploymentType {
        FULL_TIME("Full-Time"),
        PART_TIME("Part-Time"),
        CONTRACT("Contract"),
        INTERN("Intern");

        private final String displayName;

        EmploymentType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Employee status enumeration
     */
    public enum EmployeeStatus {
        ACTIVE("Active"),
        ON_LEAVE("On Leave"),
        SUSPENDED("Suspended"),
        TERMINATED("Terminated");

        private final String displayName;

        EmployeeStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}