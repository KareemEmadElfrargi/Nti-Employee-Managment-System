package org.example.repository;

import org.example.model.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("prod")
public class FileBackedEmployeeRepository implements EmployeeRepository {

    private static final String DELIMITER = ",";
    private final Path file = Path.of("employees.csv");

    @Override
    public Employee save(Employee employee) {
        List<Employee> employees = findAll();
        employees.removeIf(e -> e.getId() == employee.getId());
        employees.add(employee);
        writeAll(employees);
        return employee;
    }

    @Override
    public Employee findById(int id) {
        return findAll().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }

        try {
            List<Employee> employees = new ArrayList<>();
            for (String line : Files.readAllLines(file)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(DELIMITER, -1);
                employees.add(new Employee(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3])
                ));
            }
            return employees;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read employees from " + file, e);
        }
    }

    private void writeAll(List<Employee> employees) {
        try {
            List<String> lines = employees.stream()
                    .map(e -> String.join(DELIMITER,
                            String.valueOf(e.getId()),
                            e.getName(),
                            e.getDepartment(),
                            String.valueOf(e.getSalary())))
                    .toList();
            Files.write(file, lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write employees to " + file, e);
        }
    }
}
