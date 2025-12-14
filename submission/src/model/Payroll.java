package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a payroll record for an employee
 * Implements Serializable for data persistence
 *
 * Group Members: [ADD YOUR NAMES HERE]
 */
public class Payroll implements Serializable {
    private static final long serialVersionUID = 1L;

    private String payrollId;
    private String employeeId;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private LocalDate payDate;

    // Hours and Rate
    private double regularHours;
    private double overtimeHours;
    private double hourlyRate;

    // Earnings
    private double grossPay;
    private double regularPay;
    private double overtimePay;
    private double bonus;

    // Deductions
    private double federalTax;
    private double stateTax;
    private double socialSecurity;
    private double medicare;
    private double healthInsurance;
    private double retirement401k;
    private double otherDeductions;

    // Net Pay
    private double totalDeductions;
    private double netPay;

    /**
     * Constructor for creating a new payroll record
     */
    public Payroll(String payrollId, String employeeId, LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        this.payrollId = payrollId;
        this.employeeId = employeeId;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.payDate = LocalDate.now();
    }

    // Getters and Setters
    public String getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(String payrollId) {
        this.payrollId = payrollId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getPayPeriodStart() {
        return payPeriodStart;
    }

    public void setPayPeriodStart(LocalDate payPeriodStart) {
        this.payPeriodStart = payPeriodStart;
    }

    public LocalDate getPayPeriodEnd() {
        return payPeriodEnd;
    }

    public void setPayPeriodEnd(LocalDate payPeriodEnd) {
        this.payPeriodEnd = payPeriodEnd;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public double getRegularHours() {
        return regularHours;
    }

    public void setRegularHours(double regularHours) {
        this.regularHours = regularHours;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getRegularPay() {
        return regularPay;
    }

    public void setRegularPay(double regularPay) {
        this.regularPay = regularPay;
    }

    public double getOvertimePay() {
        return overtimePay;
    }

    public void setOvertimePay(double overtimePay) {
        this.overtimePay = overtimePay;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getFederalTax() {
        return federalTax;
    }

    public void setFederalTax(double federalTax) {
        this.federalTax = federalTax;
    }

    public double getStateTax() {
        return stateTax;
    }

    public void setStateTax(double stateTax) {
        this.stateTax = stateTax;
    }

    public double getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(double socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public double getMedicare() {
        return medicare;
    }

    public void setMedicare(double medicare) {
        this.medicare = medicare;
    }

    public double getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(double healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public double getRetirement401k() {
        return retirement401k;
    }

    public void setRetirement401k(double retirement401k) {
        this.retirement401k = retirement401k;
    }

    public double getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(double otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    /**
     * Calculate total deductions
     */
    public void calculateTotalDeductions() {
        this.totalDeductions = federalTax + stateTax + socialSecurity +
                               medicare + healthInsurance + retirement401k + otherDeductions;
    }

    /**
     * Calculate net pay (gross pay - total deductions)
     */
    public void calculateNetPay() {
        calculateTotalDeductions();
        this.netPay = grossPay - totalDeductions;
    }

    @Override
    public String toString() {
        return String.format("Payroll %s - Employee: %s, Period: %s to %s, Net Pay: $%.2f",
                payrollId, employeeId, payPeriodStart, payPeriodEnd, netPay);
    }
}