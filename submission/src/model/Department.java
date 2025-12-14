package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a department in the organization
 * Implements Serializable for data persistence
 *
 * Group Members: [ADD YOUR NAMES HERE]
 */
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    private String departmentId;
    private String departmentName;
    private String managerEmployeeId;
    private String description;
    private List<String> employeeIds;

    /**
     * Constructor for creating a new department
     */
    public Department(String departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.employeeIds = new ArrayList<>();
    }

    // Getters and Setters
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getManagerEmployeeId() {
        return managerEmployeeId;
    }

    public void setManagerEmployeeId(String managerEmployeeId) {
        this.managerEmployeeId = managerEmployeeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<String> employeeIds) {
        this.employeeIds = employeeIds;
    }

    /**
     * Add an employee to this department
     */
    public void addEmployee(String employeeId) {
        if (!employeeIds.contains(employeeId)) {
            employeeIds.add(employeeId);
        }
    }

    /**
     * Remove an employee from this department
     */
    public void removeEmployee(String employeeId) {
        employeeIds.remove(employeeId);
    }

    /**
     * Get the number of employees in this department
     */
    public int getEmployeeCount() {
        return employeeIds.size();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d employees)", departmentId, departmentName, employeeIds.size());
    }
}