package org.example;

import org.example.Service.EmployeeService;
import org.example.Service.EmployeeServiceImpl;
import org.example.Service.InvalidEmployeeException;
import org.example.audit.AuditLogger;
import org.example.config.AppConfig;
import org.example.model.Employee;
import org.example.notification.NotificationManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        String activeProfile = args.length > 0 ? args[0] : "dev";

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext();
        try {
            context.getEnvironment().setActiveProfiles(activeProfile);
            ((AnnotationConfigApplicationContext) context).register(AppConfig.class);
            context.refresh();

            EmployeeService employeeService = context.getBean(EmployeeService.class);

            Employee employee = employeeService.addEmployee(new Employee(1, "Kareem Emad", "CS", 111111));
            System.out.println("Added: " + employee);

            try {
                employeeService.addEmployee(new Employee(2, "", "HR", 60000));
            } catch (InvalidEmployeeException e) {
                System.out.println("Validation failed as expected: " + e.getMessage());
            }

            Employee raised = employeeService.giveRaise(employee.getId(), 10);
            System.out.println("After 10% raise (within limit): " + raised);

            try {
                employeeService.giveRaise(employee.getId(), 30);
            } catch (IllegalArgumentException e) {
                System.out.println("Raise rejected as expected: " + e.getMessage());
            }

            AuditLogger auditLogger1 = context.getBean(AuditLogger.class);
            AuditLogger auditLogger2 = context.getBean(AuditLogger.class);
            AuditLogger auditLogger3 = context.getBean(AuditLogger.class);
            auditLogger1.log("first audit event");
            auditLogger2.log("second audit event");
            auditLogger3.log("third audit event");
            System.out.println("AuditLogger same instance across requests? "
                    + (auditLogger1 == auditLogger2 || auditLogger2 == auditLogger3));

            NotificationManager notificationManager1 = context.getBean(NotificationManager.class);
            NotificationManager notificationManager2 = context.getBean(NotificationManager.class);
            System.out.println("NotificationManager same instance? " + (notificationManager1 == notificationManager2));

            System.out.println("\n-- Listing all employees --");
            employeeService.getAllEmployees().forEach(System.out::println);

            EmployeeServiceImpl employeeServiceImpl = context.getBean(EmployeeServiceImpl.class);
            System.out.println("name of company = " + employeeServiceImpl.getCompanyName());
            System.out.println("currency of company = " + employeeServiceImpl.getCompanyCurrency());
            System.out.println("raise max percentage = " + employeeServiceImpl.getMaxRaisePercentage());
            System.out.println("notification retry count = " + notificationManager1.getRetryCount());

        } finally {
            context.close();
        }
    }
}
