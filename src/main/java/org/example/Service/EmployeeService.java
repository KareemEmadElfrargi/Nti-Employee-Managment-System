package org.example.Service;

import org.example.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee addEmployee(Employee employee);

    Employee getEmployeeById(int id);

    List<Employee> getAllEmployees();

    Employee giveRaise(int id, double percentage);
}
