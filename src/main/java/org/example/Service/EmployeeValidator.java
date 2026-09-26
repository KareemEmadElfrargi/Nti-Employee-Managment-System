package org.example.Service;

import org.example.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {

    public void validate(Employee employee) {
        if (employee == null) {
            throw new InvalidEmployeeException("Employee must not be null");
        }
        if (employee.getName() == null || employee.getName().isBlank()) {
            throw new InvalidEmployeeException("Employee name must not be blank");
        }
        if (employee.getDepartment() == null || employee.getDepartment().isBlank()) {
            throw new InvalidEmployeeException("Employee department must not be blank");
        }
        if (employee.getSalary() < 0) {
            throw new InvalidEmployeeException("Employee salary must not be negative");
        }
    }
}
