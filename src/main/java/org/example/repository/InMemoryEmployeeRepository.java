package org.example.repository;

import org.example.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Repository
@Profile("dev")
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<Integer, Employee> employees = new LinkedHashMap<>();

    @Override
    public Employee save(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public Employee findById(int id) {
        return employees.get(id);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }
}
