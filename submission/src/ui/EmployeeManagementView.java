package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.Department;
import model.Employee;
import service.DataManager;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Employee Management View with TableView, search, and CRUD operations.
 * Includes input validation and exception handling.
 *
 * @author [ADD YOUR NAMES HERE]
 * @version 1.0
 * @since 2025-11-24
 */
public class EmployeeManagementView extends BorderPane {

    private DataManager dataManager;
    private TableView<Employee> employeeTable;
    private ObservableList<Employee> employeeList;
    private TextField searchField;
    private Runnable onBackToMenu;

    /**
     * Constructs the Employee Management View with all UI components.
     *
     * @param onBackToMenu Callback to execute when back button is clicked
     */
    public EmployeeManagementView(Runnable onBackToMenu) {
        this.dataManager = DataManager.getInstance();
        this.onBackToMenu = onBackToMenu;
        initializeUI();
        refreshEmployeeList();
    }

    /**
     * Initializes all UI components and layouts.
     */
    private void initializeUI() {
        // Set padding and styling
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f5f5f5;");

        // Create top section with title and search
        VBox topSection = createTopSection();
        setTop(topSection);

        // Create center section with table
        VBox centerSection = createTableSection();
        setCenter(centerSection);

        // Create right section with action buttons
        VBox rightSection = createActionButtons();
        setRight(rightSection);
    }

