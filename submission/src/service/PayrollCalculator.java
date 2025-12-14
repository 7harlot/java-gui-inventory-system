package service;

import model.Employee;
import model.Payroll;

import java.time.LocalDate;

/**
 * Handles payroll calculations including taxes, deductions, and net pay.
 * This utility class provides static methods for calculating payroll for both
 * hourly and salaried employees, including all standard deductions.
 *
 * <p>Tax rates are based on Ontario, Canada tax rates (simplified) and can be adjusted
 * by modifying the constants. The calculator handles:</p>
 * <ul>
 *   <li>Regular and overtime pay calculation</li>
 *   <li>Federal tax deductions (Canada)</li>
 *   <li>EI (Employment Insurance) contributions</li>
 *   <li>CPP (Canada Pension Plan) contributions</li>
 *   <li>Year-to-date payroll summaries</li>
 * </ul>
 *
 * @author [ADD YOUR NAMES HERE]
 * @version 1.0
 * @since 2025-11-24
 */
public class PayrollCalculator {

    // Ontario tax rates (simplified for demonstration)
    private static final double FEDERAL_TAX_RATE = 0.15;      // 15% federal tax (simplified)
    private static final double EI_RATE = 0.0166;             // 1.66% Employment Insurance
    private static final double CPP_RATE = 0.0595;            // 5.95% Canada Pension Plan
    private static final double OVERTIME_MULTIPLIER = 1.5;     // Time and a half
    private static final double STANDARD_WORK_HOURS = 40.0;    // Standard hours per week

    /**
     * Calculates payroll for an hourly employee including regular pay, overtime,
     * taxes, and all standard deductions.
     *
     * <p>Overtime is calculated at 1.5x the employee's regular hourly rate.
     * All applicable taxes (federal, cpp, ei, etc) are
     * automatically calculated and deducted from gross pay.</p>
     *
     * @param employee         The employee to calculate payroll for (must have hourly rate set)
     * @param regularHours     Number of regular hours worked (typically 0-40 per week)
     * @param overtimeHours    Number of overtime hours worked (typically > 40 hours/week)
     * @param bonus            Additional bonus amount to add to gross pay
     * @param payPeriodStart   Start date of the pay period
     * @param payPeriodEnd     End date of the pay period
     * @return A fully calculated Payroll object with gross pay, deductions, and net pay
     */
    public static Payroll calculateHourlyPayroll(Employee employee, double regularHours,
                                                   double overtimeHours, double bonus,
                                                   LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        // Generate unique payroll ID
        String payrollId = generatePayrollId(employee.getEmployeeId(), payPeriodEnd);

        // Create new payroll record
        Payroll payroll = new Payroll(payrollId, employee.getEmployeeId(), payPeriodStart, payPeriodEnd);

        // Set hours and rate
        payroll.setRegularHours(regularHours);
        payroll.setOvertimeHours(overtimeHours);
        payroll.setHourlyRate(employee.getHourlyRate());

        // Calculate earnings
        double regularPay = regularHours * employee.getHourlyRate();
        double overtimePay = overtimeHours * employee.getHourlyRate() * OVERTIME_MULTIPLIER;
        double grossPay = regularPay + overtimePay + bonus;

        payroll.setRegularPay(regularPay);
        payroll.setOvertimePay(overtimePay);
        payroll.setBonus(bonus);
        payroll.setGrossPay(grossPay);

        // Calculate deductions
        calculateDeductions(payroll, grossPay);

        // Calculate net pay
        payroll.calculateNetPay();

        return payroll;
    }

    /**
     * Calculates payroll for a salaried employee based on their annual salary.
     *
     * <p>The annual salary is divided into 26 bi-weekly pay periods. All applicable
     * taxes and deductions are automatically calculated.</p>
     *
     * @param employee         The employee to calculate payroll for (must have annual salary set)
     * @param bonus            Additional bonus amount to add to gross pay
     * @param payPeriodStart   Start date of the pay period
     * @param payPeriodEnd     End date of the pay period
     * @return A fully calculated Payroll object with gross pay, deductions, and net pay
     */
    public static Payroll calculateSalariedPayroll(Employee employee, double bonus,
                                                     LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        // Generate unique payroll ID
        String payrollId = generatePayrollId(employee.getEmployeeId(), payPeriodEnd);

        // Create new payroll record
        Payroll payroll = new Payroll(payrollId, employee.getEmployeeId(), payPeriodStart, payPeriodEnd);

        // Calculate bi-weekly salary (assuming 26 pay periods per year)
        double biWeeklySalary = employee.getAnnualSalary() / 26.0;
        double grossPay = biWeeklySalary + bonus;

        payroll.setRegularPay(biWeeklySalary);
        payroll.setBonus(bonus);
        payroll.setGrossPay(grossPay);

        // Calculate deductions
        calculateDeductions(payroll, grossPay);

        // Calculate net pay
        payroll.calculateNetPay();

        return payroll;
    }

