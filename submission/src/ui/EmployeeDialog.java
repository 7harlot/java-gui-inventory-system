package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Department;
import model.Employee;
import service.DataManager;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Dialog for adding or editing employee information.
 * Includes comprehensive input validation and error handling.
 *
 * @author [ADD YOUR NAMES HERE]
 * @version 1.0
 * @since 2025-11-24
 */
public class EmployeeDialog extends Dialog<Employee> {

    private Employee employee;
    private boolean isEditMode;

    // Form fields
    private TextField employeeIdField;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField phoneField;
    private DatePicker dateOfBirthPicker;
    private DatePicker hireDatePicker;
    private TextField positionField;
    private ComboBox<Employee.EmploymentType> employmentTypeCombo;
    private ComboBox<Employee.EmployeeStatus> statusCombo;
    private ComboBox<Department> departmentCombo;
    private TextField rateField;

    // Validation labels
    private Label employeeIdError;
    private Label firstNameError;
    private Label lastNameError;
    private Label emailError;
    private Label phoneError;
    private Label rateError;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Phone validation pattern (flexible)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[\\d\\s()+-]+$");

    /**
     * Constructs an EmployeeDialog.
     *
     * @param employee The employee to edit, or null to create a new employee
     */
    public EmployeeDialog(Employee employee) {
        this.employee = employee;
        this.isEditMode = (employee != null);

        setTitle(isEditMode ? "Edit Employee" : "Add New Employee");
        setHeaderText(isEditMode ? "Edit employee information" : "Enter new employee information");

        // Set dialog buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create form
        GridPane grid = createForm();
        getDialogPane().setContent(grid);

        // Style the dialog with Arial font
        getDialogPane().setStyle("-fx-background-color: #ffffff; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal;");

        // If editing, populate fields
        if (isEditMode) {
            populateFields();
        }

        // Add validation on save button
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!validateForm()) {
                event.consume(); // Prevent dialog from closing
            }
        });

        // Result converter
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return createEmployeeFromForm();
            }
            return null;
        });
    }

    /**
     * Creates the form layout with all input fields.
     *
     * @return GridPane containing the form
     */
    private GridPane createForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        int row = 0;

        // Employee ID
        Label employeeIdLabel = new Label("Employee ID:*");
        employeeIdField = new TextField();
        employeeIdField.setPromptText("e.g., EMP001");
        employeeIdField.setPrefWidth(250);
        employeeIdError = createErrorLabel();
        grid.add(employeeIdLabel, 0, row);
        grid.add(employeeIdField, 1, row);
        grid.add(employeeIdError, 2, row);
        row++;

        // First Name
        Label firstNameLabel = new Label("First Name:*");
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");
        firstNameError = createErrorLabel();
        grid.add(firstNameLabel, 0, row);
        grid.add(firstNameField, 1, row);
        grid.add(firstNameError, 2, row);
        row++;

        // Last Name
        Label lastNameLabel = new Label("Last Name:*");
        lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");
        lastNameError = createErrorLabel();
        grid.add(lastNameLabel, 0, row);
        grid.add(lastNameField, 1, row);
        grid.add(lastNameError, 2, row);
        row++;

        // Email
        Label emailLabel = new Label("Email:*");
        emailField = new TextField();
        emailField.setPromptText("employee@company.com");
        emailError = createErrorLabel();
        grid.add(emailLabel, 0, row);
        grid.add(emailField, 1, row);
        grid.add(emailError, 2, row);
        row++;

        // Phone
        Label phoneLabel = new Label("Phone:");
        phoneField = new TextField();
        phoneField.setPromptText("(123) 456-7890");
        phoneError = createErrorLabel();
        grid.add(phoneLabel, 0, row);
        grid.add(phoneField, 1, row);
        grid.add(phoneError, 2, row);
        row++;

        // Date of Birth
        Label dobLabel = new Label("Date of Birth:");
        dateOfBirthPicker = new DatePicker();
        dateOfBirthPicker.setPromptText("Select date");
        grid.add(dobLabel, 0, row);
        grid.add(dateOfBirthPicker, 1, row);
        row++;

        // Hire Date
        Label hireDateLabel = new Label("Hire Date:*");
        hireDatePicker = new DatePicker();
        hireDatePicker.setValue(LocalDate.now());
        hireDatePicker.setPromptText("Select hire date");
        grid.add(hireDateLabel, 0, row);
        grid.add(hireDatePicker, 1, row);
        row++;

        // Position
        Label positionLabel = new Label("Position:");
        positionField = new TextField();
        positionField.setPromptText("e.g., Software Engineer");
        grid.add(positionLabel, 0, row);
        grid.add(positionField, 1, row);
        row++;

        // Employment Type
        Label empTypeLabel = new Label("Employment Type:*");
        employmentTypeCombo = new ComboBox<>();
        employmentTypeCombo.getItems().addAll(Employee.EmploymentType.values());
        employmentTypeCombo.setValue(Employee.EmploymentType.FULL_TIME);
        employmentTypeCombo.setMaxWidth(Double.MAX_VALUE);
        grid.add(empTypeLabel, 0, row);
        grid.add(employmentTypeCombo, 1, row);
        row++;

        // Status
        Label statusLabel = new Label("Status:*");
        statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(Employee.EmployeeStatus.values());
        statusCombo.setValue(Employee.EmployeeStatus.ACTIVE);
        statusCombo.setMaxWidth(Double.MAX_VALUE);
        grid.add(statusLabel, 0, row);
        grid.add(statusCombo, 1, row);
        row++;

        // Department
        Label departmentLabel = new Label("Department:");
        departmentCombo = new ComboBox<>();
        loadDepartments();
        departmentCombo.setMaxWidth(Double.MAX_VALUE);
        grid.add(departmentLabel, 0, row);
        grid.add(departmentCombo, 1, row);
        row++;

        // Rate/Salary
        Label rateLabel = new Label("Hourly Rate / Annual Salary:*");
        rateField = new TextField();
        rateField.setPromptText("e.g., 25.00 or 55000.00");
        rateError = createErrorLabel();
        grid.add(rateLabel, 0, row);
        grid.add(rateField, 1, row);
        grid.add(rateError, 2, row);
        row++;

        // Add hint about hourly vs salary
        Label hintLabel = new Label("Hint: Use hourly rate for Hourly/Part-Time, annual salary for Full-Time/Contract");
        hintLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 11px; -fx-font-style: normal; -fx-text-fill: #7f8c8d;");
        grid.add(hintLabel, 1, row);
        row++;

        // Required fields note
        Label requiredNote = new Label("* Required fields");
        requiredNote.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 11px; -fx-font-style: normal; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        grid.add(requiredNote, 1, row);

        return grid;
    }

    /**
     * Creates an error label for validation messages.
     *
     * @return Styled Label for error messages
     */
    private Label createErrorLabel() {
        Label label = new Label();
        label.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-text-fill: #e74c3c; -fx-font-size: 11px; -fx-font-style: normal;");
        label.setVisible(false);
        return label;
    }

    /**
     * Loads departments into the combo box.
     */
    private void loadDepartments() {
        try {
            DataManager dataManager = DataManager.getInstance();
            departmentCombo.getItems().addAll(dataManager.getAllDepartments());
        } catch (Exception e) {
            System.err.println("Error loading departments: " + e.getMessage());
        }
    }

    /**
     * Populates form fields when editing an existing employee.
     */
    private void populateFields() {
        employeeIdField.setText(employee.getEmployeeId());
        employeeIdField.setDisable(true); // Can't change ID when editing

        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhone() != null ? employee.getPhone() : "");
        dateOfBirthPicker.setValue(employee.getDateOfBirth());
        hireDatePicker.setValue(employee.getHireDate());
        positionField.setText(employee.getPosition() != null ? employee.getPosition() : "");
        employmentTypeCombo.setValue(employee.getEmploymentType());
        statusCombo.setValue(employee.getStatus());
        departmentCombo.setValue(employee.getDepartment());

        // Set rate based on employment type
        if (employee.getEmploymentType() == Employee.EmploymentType.FULL_TIME ||
            employee.getEmploymentType() == Employee.EmploymentType.CONTRACT) {
            rateField.setText(String.valueOf(employee.getAnnualSalary()));
        } else {
            rateField.setText(String.valueOf(employee.getHourlyRate()));
        }
    }

    /**
     * Validates all form fields with comprehensive error checking.
     *
     * @return true if all validations pass, false otherwise
     */
    private boolean validateForm() {
        boolean isValid = true;
        clearErrorMessages();

        // Validate Employee ID
        String empId = employeeIdField.getText().trim();
        if (empId.isEmpty()) {
            showError(employeeIdError, "Employee ID is required");
            isValid = false;
        } else if (empId.length() < 3) {
            showError(employeeIdError, "ID must be at least 3 characters");
            isValid = false;
        } else if (!isEditMode) {
            // Check for duplicate ID only when adding new employee
            try {
                if (DataManager.getInstance().getEmployee(empId) != null) {
                    showError(employeeIdError, "Employee ID already exists");
                    isValid = false;
                }
            } catch (Exception e) {
                // Employee doesn't exist, which is good
            }
        }

        // Validate First Name
        String firstName = firstNameField.getText().trim();
        if (firstName.isEmpty()) {
            showError(firstNameError, "First name is required");
            isValid = false;
        } else if (firstName.length() < 2) {
            showError(firstNameError, "Name must be at least 2 characters");
            isValid = false;
        } else if (!firstName.matches("[a-zA-Z\\s-]+")) {
            showError(firstNameError, "Name can only contain letters");
            isValid = false;
        }

        // Validate Last Name
        String lastName = lastNameField.getText().trim();
        if (lastName.isEmpty()) {
            showError(lastNameError, "Last name is required");
            isValid = false;
        } else if (lastName.length() < 2) {
            showError(lastNameError, "Name must be at least 2 characters");
            isValid = false;
        } else if (!lastName.matches("[a-zA-Z\\s-]+")) {
            showError(lastNameError, "Name can only contain letters");
            isValid = false;
        }

        // Validate Email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showError(emailError, "Email is required");
            isValid = false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError(emailError, "Invalid email format");
            isValid = false;
        }

        // Validate Phone (optional, but must be valid if provided)
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            showError(phoneError, "Invalid phone format");
            isValid = false;
        }

        // Validate Rate/Salary
        String rateStr = rateField.getText().trim();
        if (rateStr.isEmpty()) {
            showError(rateError, "Rate/Salary is required");
            isValid = false;
        } else {
            try {
                double rate = Double.parseDouble(rateStr);
                if (rate <= 0) {
                    showError(rateError, "Rate must be greater than 0");
                    isValid = false;
                } else if (rate > 1000000) {
                    showError(rateError, "Value seems unreasonably high");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                showError(rateError, "Must be a valid number");
                isValid = false;
            }
        }

        // Validate Hire Date
        if (hireDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Missing Hire Date");
            alert.setContentText("Please select a hire date.");
            alert.showAndWait();
            isValid = false;
        } else if (hireDatePicker.getValue().isAfter(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Invalid Hire Date");
            alert.setContentText("Hire date cannot be in the future.");
            alert.showAndWait();
            isValid = false;
        }

        // Validate Date of Birth (if provided)
        if (dateOfBirthPicker.getValue() != null) {
            LocalDate dob = dateOfBirthPicker.getValue();
            LocalDate minAge = LocalDate.now().minusYears(16);
            LocalDate maxAge = LocalDate.now().minusYears(100);

            if (dob.isAfter(minAge)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid Date of Birth");
                alert.setContentText("Employee must be at least 16 years old.");
                alert.showAndWait();
                isValid = false;
            } else if (dob.isBefore(maxAge)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid Date of Birth");
                alert.setContentText("Date of birth seems invalid (too far in the past).");
                alert.showAndWait();
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Shows an error message on a validation label.
     *
     * @param label The error label to display
     * @param message The error message
     */
    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    /**
     * Clears all error messages from the form.
     */
    private void clearErrorMessages() {
        employeeIdError.setVisible(false);
        firstNameError.setVisible(false);
        lastNameError.setVisible(false);
        emailError.setVisible(false);
        phoneError.setVisible(false);
        rateError.setVisible(false);
    }

    /**
     * Creates an Employee object from the form data.
     *
     * @return Employee object with form data
     */
    private Employee createEmployeeFromForm() {
        try {
            Employee emp;

            if (isEditMode) {
                emp = employee;
            } else {
                emp = new Employee(
                        employeeIdField.getText().trim(),
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        emailField.getText().trim()
                );
            }

            // Update all fields
            emp.setFirstName(firstNameField.getText().trim());
            emp.setLastName(lastNameField.getText().trim());
            emp.setEmail(emailField.getText().trim());
            emp.setPhone(phoneField.getText().trim().isEmpty() ? null : phoneField.getText().trim());
            emp.setDateOfBirth(dateOfBirthPicker.getValue());
            emp.setHireDate(hireDatePicker.getValue());
            emp.setPosition(positionField.getText().trim().isEmpty() ? null : positionField.getText().trim());
            emp.setEmploymentType(employmentTypeCombo.getValue());
            emp.setStatus(statusCombo.getValue());
            emp.setDepartment(departmentCombo.getValue());

            // Set rate based on employment type
            double rate = Double.parseDouble(rateField.getText().trim());
            if (employmentTypeCombo.getValue() == Employee.EmploymentType.FULL_TIME ||
                employmentTypeCombo.getValue() == Employee.EmploymentType.CONTRACT) {
                emp.setAnnualSalary(rate);
                emp.setHourlyRate(0);
            } else {
                emp.setHourlyRate(rate);
                emp.setAnnualSalary(0);
            }

            return emp;

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Creating Employee");
            alert.setHeaderText("Failed to create employee from form data");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
            return null;
        }
    }
}