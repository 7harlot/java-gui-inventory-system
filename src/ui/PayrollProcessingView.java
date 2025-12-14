package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Employee;
import model.Payroll;
import service.DataManager;
import service.PayrollCalculator;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Payroll Processing View for calculating and generating employee payroll.
 * Includes comprehensive validation and real-time calculation preview.
 *
 * @author [ADD YOUR NAMES HERE]
 * @version 1.0
 * @since 2025-11-24
 */
public class PayrollProcessingView extends BorderPane {

    private DataManager dataManager;
    private Runnable onBackToMenu;

    // Form fields
    private ComboBox<Employee> employeeCombo;
    private DatePicker payPeriodStartPicker;
    private DatePicker payPeriodEndPicker;
    private TextField regularHoursField;
    private TextField overtimeHoursField;
    private TextField bonusField;

    // Preview labels
    private Label grossPayLabel;
    private Label federalTaxLabel;
    private Label eiLabel;              // EI (Employment Insurance)
    private Label cppLabel;             // CPP (Canada Pension Plan)
    private Label totalDeductionsLabel;
    private Label netPayLabel;

    // Error labels
    private Label employeeError;
    private Label hoursError;
    private Label bonusError;
    private Label dateError;

    /**
     * Constructs the Payroll Processing View.
     *
     * @param onBackToMenu Callback to execute when back button is clicked
     */
    public PayrollProcessingView(Runnable onBackToMenu) {
        this.dataManager = DataManager.getInstance();
        this.onBackToMenu = onBackToMenu;
        initializeUI();
    }

    /**
     * Initializes all UI components and layouts.
     */
    private void initializeUI() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f5f5f5;");

        // Top section with back button and title
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

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
        Label titleLabel = new Label("Payroll Processing");
        titleLabel.setStyle("-fx-font-family: 'Arial', 'Segoe UI', sans-serif; " +
                "-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-style: normal; " +
                "-fx-text-fill: #E90064; -fx-effect: dropshadow(gaussian, #FF5F9E, 8, 0.6, 0, 0);");

        headerBox.getChildren().addAll(backButton, titleLabel);
        setTop(headerBox);

        // Main content in HBox (form on left, preview on right)
        HBox mainContent = new HBox(30);
        mainContent.setAlignment(Pos.TOP_LEFT);

        // Left side - Input form
        VBox formBox = createInputForm();
        formBox.setPrefWidth(500);

        // Right side - Preview
        VBox previewBox = createPreviewPanel();
        previewBox.setPrefWidth(350);

