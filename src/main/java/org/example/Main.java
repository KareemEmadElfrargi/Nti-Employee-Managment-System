package org.example;

import org.example.Service.EmployeeService;
import org.example.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        String activeProfile = args.length > 0 ? args[0] : "dev";

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("prod");
            context.register(AppConfig.class);
            context.refresh();


            EmployeeService employeeService = context.getBean(EmployeeService.class);
            Employee employee = employeeService.addEmployee(new Employee(1, "Kareem Emad", "CS", 5000));

            System.out.println("Before raise: " + employee);

            Employee raised = employeeService.giveRaise(employee.getId(), 10);
            System.out.println("After 10% raise: " + raised);
        }
    }
}
