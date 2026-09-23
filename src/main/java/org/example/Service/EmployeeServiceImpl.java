package org.example.Service;

import org.example.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
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

        return employeeRepository.save(employee);
    }
}