        mainContent.getChildren().addAll(formBox, previewBox);
        setCenter(mainContent);
    }

    /**
     * Creates the input form for payroll data.
     *
     * @return VBox containing the input form
     */
    private VBox createInputForm() {
        VBox formBox = new VBox(15);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 25;");

        Label formTitle = new Label("Payroll Information");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        int row = 0;

        // Employee selection
        Label employeeLabel = new Label("Select Employee:*");
        employeeLabel.setStyle("-fx-font-weight: bold;");
        employeeCombo = new ComboBox<>();
        employeeCombo.setPromptText("Choose an employee...");
        employeeCombo.setMaxWidth(Double.MAX_VALUE);
        employeeCombo.setOnAction(e -> onEmployeeSelected());
        loadEmployees();

        employeeError = createErrorLabel();

        grid.add(employeeLabel, 0, row);
        grid.add(employeeCombo, 1, row);
        grid.add(employeeError, 2, row);
        row++;

        // Pay period start
        Label startLabel = new Label("Pay Period Start:*");
        startLabel.setStyle("-fx-font-weight: bold;");
        payPeriodStartPicker = new DatePicker();
        payPeriodStartPicker.setValue(LocalDate.now().minusDays(14));
        payPeriodStartPicker.setMaxWidth(Double.MAX_VALUE);

        grid.add(startLabel, 0, row);
        grid.add(payPeriodStartPicker, 1, row);
        row++;

        // Pay period end
        Label endLabel = new Label("Pay Period End:*");
        endLabel.setStyle("-fx-font-weight: bold;");
        payPeriodEndPicker = new DatePicker();
        payPeriodEndPicker.setValue(LocalDate.now());
        payPeriodEndPicker.setMaxWidth(Double.MAX_VALUE);

        dateError = createErrorLabel();

        grid.add(endLabel, 0, row);
        grid.add(payPeriodEndPicker, 1, row);
        grid.add(dateError, 2, row);
        row++;

        // Regular hours
        Label regularHoursLabel = new Label("Regular Hours:*");
        regularHoursLabel.setStyle("-fx-font-weight: bold;");
        regularHoursField = new TextField();
        regularHoursField.setPromptText("e.g., 80");
        regularHoursField.textProperty().addListener((obs, old, newVal) -> calculatePreview());

        grid.add(regularHoursLabel, 0, row);
        grid.add(regularHoursField, 1, row);
        row++;

        // Overtime hours
        Label overtimeHoursLabel = new Label("Overtime Hours:");
        overtimeHoursLabel.setStyle("-fx-font-weight: bold;");
        overtimeHoursField = new TextField();
        overtimeHoursField.setPromptText("e.g., 5");
        overtimeHoursField.setText("0");
        overtimeHoursField.textProperty().addListener((obs, old, newVal) -> calculatePreview());

        hoursError = createErrorLabel();

        grid.add(overtimeHoursLabel, 0, row);
        grid.add(overtimeHoursField, 1, row);
        grid.add(hoursError, 2, row);
        row++;

        // Bonus
        Label bonusLabel = new Label("Bonus:");
        bonusLabel.setStyle("-fx-font-weight: bold;");
        bonusField = new TextField();
        bonusField.setPromptText("e.g., 500.00");
        bonusField.setText("0");
        bonusField.textProperty().addListener((obs, old, newVal) -> calculatePreview());

        bonusError = createErrorLabel();

        grid.add(bonusLabel, 0, row);
        grid.add(bonusField, 1, row);
        grid.add(bonusError, 2, row);
        row++;

        // Action buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button calculateBtn = createRetroButton("Calculate", "#B3005E", "#E90064");
        calculateBtn.setOnAction(e -> calculatePreview());

        Button processBtn = createRetroButton("Process & Save Payroll", "#E90064", "#FF5F9E");
        processBtn.setOnAction(e -> processPayroll());

        Button clearBtn = createRetroButton("Clear", "#060047", "#B3005E");
        clearBtn.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(calculateBtn, processBtn, clearBtn);

        // Required fields note
        Label requiredNote = new Label("* Required fields");
        requiredNote.setStyle("-fx-font-size: 11px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        formBox.getChildren().addAll(formTitle, grid, buttonBox, requiredNote);

        return formBox;
    }

    /**
     * Creates the preview panel showing payroll calculation breakdown.
     *
     * @return VBox containing the preview panel
     */
    private VBox createPreviewPanel() {
        VBox previewBox = new VBox(15);
        previewBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 25;");

        Label previewTitle = new Label("Payroll Preview");
        previewTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Earnings section
        VBox earningsBox = createSection("Earnings");
        grossPayLabel = createValueLabel("$0.00");
        earningsBox.getChildren().addAll(
                createRowLabel("Gross Pay:", grossPayLabel)
        );

        // Deductions section (Ontario)
        VBox deductionsBox = createSection("Deductions");
        federalTaxLabel = createValueLabel("$0.00");
        eiLabel = createValueLabel("$0.00");
        cppLabel = createValueLabel("$0.00");
        totalDeductionsLabel = createValueLabel("$0.00");

        deductionsBox.getChildren().addAll(
                createRowLabel("Federal Tax (15%):", federalTaxLabel),
                createRowLabel("EI (1.66%):", eiLabel),
                createRowLabel("CPP (5.95%):", cppLabel),
                new Separator(),
                createRowLabel("Total Deductions:", totalDeductionsLabel)
        );

        // Net pay section with retro glow
        VBox netPayBox = new VBox(10);
        netPayBox.setStyle("-fx-background-color: linear-gradient(to bottom, #E90064, #B3005E); " +
                "-fx-background-radius: 8; -fx-padding: 15; " +
                "-fx-effect: dropshadow(gaussian, #FF5F9E, 12, 0.7, 0, 0); " +
                "-fx-border-color: #FF5F9E; -fx-border-width: 2; -fx-border-radius: 8;");

        Label netPayTitleLabel = new Label("NET PAY");
        netPayTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        netPayLabel = new Label("$0.00");
        netPayLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, #060047, 4, 0.8, 0, 0);");

        netPayBox.getChildren().addAll(netPayTitleLabel, netPayLabel);
        netPayBox.setAlignment(Pos.CENTER);

        previewBox.getChildren().addAll(previewTitle, earningsBox, deductionsBox, netPayBox);

        return previewBox;
    }

    /**
     * Creates a section header with styling.
     */
    private VBox createSection(String title) {
        VBox section = new VBox(10);
        Label sectionLabel = new Label(title);
        sectionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        section.getChildren().add(sectionLabel);
        return section;
    }

    /**
     * Creates a row with label and value.
     */
    private HBox createRowLabel(String label, Label valueLabel) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-font-size: 13px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        valueLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        row.getChildren().addAll(textLabel, spacer, valueLabel);
        return row;
    }

    /**
     * Creates a value label for preview display.
     */
    private Label createValueLabel(String initialValue) {
        Label label = new Label(initialValue);
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        return label;
    }

    /**
     * Creates an error label for validation messages.
     */
    private Label createErrorLabel() {
        Label label = new Label();
        label.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
        label.setVisible(false);
        label.setWrapText(true);
        return label;
    }

    /**
     * Loads all active employees into the combo box.
     */
    private void loadEmployees() {
        try {
            employeeCombo.getItems().clear();
            for (Employee emp : dataManager.getAllEmployees()) {
                if (emp.getStatus() == Employee.EmployeeStatus.ACTIVE) {
                    employeeCombo.getItems().add(emp);
                }
            }
        } catch (Exception e) {
            showError("Error Loading Employees",
                    "Failed to load employees from database.",
                    e.getMessage());
        }
    }

    /**
     * Handles employee selection change.
     */
    private void onEmployeeSelected() {
        Employee selected = employeeCombo.getValue();
        if (selected != null) {
            // Pre-fill regular hours based on employment type
            if (selected.getEmploymentType() == Employee.EmploymentType.FULL_TIME) {
                regularHoursField.setText("80"); // 2 weeks * 40 hours
            } else if (selected.getEmploymentType() == Employee.EmploymentType.PART_TIME) {
                regularHoursField.setText("40"); // 2 weeks * 20 hours
            }
            calculatePreview();
        }
    }

    /**
     * Calculates and updates the payroll preview in real-time.
     */
    private void calculatePreview() {
        clearErrorMessages();

        Employee employee = employeeCombo.getValue();
        if (employee == null) {
            resetPreview();
            return;
        }

        try {
            // Parse input values
            double regularHours = parseDouble(regularHoursField.getText(), 0);
            double overtimeHours = parseDouble(overtimeHoursField.getText(), 0);
            double bonus = parseDouble(bonusField.getText(), 0);

            // Calculate payroll
            Payroll payroll;
            if (employee.getEmploymentType() == Employee.EmploymentType.FULL_TIME ||
                employee.getEmploymentType() == Employee.EmploymentType.CONTRACT) {
                payroll = PayrollCalculator.calculateSalariedPayroll(
                        employee, bonus,
                        payPeriodStartPicker.getValue(),
                        payPeriodEndPicker.getValue()
                );
            } else {
                payroll = PayrollCalculator.calculateHourlyPayroll(
                        employee, regularHours, overtimeHours, bonus,
                        payPeriodStartPicker.getValue(),
                        payPeriodEndPicker.getValue()
                );
            }

            // Update preview
            updatePreview(payroll);

        } catch (NumberFormatException e) {
            // Invalid number format - reset preview
            resetPreview();
        } catch (Exception e) {
            showError("Calculation Error",
                    "Failed to calculate payroll.",
                    e.getMessage());
            resetPreview();
        }
    }

    /**
     * Updates the preview panel with calculated payroll values.
     */
    private void updatePreview(Payroll payroll) {
        grossPayLabel.setText(String.format("$%.2f", payroll.getGrossPay()));
        federalTaxLabel.setText(String.format("$%.2f", payroll.getFederalTax()));
        eiLabel.setText(String.format("$%.2f", payroll.getStateTax()));              // EI stored in stateTax field
        cppLabel.setText(String.format("$%.2f", payroll.getSocialSecurity()));       // CPP stored in socialSecurity field
        totalDeductionsLabel.setText(String.format("$%.2f", payroll.getTotalDeductions()));
        netPayLabel.setText(String.format("$%.2f", payroll.getNetPay()));
    }

    /**
     * Resets the preview to zero values.
     */
    private void resetPreview() {
        grossPayLabel.setText("$0.00");
        federalTaxLabel.setText("$0.00");
        eiLabel.setText("$0.00");
        cppLabel.setText("$0.00");
        totalDeductionsLabel.setText("$0.00");
        netPayLabel.setText("$0.00");
    }

    /**
     * Validates and processes the payroll, saving to database.
     */
    private void processPayroll() {
        if (!validateForm()) {
            return;
        }

        try {
            Employee employee = employeeCombo.getValue();
            double regularHours = parseDouble(regularHoursField.getText(), 0);
            double overtimeHours = parseDouble(overtimeHoursField.getText(), 0);
            double bonus = parseDouble(bonusField.getText(), 0);

            // Calculate final payroll
            Payroll payroll;
            if (employee.getEmploymentType() == Employee.EmploymentType.FULL_TIME ||
                employee.getEmploymentType() == Employee.EmploymentType.CONTRACT) {
                payroll = PayrollCalculator.calculateSalariedPayroll(
                        employee, bonus,
                        payPeriodStartPicker.getValue(),
                        payPeriodEndPicker.getValue()
                );
            } else {
                payroll = PayrollCalculator.calculateHourlyPayroll(
                        employee, regularHours, overtimeHours, bonus,
                        payPeriodStartPicker.getValue(),
                        payPeriodEndPicker.getValue()
                );
            }

            // Save to database
            dataManager.addPayroll(payroll);

            // Show success
            showSuccess("Payroll Processed",
                    String.format("Payroll for %s has been processed successfully.\nNet Pay: $%.2f",
                            employee.getFullName(), payroll.getNetPay()));

            // Clear form
            clearForm();

        } catch (Exception e) {
            showError("Processing Error",
                    "Failed to process payroll.",
                    e.getMessage());
        }
    }

    /**
     * Validates all form inputs.
     *
     * @return true if validation passes, false otherwise
     */
    private boolean validateForm() {
        boolean isValid = true;
        clearErrorMessages();

        // Validate employee selection
        if (employeeCombo.getValue() == null) {
            showErrorLabel(employeeError, "Please select an employee");
            isValid = false;
        }

        // Validate dates
        if (payPeriodStartPicker.getValue() == null || payPeriodEndPicker.getValue() == null) {
            showErrorLabel(dateError, "Both dates are required");
            isValid = false;
        } else if (payPeriodEndPicker.getValue().isBefore(payPeriodStartPicker.getValue())) {
            showErrorLabel(dateError, "End date must be after start date");
            isValid = false;
        }

        // Validate hours
        try {
            double regularHours = parseDouble(regularHoursField.getText(), -1);
            if (regularHours < 0) {
                showErrorLabel(hoursError, "Regular hours are required");
                isValid = false;
            } else if (regularHours > 200) {
                showErrorLabel(hoursError, "Regular hours seem unreasonably high");
                isValid = false;
            }

            double overtimeHours = parseDouble(overtimeHoursField.getText(), 0);
            if (overtimeHours < 0) {
                showErrorLabel(hoursError, "Overtime hours cannot be negative");
                isValid = false;
            } else if (overtimeHours > 80) {
                showErrorLabel(hoursError, "Overtime hours seem unreasonably high");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            showErrorLabel(hoursError, "Hours must be valid numbers");
            isValid = false;
        }

        // Validate bonus
        try {
            double bonus = parseDouble(bonusField.getText(), 0);
            if (bonus < 0) {
                showErrorLabel(bonusError, "Bonus cannot be negative");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            showErrorLabel(bonusError, "Bonus must be a valid number");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Shows an error message on a validation label.
     */
    private void showErrorLabel(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    /**
     * Clears all error messages.
     */
    private void clearErrorMessages() {
        employeeError.setVisible(false);
        hoursError.setVisible(false);
        bonusError.setVisible(false);
        dateError.setVisible(false);
    }

    /**
     * Clears the form and resets to default values.
     */
    private void clearForm() {
        employeeCombo.setValue(null);
        payPeriodStartPicker.setValue(LocalDate.now().minusDays(14));
        payPeriodEndPicker.setValue(LocalDate.now());
        regularHoursField.clear();
        overtimeHoursField.setText("0");
        bonusField.setText("0");
        resetPreview();
        clearErrorMessages();
    }

    /**
     * Safely parses a double from a string.
     */
    private double parseDouble(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Double.parseDouble(value.trim());
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

    /**
     * Creates a retro-styled button with neon colors.
     *
     * @param text       The button text
     * @param bgColor    The background color
     * @param hoverColor The hover color
     * @return Styled Button with retro neon appearance
     */
    private Button createRetroButton(String text, String bgColor, String hoverColor) {
        Button button = new Button(text);
        button.setMinWidth(Button.USE_PREF_SIZE); // Allow button to grow to fit text
        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20; " +
                "-fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, %s, 8, 0.6, 0, 0);",
                bgColor, hoverColor, hoverColor));

        button.setOnMouseEntered(e -> button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20; " +
                "-fx-background-radius: 8; -fx-border-color: #FF5F9E; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, #FF5F9E, 15, 0.8, 0, 0);",
                hoverColor)));

        button.setOnMouseExited(e -> button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-family: 'Arial', 'Segoe UI', sans-serif; -fx-font-style: normal; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20; " +
                "-fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; " +
                "-fx-border-radius: 8; -fx-effect: dropshadow(gaussian, %s, 8, 0.6, 0, 0);",
                bgColor, hoverColor, hoverColor)));

        return button;
    }
}
