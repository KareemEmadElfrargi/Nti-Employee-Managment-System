package org.example.Service;

import org.example.audit.AuditLogger;
import org.example.model.Employee;
import org.example.notification.NotificationManager;
import org.example.repository.EmployeeRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final NotificationManager notificationManager;
    private final EmployeeValidator employeeValidator;
    private final ObjectProvider<AuditLogger> auditLoggerProvider;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, NotificationManager notificationManager,
                                EmployeeValidator employeeValidator, ObjectProvider<AuditLogger> auditLoggerProvider) {
        this.employeeRepository = employeeRepository;
        this.notificationManager = notificationManager;
        this.employeeValidator = employeeValidator;
        this.auditLoggerProvider = auditLoggerProvider;
    }

    @PostConstruct
    public void init() {
        System.out.println("EmployeeServiceImpl initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("EmployeeServiceImpl destroyed");
    }

    @Override
    public Employee addEmployee(Employee employee) {
        employeeValidator.validate(employee);
        Employee saved = employeeRepository.save(employee);
        notificationManager.notifyAll("New employee added: " + saved.getName() + " (id " + saved.getId() + ")");
        auditLoggerProvider.getObject().log("addEmployee: " + saved.getName() + " (id " + saved.getId() + ")");
        return saved;
    }

    @Override
    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee giveRaise(int id, double percentage) {
        if (percentage <= 0) {
            throw new IllegalArgumentException("Raise percentage must be positive");
        }

        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new NoSuchElementException("No employee found with id " + id);
        }

        double newSalary = employee.getSalary() * (1 + percentage / 100);
        employee.setSalary(newSalary);
        employeeValidator.validate(employee);

        Employee updated = employeeRepository.save(employee);
        notificationManager.notifyAll(updated.getName() + " (id " + updated.getId() + ") received a " + percentage + "% raise, new salary $" + String.format("%.2f", updated.getSalary()));
        auditLoggerProvider.getObject().log("giveRaise: " + updated.getName() + " (id " + updated.getId() + ") -> $" + String.format("%.2f", updated.getSalary()));
        return updated;
    }
}
