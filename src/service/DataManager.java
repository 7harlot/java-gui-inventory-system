package service;

import model.Department;
import model.Employee;
import model.Payroll;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages data persistence using Java serialization
 * Handles saving and loading of employees, departments, and payroll records
 *
 * Group Members: [ADD YOUR NAMES HERE]
 */
public class DataManager {
    private static final String EMPLOYEES_FILE = "data/employees.dat";
    private static final String DEPARTMENTS_FILE = "data/departments.dat";
    private static final String PAYROLLS_FILE = "data/payrolls.dat";

    private Map<String, Employee> employees;
    private Map<String, Department> departments;
    private Map<String, Payroll> payrolls;

    private static DataManager instance;

    /**
     * Private constructor for singleton pattern
     */
    private DataManager() {
        employees = new HashMap<>();
        departments = new HashMap<>();
        payrolls = new HashMap<>();
        ensureDataDirectoryExists();
    }

    /**
     * Get singleton instance of DataManager
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * Ensure data directory exists
     */
    private void ensureDataDirectoryExists() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    // Employee Management Methods
    public void addEmployee(Employee employee) {
        employees.put(employee.getEmployeeId(), employee);
        saveEmployees();
    }

    public Employee getEmployee(String employeeId) {
        return employees.get(employeeId);
    }

    public void updateEmployee(Employee employee) {
        employees.put(employee.getEmployeeId(), employee);
        saveEmployees();
    }

    public void deleteEmployee(String employeeId) {
        employees.remove(employeeId);
        saveEmployees();
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    // Department Management Methods
    public void addDepartment(Department department) {
        departments.put(department.getDepartmentId(), department);
        saveDepartments();
    }

    public Department getDepartment(String departmentId) {
        return departments.get(departmentId);
    }

    public void updateDepartment(Department department) {
        departments.put(department.getDepartmentId(), department);
        saveDepartments();
    }

    public void deleteDepartment(String departmentId) {
        departments.remove(departmentId);
        saveDepartments();
    }

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments.values());
    }

    // Payroll Management Methods
    public void addPayroll(Payroll payroll) {
        payrolls.put(payroll.getPayrollId(), payroll);
        savePayrolls();
    }

    public Payroll getPayroll(String payrollId) {
        return payrolls.get(payrollId);
    }

    public void updatePayroll(Payroll payroll) {
        payrolls.put(payroll.getPayrollId(), payroll);
        savePayrolls();
    }

    public void deletePayroll(String payrollId) {
        payrolls.remove(payrollId);
        savePayrolls();
    }

    public List<Payroll> getAllPayrolls() {
        return new ArrayList<>(payrolls.values());
    }

    /**
     * Get all payrolls for a specific employee
     */
    public List<Payroll> getPayrollsByEmployee(String employeeId) {
        List<Payroll> employeePayrolls = new ArrayList<>();
        for (Payroll payroll : payrolls.values()) {
            if (payroll.getEmployeeId().equals(employeeId)) {
                employeePayrolls.add(payroll);
            }
        }
        return employeePayrolls;
    }

    // Serialization Methods
    private void saveEmployees() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMPLOYEES_FILE))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadEmployees() {
        File file = new File(EMPLOYEES_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                employees = (Map<String, Employee>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading employees: " + e.getMessage());
                employees = new HashMap<>();
            }
        }
    }

    private void saveDepartments() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DEPARTMENTS_FILE))) {
            oos.writeObject(departments);
        } catch (IOException e) {
            System.err.println("Error saving departments: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadDepartments() {
        File file = new File(DEPARTMENTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                departments = (Map<String, Department>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading departments: " + e.getMessage());
                departments = new HashMap<>();
            }
        }
    }

    private void savePayrolls() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PAYROLLS_FILE))) {
            oos.writeObject(payrolls);
        } catch (IOException e) {
            System.err.println("Error saving payrolls: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadPayrolls() {
        File file = new File(PAYROLLS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                payrolls = (Map<String, Payroll>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading payrolls: " + e.getMessage());
                payrolls = new HashMap<>();
            }
        }
    }

    /**
     * Load all data from files
     */
    public void loadAllData() {
        loadEmployees();
        loadDepartments();
        loadPayrolls();
    }

    /**
     * Save all data to files
     */
    public void saveAllData() {
        saveEmployees();
        saveDepartments();
        savePayrolls();
    }

    /**
     * Clear all data (useful for testing)
     */
    public void clearAllData() {
        employees.clear();
        departments.clear();
        payrolls.clear();
        saveAllData();
    }
}