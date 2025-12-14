# Javadoc Style Guide for HR Management System

## What is Javadoc?

Javadoc is a documentation standard for Java that uses special comment blocks starting with `/**` and ending with `*/`. These comments can generate HTML documentation and help other developers understand your code.

## Javadoc Tags

Common Javadoc tags you should use:

- `@author` - Author of the class
- `@version` - Version information
- `@param` - Describes a method parameter
- `@return` - Describes what a method returns
- `@throws` or `@exception` - Documents exceptions thrown
- `@see` - References to other classes/methods
- `@since` - When the feature was added
- `@deprecated` - Marks deprecated methods

## Examples from Our Project

### Class-Level Javadoc

```java
/**
 * Manages all employee data and provides CRUD operations.
 * This class uses the Singleton pattern to ensure only one instance
 * manages the employee data throughout the application.
 *
 * <p>Data is persisted using Java serialization to the file system.
 * All changes are automatically saved when modifications are made.</p>
 *
 * @author [Your Name Here]
 * @version 1.0
 * @since 2025-11-24
 */
public class DataManager {
    // class implementation
}
```

### Constructor Javadoc

```java
/**
 * Constructs a new Employee with the specified details.
 * The employee is automatically assigned an ACTIVE status and
 * the hire date is set to the current date.
 *
 * @param employeeId   Unique identifier for the employee (e.g., "EMP001")
 * @param firstName    Employee's first name
 * @param lastName     Employee's last name
 * @param email        Employee's email address for communication
 */
public Employee(String employeeId, String firstName, String lastName, String email) {
    // constructor implementation
}
```

### Method Javadoc with Return Value

```java
/**
 * Calculates the payroll for an hourly employee including regular pay,
 * overtime pay, taxes, and other deductions.
 *
 * <p>Overtime is calculated at 1.5x the regular hourly rate for any hours
 * worked beyond the standard 40 hours per week.</p>
 *
 * @param employee         The employee to calculate payroll for
 * @param regularHours     Number of regular hours worked (typically 0-40)
 * @param overtimeHours    Number of overtime hours worked
 * @param bonus            Additional bonus amount to include
 * @param payPeriodStart   Start date of the pay period
 * @param payPeriodEnd     End date of the pay period
 * @return A calculated Payroll object with all earnings and deductions
 * @throws IllegalArgumentException if hours or bonus are negative
 */
public static Payroll calculateHourlyPayroll(Employee employee,
                                             double regularHours,
                                             double overtimeHours,
                                             double bonus,
                                             LocalDate payPeriodStart,
                                             LocalDate payPeriodEnd) {
    // method implementation
}
```

### Method Javadoc with No Return (void)

```java
/**
 * Adds a new employee to the data store and persists the changes.
 * If an employee with the same ID already exists, it will be replaced.
 *
 * <p>This method automatically triggers serialization to save
 * the updated employee list to disk.</p>
 *
 * @param employee The employee object to add
 * @see #updateEmployee(Employee)
 * @see #deleteEmployee(String)
 */
public void addEmployee(Employee employee) {
    // method implementation
}
```

### Getter/Setter Javadoc (Simplified)

For simple getters and setters, you can use brief Javadoc:

```java
/**
 * Gets the employee's email address.
 * @return The employee's email
 */
public String getEmail() {
    return email;
}

/**
 * Sets the employee's email address.
 * @param email The email to set
 */
public void setEmail(String email) {
    this.email = email;
}
```

## Best Practices

1. **Write Javadoc for all public methods and classes**
2. **Explain the "why" not just the "what"** - Don't just repeat the method name
3. **Document parameters and return values** - Always use @param and @return tags
4. **Use proper grammar** - Complete sentences with proper punctuation
5. **Keep it concise but informative** - Balance between too brief and too verbose
6. **Update Javadoc when code changes** - Keep documentation in sync

## What to Document

### Must Have Javadoc:
- All public classes
- All public methods
- Constructors
- Important private methods with complex logic

### Optional but Recommended:
- Package-level documentation (package-info.java)
- All getter/setter methods
- Enums and their values

## Generating HTML Documentation

To generate HTML documentation from your Javadoc comments:

```bash
# From your project root
javadoc -d docs -sourcepath src -subpackages model:service
```

This will create HTML documentation in a `docs` folder.

## For Your Assignment

Make sure to:
1. Add `@author` with all team member names at the class level
2. Document all complex methods in PayrollCalculator
3. Document all public methods in DataManager
4. Add @param and @return tags where appropriate
5. Generate the HTML docs and include screenshots in your report

## Quick Checklist

- [ ] Class-level Javadoc on all classes (Employee, Department, Payroll, DataManager, PayrollCalculator, Main)
- [ ] Constructor Javadoc with @param tags
- [ ] Public method Javadoc with @param and @return tags
- [ ] Complex private method Javadoc
- [ ] @author tags with team member names
- [ ] Generated HTML documentation (optional but impressive)