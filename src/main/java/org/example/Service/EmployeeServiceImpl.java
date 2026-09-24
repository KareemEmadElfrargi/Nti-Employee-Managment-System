package org.example.Service;

import org.example.model.Employee;
import org.example.notification.NotificationManager;
import org.example.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final NotificationManager notificationManager;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, NotificationManager notificationManager) {
        this.employeeRepository = employeeRepository;
        this.notificationManager = notificationManager;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        Employee saved = employeeRepository.save(employee);
        notificationManager.notifyAll("New employee added: " + saved.getName() + " (id " + saved.getId() + ")");
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

        Employee updated = employeeRepository.save(employee);
        notificationManager.notifyAll(updated.getName() + " (id " + updated.getId() + ") received a " + percentage + "% raise, new salary $" + String.format("%.2f", updated.getSalary()));
        return updated;
    }
}
