import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import service.DataManager;
import ui.EmployeeManagementView;
import ui.EmployeeDialog;
import ui.PayrollProcessingView;
import ui.ReportView;

/**
 * Main application class for HR Management and Payroll System
 * Group Members: Ben Morrison - 101572409
 */
public class Main extends Application {

    private BorderPane mainLayout;
    private DataManager dataManager;

    @Override
    public void start(Stage primaryStage) {
        // Initialize data manager and load existing data
        dataManager = DataManager.getInstance();
        dataManager.loadAllData();

        // Set up main layout
        mainLayout = new BorderPane();

        // Set default font to avoid italics - use Arial which is guaranteed non-italic
        mainLayout.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal;");

        // Create top menu bar
        MenuBar menuBar = createMenuBar(primaryStage);
        mainLayout.setTop(menuBar);

        // Create welcome screen
        VBox welcomeScreen = createWelcomeScreen();
        mainLayout.setCenter(welcomeScreen);

        // Create scene
        Scene scene = new Scene(mainLayout, 1200, 700);

        // Apply global font styling to remove italics everywhere
        scene.getRoot().setStyle("-fx-font-family: 'Arial', 'Segoe UI', 'Helvetica', sans-serif; " +
                "-fx-font-style: normal;");

        // Set up stage
        primaryStage.setTitle("HR Management & Payroll System");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            dataManager.saveAllData();
        });
        primaryStage.show();
    }

    /**
     * Create the menu bar
     */
    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> {
            dataManager.saveAllData();
            stage.close();
        });
        fileMenu.getItems().add(exitItem);

        // Employee menu
        Menu employeeMenu = new Menu("Employees");
        MenuItem manageEmployees = new MenuItem("Manage Employees");
        manageEmployees.setOnAction(e -> showEmployeeManagement());
        MenuItem addEmployee = new MenuItem("Add New Employee");
        addEmployee.setOnAction(e -> showAddEmployeeDialog());
        employeeMenu.getItems().addAll(manageEmployees, addEmployee);

        // Department menu
        Menu departmentMenu = new Menu("Departments");
        MenuItem manageDepartments = new MenuItem("Manage Departments");
        manageDepartments.setOnAction(e -> showDepartmentManagement());
        MenuItem addDepartment = new MenuItem("Add New Department");
        addDepartment.setOnAction(e -> showAddDepartmentDialog());
        departmentMenu.getItems().addAll(manageDepartments, addDepartment);

        // Payroll menu
        Menu payrollMenu = new Menu("Payroll");
        MenuItem processPayroll = new MenuItem("Process Payroll");
        processPayroll.setOnAction(e -> showPayrollProcessing());
        MenuItem viewPayrolls = new MenuItem("View Payroll Records");
        viewPayrolls.setOnAction(e -> showPayrollRecords());
        payrollMenu.getItems().addAll(processPayroll, viewPayrolls);

        // Reports menu
        Menu reportsMenu = new Menu("Reports");
        MenuItem employeeReport = new MenuItem("Employee Report");
        employeeReport.setOnAction(e -> showEmployeeReport());
        MenuItem payrollReport = new MenuItem("Payroll Report");
        payrollReport.setOnAction(e -> showPayrollReport());
        reportsMenu.getItems().addAll(employeeReport, payrollReport);

        menuBar.getMenus().addAll(fileMenu, employeeMenu, departmentMenu, payrollMenu, reportsMenu);
        return menuBar;
    }

    /**
     * Create welcome screen
     */
    private VBox createWelcomeScreen() {
        VBox welcomeBox = new VBox(30);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(50));
        welcomeBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1a0a2e, #060047);");

        Label titleLabel = new Label("HR Management & Payroll System");
        titleLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 36px; -fx-font-weight: bold; -fx-font-style: normal; " +
                "-fx-text-fill: #FF5F9E; -fx-effect: dropshadow(gaussian, #E90064, 10, 0.5, 0, 0);");

        Label subtitleLabel = new Label("Welcome! Select an option to get started.");
        subtitleLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 16px; -fx-text-fill: #FF5F9E; -fx-font-style: normal;");

        // Quick action buttons with retro neon styling
        Button manageEmployeesBtn = createRetroButton("Manage Employees", "#B3005E", "#E90064");
        manageEmployeesBtn.setOnAction(e -> showEmployeeManagement());

        Button processPayrollBtn = createRetroButton("Process Payroll", "#E90064", "#FF5F9E");
        processPayrollBtn.setOnAction(e -> showPayrollProcessing());

        Button viewReportsBtn = createRetroButton("View Reports", "#B3005E", "#E90064");
        viewReportsBtn.setOnAction(e -> showEmployeeReport());

        welcomeBox.getChildren().addAll(titleLabel, subtitleLabel,
                manageEmployeesBtn, processPayrollBtn, viewReportsBtn);

        return welcomeBox;
    }

    /**
     * Create a retro-styled button with neon colors
     */
    private Button createRetroButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefWidth(250);
        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15 30; " +
                "-fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, %s, 8, 0.6, 0, 0);",
                bgColor, hoverColor, hoverColor));

        button.setOnMouseEntered(e -> button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15 30; " +
                "-fx-background-radius: 8; -fx-border-color: #FF5F9E; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, #FF5F9E, 15, 0.8, 0, 0); " +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;",
                hoverColor)));

        button.setOnMouseExited(e -> button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15 30; " +
                "-fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, %s, 8, 0.6, 0, 0);",
                bgColor, hoverColor, hoverColor)));

        return button;
    }

    /**
     * Show employee management screen with full TableView and CRUD operations
     */
    private void showEmployeeManagement() {
        try {
            EmployeeManagementView employeeView = new EmployeeManagementView(this::showWelcomeScreenMethod);
            mainLayout.setCenter(employeeView);
        } catch (Exception e) {
            showErrorAlert("Error Loading Employee Management",
                    "Failed to load employee management view.",
                    e.getMessage());
        }
    }

    /**
     * Show add employee dialog with validation
     */
    private void showAddEmployeeDialog() {
        try {
            EmployeeDialog dialog = new EmployeeDialog(null);
            dialog.showAndWait().ifPresent(employee -> {
                dataManager.addEmployee(employee);
                showSuccessAlert("Employee Added",
                        "Employee " + employee.getFullName() + " has been added successfully.");
            });
        } catch (Exception e) {
            showErrorAlert("Error Adding Employee",
                    "Failed to add new employee.",
                    e.getMessage());
        }
    }

    /**
     * Show department management screen (placeholder)
     */
    private void showDepartmentManagement() {
        Label label = new Label("Department Management Screen - Implementation in progress");
        label.setStyle("-fx-font-size: 20px;");
        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        mainLayout.setCenter(box);
    }

    /**
     * Show add department dialog (placeholder)
     */
    private void showAddDepartmentDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Department");
        alert.setHeaderText("Add New Department");
        alert.setContentText("This feature will allow you to add a new department to the system.");
        alert.showAndWait();
    }

    /**
     * Show payroll processing screen with calculation and validation
     */
    private void showPayrollProcessing() {
        try {
            PayrollProcessingView payrollView = new PayrollProcessingView(this::showWelcomeScreenMethod);
            mainLayout.setCenter(payrollView);
        } catch (Exception e) {
            showErrorAlert("Error Loading Payroll Processing",
                    "Failed to load payroll processing view.",
                    e.getMessage());
        }
    }

    /**
     * Show payroll records screen (placeholder)
     */
    private void showPayrollRecords() {
        Label label = new Label("Payroll Records Screen - Implementation in progress");
        label.setStyle("-fx-font-size: 20px;");
        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        mainLayout.setCenter(box);
    }

    /**
     * Show employee and payroll reports
     */
    private void showEmployeeReport() {
        try {
            ReportView reportView = new ReportView(this::showWelcomeScreenMethod);
            mainLayout.setCenter(reportView);
        } catch (Exception e) {
            showErrorAlert("Error Loading Reports",
                    "Failed to load report view.",
                    e.getMessage());
        }
    }

    /**
     * Show the welcome screen (used by back buttons)
     */
    private void showWelcomeScreenMethod() {
        VBox welcomeScreen = createWelcomeScreen();
        mainLayout.setCenter(welcomeScreen);
    }

    /**
     * Show payroll report (uses same view as employee report)
     */
    private void showPayrollReport() {
        showEmployeeReport();
    }

    /**
     * Shows an error alert dialog with exception details
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-style: normal;");
        alert.showAndWait();
    }

    /**
     * Shows a success alert dialog
     */
    private void showSuccessAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-style: normal;");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}