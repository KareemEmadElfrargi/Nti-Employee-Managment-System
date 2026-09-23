package org.example.repository;

import org.example.Employee;

import java.util.List;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Employee findById(int id);

    List<Employee> findAll();
}