    /**
     * Creates the top section with title and search bar.
     *
     * @return VBox containing title and search components
     */
    private VBox createTopSection() {
        VBox topBox = new VBox(15);
        topBox.setPadding(new Insets(0, 0, 20, 0));

        // Back button and title in HBox
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Back to Main Menu");
        backButton.setStyle("-fx-background-color: #060047; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 15; " +
                "-fx-background-radius: 8; -fx-border-color: #B3005E; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-cursor: hand;");
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: #B3005E; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 15; " +
                "-fx-background-radius: 8; -fx-border-color: #FF5F9E; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, #FF5F9E, 10, 0.7, 0, 0);"));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: #060047; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 15; " +
                "-fx-background-radius: 8; -fx-border-color: #B3005E; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-cursor: hand;"));
        backButton.setOnAction(e -> {
            if (onBackToMenu != null) {
                onBackToMenu.run();
            }
        });

        // Title with neon glow
        Label titleLabel = new Label("Employee Management");
        titleLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-style: normal; " +
                "-fx-text-fill: #E90064; -fx-effect: dropshadow(gaussian, #FF5F9E, 8, 0.6, 0, 0);");

        headerBox.getChildren().addAll(backButton, titleLabel);

        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");
        searchLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 14px; -fx-text-fill: #B3005E; -fx-font-weight: bold; " +
                "-fx-font-style: normal;");

        searchField = new TextField();
        searchField.setPromptText("Search by name, ID, or email...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-radius: 8; -fx-padding: 8; " +
                "-fx-border-color: #B3005E; -fx-border-width: 2; -fx-border-radius: 8;");

        // Add real-time search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterEmployees(newValue);
        });

        Button clearSearchBtn = new Button("Clear");
        clearSearchBtn.setStyle("-fx-background-color: #B3005E; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-background-radius: 8; -fx-padding: 8 15; -fx-font-weight: bold; " +
                "-fx-border-color: #E90064; -fx-border-width: 2; -fx-border-radius: 8;");
        clearSearchBtn.setOnMouseEntered(e -> clearSearchBtn.setStyle(
                "-fx-background-color: #E90064; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-background-radius: 8; -fx-padding: 8 15; -fx-font-weight: bold; " +
                "-fx-border-color: #FF5F9E; -fx-border-width: 2; -fx-border-radius: 8;"));
        clearSearchBtn.setOnMouseExited(e -> clearSearchBtn.setStyle(
                "-fx-background-color: #B3005E; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-background-radius: 8; -fx-padding: 8 15; -fx-font-weight: bold; " +
                "-fx-border-color: #E90064; -fx-border-width: 2; -fx-border-radius: 8;"));
        clearSearchBtn.setOnAction(e -> {
            searchField.clear();
            refreshEmployeeList();
        });

        searchBox.getChildren().addAll(searchLabel, searchField, clearSearchBtn);

        topBox.getChildren().addAll(headerBox, searchBox);
        return topBox;
    }

    /**
     * Creates the table section displaying all employees.
     *
     * @return VBox containing the employee TableView
     */
    private VBox createTableSection() {
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(0, 10, 0, 0));

        employeeList = FXCollections.observableArrayList();
        employeeTable = new TableView<>(employeeList);
        employeeTable.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        // Employee ID column
        TableColumn<Employee, String> idCol = new TableColumn<>("Employee ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        idCol.setPrefWidth(120);

        // First Name column
        TableColumn<Employee, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setPrefWidth(120);

        // Last Name column
        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(120);

        // Email column
        TableColumn<Employee, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        // Position column
        TableColumn<Employee, String> positionCol = new TableColumn<>("Position");
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        positionCol.setPrefWidth(150);

        // Employment Type column
        TableColumn<Employee, Employee.EmploymentType> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("employmentType"));
        typeCol.setPrefWidth(100);

        // Status column
        TableColumn<Employee, Employee.EmployeeStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        employeeTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol,
                                          positionCol, typeCol, statusCol);

        // Allow multiple selection for batch operations
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableBox.getChildren().add(employeeTable);
        VBox.setVgrow(employeeTable, Priority.ALWAYS);

        return tableBox;
    }

    /**
     * Creates the action buttons panel on the right side.
     *
     * @return VBox containing all action buttons
     */
    private VBox createActionButtons() {
        VBox buttonBox = new VBox(15);
        buttonBox.setPadding(new Insets(0, 0, 0, 10));
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.setPrefWidth(150);

        // Add Employee button - bright pink
        Button addBtn = createStyledButton("Add Employee", "#E90064");
        addBtn.setOnAction(e -> showAddEmployeeDialog());

        // Edit Employee button - deep magenta
        Button editBtn = createStyledButton("Edit Employee", "#B3005E");
        editBtn.setOnAction(e -> showEditEmployeeDialog());

        // Delete Employee button - red (keep for danger)
        Button deleteBtn = createStyledButton("Delete Employee", "#e74c3c");
        deleteBtn.setOnAction(e -> deleteEmployee());

        // View Details button - lighter pink
        Button viewBtn = createStyledButton("View Details", "#FF5F9E");
        viewBtn.setOnAction(e -> showEmployeeDetails());

        // Refresh button - dark purple
        Button refreshBtn = createStyledButton("Refresh", "#060047");
        refreshBtn.setOnAction(e -> refreshEmployeeList());

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, viewBtn, refreshBtn);

        return buttonBox;
    }

    /**
     * Creates a styled button with retro neon appearance.
     *
     * @param text  The button text
     * @param color The background color in hex format
     * @return Styled Button
     */
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);

        // Determine glow color based on button color
        String glowColor = color.equals("#e74c3c") ? "#FF5F9E" : "#E90064";

        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20; " +
                "-fx-background-radius: 8; -fx-cursor: hand; " +
                "-fx-border-color: %s; -fx-border-width: 2; -fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, %s, 6, 0.5, 0, 0);",
                color, glowColor, glowColor));

        // Hover effect with neon glow
        button.setOnMouseEntered(e -> button.setStyle(String.format(
                "-fx-background-color: derive(%s, 20%%); -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20; " +
                "-fx-background-radius: 8; -fx-cursor: hand; " +
                "-fx-border-color: #FF5F9E; -fx-border-width: 2; -fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, #FF5F9E, 12, 0.8, 0, 0);", color)));

        button.setOnMouseExited(e -> button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20; " +
                "-fx-background-radius: 8; -fx-cursor: hand; " +
                "-fx-border-color: %s; -fx-border-width: 2; -fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, %s, 6, 0.5, 0, 0);",
                color, glowColor, glowColor)));

        return button;
    }

    /**
     * Refreshes the employee list from the database.
     */
    private void refreshEmployeeList() {
        try {
            employeeList.clear();
            employeeList.addAll(dataManager.getAllEmployees());
        } catch (Exception e) {
            showErrorDialog("Error Loading Employees",
                    "Failed to load employee data from database.",
                    e.getMessage());
        }
    }

    /**
     * Filters employees based on search query.
     *
     * @param query The search query string
     */
    private void filterEmployees(String query) {
        if (query == null || query.trim().isEmpty()) {
            refreshEmployeeList();
            return;
        }

        try {
            String lowerQuery = query.toLowerCase();
            ObservableList<Employee> filtered = FXCollections.observableArrayList();

            for (Employee emp : dataManager.getAllEmployees()) {
                if (emp.getEmployeeId().toLowerCase().contains(lowerQuery) ||
                    emp.getFirstName().toLowerCase().contains(lowerQuery) ||
                    emp.getLastName().toLowerCase().contains(lowerQuery) ||
                    (emp.getEmail() != null && emp.getEmail().toLowerCase().contains(lowerQuery)) ||
                    (emp.getPosition() != null && emp.getPosition().toLowerCase().contains(lowerQuery))) {
                    filtered.add(emp);
                }
            }

            employeeList.clear();
            employeeList.addAll(filtered);
        } catch (Exception e) {
            showErrorDialog("Error Searching",
                    "An error occurred while searching for employees.",
                    e.getMessage());
        }
    }

    /**
     * Shows the Add Employee dialog.
     */
    private void showAddEmployeeDialog() {
        EmployeeDialog dialog = new EmployeeDialog(null);
        Optional<Employee> result = dialog.showAndWait();

        result.ifPresent(employee -> {
            try {
                dataManager.addEmployee(employee);
                refreshEmployeeList();
                showSuccessDialog("Employee Added",
                        "Employee " + employee.getFullName() + " has been added successfully.");
            } catch (Exception e) {
                showErrorDialog("Error Adding Employee",
                        "Failed to add employee to database.",
                        e.getMessage());
            }
        });
    }

    /**
     * Shows the Edit Employee dialog for the selected employee.
     */
    private void showEditEmployeeDialog() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarningDialog("No Selection",
                    "Please select an employee to edit.");
            return;
        }

        EmployeeDialog dialog = new EmployeeDialog(selected);
        Optional<Employee> result = dialog.showAndWait();

        result.ifPresent(employee -> {
            try {
                dataManager.updateEmployee(employee);
                refreshEmployeeList();
                showSuccessDialog("Employee Updated",
                        "Employee " + employee.getFullName() + " has been updated successfully.");
            } catch (Exception e) {
                showErrorDialog("Error Updating Employee",
                        "Failed to update employee in database.",
                        e.getMessage());
            }
        });
    }

    /**
     * Deletes the selected employee after confirmation.
     */
    private void deleteEmployee() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarningDialog("No Selection",
                    "Please select an employee to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Employee: " + selected.getFullName());
        confirmAlert.setContentText("Are you sure you want to delete this employee? This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                dataManager.deleteEmployee(selected.getEmployeeId());
                refreshEmployeeList();
                showSuccessDialog("Employee Deleted",
                        "Employee " + selected.getFullName() + " has been deleted successfully.");
            } catch (Exception e) {
                showErrorDialog("Error Deleting Employee",
                        "Failed to delete employee from database.",
                        e.getMessage());
            }
        }
    }

    /**
     * Shows detailed information about the selected employee.
     */
    private void showEmployeeDetails() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarningDialog("No Selection",
                    "Please select an employee to view details.");
            return;
        }

        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Employee Details");
        detailsAlert.setHeaderText(selected.getFullName());

        StringBuilder details = new StringBuilder();
        details.append("Employee ID: ").append(selected.getEmployeeId()).append("\n");
        details.append("Email: ").append(selected.getEmail() != null ? selected.getEmail() : "N/A").append("\n");
        details.append("Phone: ").append(selected.getPhone() != null ? selected.getPhone() : "N/A").append("\n");
        details.append("Position: ").append(selected.getPosition() != null ? selected.getPosition() : "N/A").append("\n");
        details.append("Employment Type: ").append(selected.getEmploymentType() != null ?
                selected.getEmploymentType().getDisplayName() : "N/A").append("\n");
        details.append("Status: ").append(selected.getStatus() != null ?
                selected.getStatus().getDisplayName() : "N/A").append("\n");
        details.append("Hire Date: ").append(selected.getHireDate() != null ?
                selected.getHireDate().toString() : "N/A").append("\n");

        if (selected.getEmploymentType() == Employee.EmploymentType.FULL_TIME ||
            selected.getEmploymentType() == Employee.EmploymentType.PART_TIME) {
            details.append("Hourly Rate: $").append(String.format("%.2f", selected.getHourlyRate())).append("\n");
        } else {
            details.append("Annual Salary: $").append(String.format("%.2f", selected.getAnnualSalary())).append("\n");
        }

        detailsAlert.setContentText(details.toString());
        detailsAlert.showAndWait();
    }

    // Dialog helper methods

    /**
     * Shows an error dialog with exception details.
     */
    private void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-style: normal;");
        alert.showAndWait();
    }

    /**
     * Shows a warning dialog.
     */
    private void showWarningDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-style: normal;");
        alert.showAndWait();
    }

    /**
     * Shows a success dialog.
     */
    private void showSuccessDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-style: normal;");
        alert.showAndWait();
    }
}