    /**
     * Calculates all tax deductions based on gross pay including federal tax,
     * EI (Employment Insurance), and CPP (Canada Pension Plan) for Ontario.
     *
     * @param payroll  The payroll object to update with calculated deductions
     * @param grossPay The gross pay amount before deductions
     */
    private static void calculateDeductions(Payroll payroll, double grossPay) {
        // Ontario tax deductions
        double federalTax = grossPay * FEDERAL_TAX_RATE;
        double ei = grossPay * EI_RATE;           // Employment Insurance (using stateTax field)
        double cpp = grossPay * CPP_RATE;         // Canada Pension Plan (using socialSecurity field)

        payroll.setFederalTax(federalTax);
        payroll.setStateTax(ei);                  // Repurposed for EI
        payroll.setSocialSecurity(cpp);           // Repurposed for CPP
        payroll.setMedicare(0.0);                 // Not used in Ontario

        // Additional deductions (these would typically be employee-specific)
        // For now, using default values - you can make these configurable
        payroll.setHealthInsurance(0.0);
        payroll.setRetirement401k(0.0);
        payroll.setOtherDeductions(0.0);
    }

    /**
     * Generates a unique payroll ID based on employee ID and pay date.
     * Format: "PAY-{employeeId}-{date}"
     *
     * @param employeeId The employee's unique identifier
     * @param date       The end date of the pay period
     * @return A unique payroll identifier string
     */
    private static String generatePayrollId(String employeeId, LocalDate date) {
        return String.format("PAY-%s-%s", employeeId, date.toString());
    }

    /**
     * Calculates the total taxes for a given payroll record.
     * Includes federal tax, state tax, Social Security, and Medicare.
     *
     * @param payroll The payroll record to calculate taxes for
     * @return The sum of all tax deductions
     */
    public static double calculateTotalTaxes(Payroll payroll) {
        return payroll.getFederalTax() +
               payroll.getStateTax() +
               payroll.getSocialSecurity() +
               payroll.getMedicare();
    }

    /**
     * Calculates year-to-date payroll totals for a specific employee.
     * Only includes payroll records from the current calendar year.
     *
     * @param employeeId The employee's unique identifier
     * @return A PayrollSummary object containing YTD totals for gross pay, deductions, and net pay
     */
    public static PayrollSummary calculateYearToDateSummary(String employeeId) {
        DataManager dataManager = DataManager.getInstance();
        var payrolls = dataManager.getPayrollsByEmployee(employeeId);

        double totalGrossPay = 0.0;
        double totalDeductions = 0.0;
        double totalNetPay = 0.0;

        for (Payroll payroll : payrolls) {
            if (payroll.getPayPeriodEnd().getYear() == LocalDate.now().getYear()) {
                totalGrossPay += payroll.getGrossPay();
                totalDeductions += payroll.getTotalDeductions();
                totalNetPay += payroll.getNetPay();
            }
        }

        return new PayrollSummary(employeeId, totalGrossPay, totalDeductions, totalNetPay);
    }

    /**
     * Inner class representing a summary of payroll totals for an employee.
     * Used for year-to-date and period summary reporting.
     */
    public static class PayrollSummary {
        private String employeeId;
        private double totalGrossPay;
        private double totalDeductions;
        private double totalNetPay;

        /**
         * Constructs a new PayrollSummary with the specified totals.
         *
         * @param employeeId       The employee's unique identifier
         * @param totalGrossPay    The sum of all gross pay
         * @param totalDeductions  The sum of all deductions
         * @param totalNetPay      The sum of all net pay
         */
        public PayrollSummary(String employeeId, double totalGrossPay,
                            double totalDeductions, double totalNetPay) {
            this.employeeId = employeeId;
            this.totalGrossPay = totalGrossPay;
            this.totalDeductions = totalDeductions;
            this.totalNetPay = totalNetPay;
        }

        /**
         * Gets the employee ID
         * @return The employee's unique identifier
         */
        public String getEmployeeId() { return employeeId; }

        /**
         * Gets the total gross pay
         * @return The sum of all gross pay amounts
         */
        public double getTotalGrossPay() { return totalGrossPay; }

        /**
         * Gets the total deductions
         * @return The sum of all deduction amounts
         */
        public double getTotalDeductions() { return totalDeductions; }

        /**
         * Gets the total net pay
         * @return The sum of all net pay amounts
         */
        public double getTotalNetPay() { return totalNetPay; }
    }
}