package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import model.Employee;
import model.Payroll;
import service.DataManager;
import service.PayrollCalculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Report View for generating and displaying employee and payroll reports.
 * Includes export functionality to CSV format.
 *
 * @author [ADD YOUR NAMES HERE]
 * @version 1.0
 * @since 2025-11-24
 */
public class ReportView extends BorderPane {

    private DataManager dataManager;
    private ComboBox<String> reportTypeCombo;
    private TextArea reportTextArea;
    private TableView<Object> reportTable;
    private Runnable onBackToMenu;

    /**
     * Constructs the Report View.
     *
     * @param onBackToMenu Callback to execute when back button is clicked
     */
    public ReportView(Runnable onBackToMenu) {
        this.dataManager = DataManager.getInstance();
        this.onBackToMenu = onBackToMenu;
        initializeUI();
    }

    /**
     * Initializes all UI components.
     */
    private void initializeUI() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f5f5f5;");

        // Top section with title and controls
        VBox topSection = createTopSection();
        setTop(topSection);

        // Center section with report display
        VBox centerSection = createReportSection();
        setCenter(centerSection);
    }

    /**
     * Creates the top section with report selection.
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
        Label titleLabel = new Label("Reports");
        titleLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-style: normal; " +
                "-fx-text-fill: #E90064; -fx-effect: dropshadow(gaussian, #FF5F9E, 8, 0.6, 0, 0);");

        headerBox.getChildren().addAll(backButton, titleLabel);

        // Report selection
        HBox controlBox = new HBox(15);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        Label selectLabel = new Label("Select Report:");
        selectLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-style: normal; " +
                "-fx-text-fill: #B3005E;");

        reportTypeCombo = new ComboBox<>();
        reportTypeCombo.getItems().addAll(
                "All Employees",
                "Active Employees",
                "All Payroll Records",
                "Payroll Summary by Employee",
                "Year-to-Date Summary"
        );
        reportTypeCombo.setValue("All Employees");
        reportTypeCombo.setPrefWidth(250);
        reportTypeCombo.setStyle("-fx-border-color: #B3005E; -fx-border-width: 2; -fx-border-radius: 8;");

        Button generateBtn = createStyledButton("Generate Report", "#B3005E");
        generateBtn.setOnAction(e -> generateReport());

        Button exportBtn = createStyledButton("Export to CSV", "#E90064");
        exportBtn.setOnAction(e -> exportToCSV());

        Button printBtn = createStyledButton("Print", "#FF5F9E");
        printBtn.setOnAction(e -> printReport());

        controlBox.getChildren().addAll(selectLabel, reportTypeCombo, generateBtn, exportBtn, printBtn);

        topBox.getChildren().addAll(headerBox, controlBox);
        return topBox;
    }

    /**
     * Creates the report display section.
     */
    private VBox createReportSection() {
        VBox reportBox = new VBox(10);
        reportBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        // Report header
        Label reportHeader = new Label("Report Output");
        reportHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Text area for text reports
        reportTextArea = new TextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        reportTextArea.setPrefHeight(500);
        reportTextArea.setVisible(true);

        reportBox.getChildren().addAll(reportHeader, reportTextArea);
        VBox.setVgrow(reportTextArea, Priority.ALWAYS);

        return reportBox;
    }

    /**
     * Creates a retro-styled button with neon colors.
     */
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setMinWidth(Button.USE_PREF_SIZE); // Allow button to grow to fit text

        // Determine glow color
        String glowColor = color.equals("#FF5F9E") ? "#E90064" : "#FF5F9E";

        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20; " +
                "-fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, %s, 6, 0.5, 0, 0);",
                color, glowColor, glowColor));

        button.setOnMouseEntered(e -> button.setStyle(String.format(
                "-fx-background-color: derive(%s, 20%%); -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20; " +
                "-fx-background-radius: 8; -fx-border-color: #FF5F9E; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, #FF5F9E, 12, 0.8, 0, 0);", color)));

        button.setOnMouseExited(e -> button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20; " +
                "-fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, %s, 6, 0.5, 0, 0);",
                color, glowColor, glowColor)));

        return button;
    }

    /**
     * Generates the selected report.
     */
    private void generateReport() {
        try {
            String reportType = reportTypeCombo.getValue();
            StringBuilder report = new StringBuilder();

            report.append("=".repeat(80)).append("\n");
            report.append(reportType.toUpperCase()).append("\n");
            report.append("Generated: ").append(LocalDate.now()).append("\n");
            report.append("=".repeat(80)).append("\n\n");

            switch (reportType) {
                case "All Employees":
                    generateAllEmployeesReport(report);
                    break;
                case "Active Employees":
                    generateActiveEmployeesReport(report);
                    break;
                case "All Payroll Records":
                    generateAllPayrollReport(report);
                    break;
                case "Payroll Summary by Employee":
                    generatePayrollSummaryReport(report);
                    break;
                case "Year-to-Date Summary":
                    generateYTDSummaryReport(report);
                    break;
            }

            reportTextArea.setText(report.toString());

        } catch (Exception e) {
            showError("Report Generation Error",
                    "Failed to generate report.",
                    e.getMessage());
        }
    }

    /**
     * Generates report of all employees.
     */
    private void generateAllEmployeesReport(StringBuilder report) {
        List<Employee> employees = dataManager.getAllEmployees();

        report.append("Total Employees: ").append(employees.size()).append("\n\n");
        report.append(String.format("%-12s %-20s %-30s %-20s %-15s%n",
                "ID", "Name", "Email", "Position", "Status"));
        report.append("-".repeat(100)).append("\n");

        for (Employee emp : employees) {
            report.append(String.format("%-12s %-20s %-30s %-20s %-15s%n",
                    emp.getEmployeeId(),
                    emp.getFullName(),
                    emp.getEmail() != null ? emp.getEmail() : "N/A",
                    emp.getPosition() != null ? emp.getPosition() : "N/A",
                    emp.getStatus() != null ? emp.getStatus().getDisplayName() : "N/A"
            ));
        }

        report.append("\n").append("=".repeat(80)).append("\n");
    }

    /**
     * Generates report of active employees only.
     */
    private void generateActiveEmployeesReport(StringBuilder report) {
        List<Employee> employees = dataManager.getAllEmployees();
        long activeCount = employees.stream()
                .filter(e -> e.getStatus() == Employee.EmployeeStatus.ACTIVE)
                .count();

        report.append("Active Employees: ").append(activeCount).append("\n\n");
        report.append(String.format("%-12s %-20s %-30s %-20s %-15s%n",
                "ID", "Name", "Email", "Position", "Type"));
        report.append("-".repeat(100)).append("\n");

        for (Employee emp : employees) {
            if (emp.getStatus() == Employee.EmployeeStatus.ACTIVE) {
                report.append(String.format("%-12s %-20s %-30s %-20s %-15s%n",
                        emp.getEmployeeId(),
                        emp.getFullName(),
                        emp.getEmail() != null ? emp.getEmail() : "N/A",
                        emp.getPosition() != null ? emp.getPosition() : "N/A",
                        emp.getEmploymentType() != null ? emp.getEmploymentType().getDisplayName() : "N/A"
                ));
            }
        }

        report.append("\n").append("=".repeat(80)).append("\n");
    }

    /**
     * Generates report of all payroll records.
     */
    private void generateAllPayrollReport(StringBuilder report) {
        List<Payroll> payrolls = dataManager.getAllPayrolls();

        report.append("Total Payroll Records: ").append(payrolls.size()).append("\n\n");
        report.append(String.format("%-20s %-12s %-12s %-15s %-15s %-15s%n",
                "Payroll ID", "Employee ID", "Pay Date", "Gross Pay", "Deductions", "Net Pay"));
        report.append("-".repeat(100)).append("\n");

        double totalGross = 0;
        double totalDeductions = 0;
        double totalNet = 0;

        for (Payroll payroll : payrolls) {
            report.append(String.format("%-20s %-12s %-12s $%-14.2f $%-14.2f $%-14.2f%n",
                    payroll.getPayrollId(),
                    payroll.getEmployeeId(),
                    payroll.getPayDate(),
                    payroll.getGrossPay(),
                    payroll.getTotalDeductions(),
                    payroll.getNetPay()
            ));

            totalGross += payroll.getGrossPay();
            totalDeductions += payroll.getTotalDeductions();
            totalNet += payroll.getNetPay();
        }

        report.append("-".repeat(100)).append("\n");
        report.append(String.format("%-45s $%-14.2f $%-14.2f $%-14.2f%n",
                "TOTALS:", totalGross, totalDeductions, totalNet));
        report.append("\n").append("=".repeat(80)).append("\n");
    }

    /**
     * Generates payroll summary by employee.
     */
    private void generatePayrollSummaryReport(StringBuilder report) {
        List<Employee> employees = dataManager.getAllEmployees();

        report.append("Payroll Summary by Employee\n\n");
        report.append(String.format("%-12s %-20s %-15s %-15s %-15s%n",
                "Employee ID", "Name", "Total Gross", "Total Deduct", "Total Net"));
        report.append("-".repeat(80)).append("\n");

        for (Employee emp : employees) {
            List<Payroll> empPayrolls = dataManager.getPayrollsByEmployee(emp.getEmployeeId());

            if (!empPayrolls.isEmpty()) {
                double totalGross = empPayrolls.stream().mapToDouble(Payroll::getGrossPay).sum();
                double totalDeduct = empPayrolls.stream().mapToDouble(Payroll::getTotalDeductions).sum();
                double totalNet = empPayrolls.stream().mapToDouble(Payroll::getNetPay).sum();

                report.append(String.format("%-12s %-20s $%-14.2f $%-14.2f $%-14.2f%n",
                        emp.getEmployeeId(),
                        emp.getFullName(),
                        totalGross,
                        totalDeduct,
                        totalNet
                ));
            }
        }

        report.append("\n").append("=".repeat(80)).append("\n");
    }

    /**
     * Generates year-to-date summary report.
     */
    private void generateYTDSummaryReport(StringBuilder report) {
        List<Employee> employees = dataManager.getAllEmployees();
        int currentYear = LocalDate.now().getYear();

        report.append("Year-to-Date Payroll Summary (").append(currentYear).append(")\n\n");
        report.append(String.format("%-12s %-20s %-15s %-15s %-15s%n",
                "Employee ID", "Name", "YTD Gross", "YTD Deduct", "YTD Net"));
        report.append("-".repeat(80)).append("\n");

        double grandTotalGross = 0;
        double grandTotalDeduct = 0;
        double grandTotalNet = 0;

        for (Employee emp : employees) {
            PayrollCalculator.PayrollSummary summary =
                    PayrollCalculator.calculateYearToDateSummary(emp.getEmployeeId());

            if (summary.getTotalGrossPay() > 0) {
                report.append(String.format("%-12s %-20s $%-14.2f $%-14.2f $%-14.2f%n",
                        emp.getEmployeeId(),
                        emp.getFullName(),
                        summary.getTotalGrossPay(),
                        summary.getTotalDeductions(),
                        summary.getTotalNetPay()
                ));

                grandTotalGross += summary.getTotalGrossPay();
                grandTotalDeduct += summary.getTotalDeductions();
                grandTotalNet += summary.getTotalNetPay();
            }
        }

        report.append("-".repeat(80)).append("\n");
        report.append(String.format("%-32s $%-14.2f $%-14.2f $%-14.2f%n",
                "GRAND TOTALS:", grandTotalGross, grandTotalDeduct, grandTotalNet));
        report.append("\n").append("=".repeat(80)).append("\n");
    }

    /**
     * Exports the current report to CSV file.
     */
    private void exportToCSV() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Report to CSV");
            fileChooser.setInitialFileName("report_" + LocalDate.now() + ".csv");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File file = fileChooser.showSaveDialog(this.getScene().getWindow());

            if (file != null) {
                String reportType = reportTypeCombo.getValue();
                try (FileWriter writer = new FileWriter(file)) {
                    switch (reportType) {
                        case "All Employees":
                        case "Active Employees":
                            exportEmployeesCSV(writer, reportType.equals("Active Employees"));
                            break;
                        case "All Payroll Records":
                            exportPayrollCSV(writer);
                            break;
                        case "Payroll Summary by Employee":
                        case "Year-to-Date Summary":
                            exportSummaryCSV(writer);
                            break;
                    }
                }

                showSuccess("Export Successful",
                        "Report has been exported to:\n" + file.getAbsolutePath());
            }

        } catch (IOException e) {
            showError("Export Error",
                    "Failed to export report to CSV.",
                    e.getMessage());
        }
    }

    /**
     * Exports employee data to CSV.
     */
    private void exportEmployeesCSV(FileWriter writer, boolean activeOnly) throws IOException {
        writer.write("Employee ID,First Name,Last Name,Email,Phone,Position,Type,Status\n");

        for (Employee emp : dataManager.getAllEmployees()) {
            if (activeOnly && emp.getStatus() != Employee.EmployeeStatus.ACTIVE) {
                continue;
            }

            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                    emp.getEmployeeId(),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getEmail() != null ? emp.getEmail() : "",
                    emp.getPhone() != null ? emp.getPhone() : "",
                    emp.getPosition() != null ? emp.getPosition() : "",
                    emp.getEmploymentType() != null ? emp.getEmploymentType().getDisplayName() : "",
                    emp.getStatus() != null ? emp.getStatus().getDisplayName() : ""
            ));
        }
    }

    /**
     * Exports payroll data to CSV.
     */
    private void exportPayrollCSV(FileWriter writer) throws IOException {
        writer.write("Payroll ID,Employee ID,Pay Period Start,Pay Period End,Gross Pay,Total Deductions,Net Pay\n");

        for (Payroll payroll : dataManager.getAllPayrolls()) {
            writer.write(String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f\n",
                    payroll.getPayrollId(),
                    payroll.getEmployeeId(),
                    payroll.getPayPeriodStart(),
                    payroll.getPayPeriodEnd(),
                    payroll.getGrossPay(),
                    payroll.getTotalDeductions(),
                    payroll.getNetPay()
            ));
        }
    }

    /**
     * Exports summary data to CSV.
     */
    private void exportSummaryCSV(FileWriter writer) throws IOException {
        writer.write("Employee ID,Employee Name,Total Gross Pay,Total Deductions,Total Net Pay\n");

        for (Employee emp : dataManager.getAllEmployees()) {
            PayrollCalculator.PayrollSummary summary =
                    PayrollCalculator.calculateYearToDateSummary(emp.getEmployeeId());

            if (summary.getTotalGrossPay() > 0) {
                writer.write(String.format("%s,%s,%.2f,%.2f,%.2f\n",
                        emp.getEmployeeId(),
                        emp.getFullName(),
                        summary.getTotalGrossPay(),
                        summary.getTotalDeductions(),
                        summary.getTotalNetPay()
                ));
            }
        }
    }

    /**
     * Prints the current report (opens print dialog).
     */
    private void printReport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Print Report");
        alert.setHeaderText("Print Functionality");
        alert.setContentText("Printing functionality would typically integrate with JavaFX PrinterJob.\n" +
                "For now, you can export to CSV and print from there.");
        alert.showAndWait();
    }

    /**
     * Shows an error dialog.
     */
    private void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-style: normal;");
        alert.showAndWait();
    }

    /**
     * Shows a success dialog.
     */
    private void showSuccess(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-style: normal;");
        alert.showAndWait();
    }
}
