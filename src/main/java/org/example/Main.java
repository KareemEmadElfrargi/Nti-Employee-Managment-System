package org.example;

import org.example.Service.EmployeeService;
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

            System.out.println("Before raise: " + employee);

            Employee raised = employeeService.giveRaise(employee.getId(), 10);
            System.out.println("After 10% raise: " + raised);

            System.out.println("\n-- Bean scope demonstration --");

            AuditLogger auditLogger1 = context.getBean(AuditLogger.class);
            AuditLogger auditLogger2 = context.getBean(AuditLogger.class);
            auditLogger1.log("first audit event");
            auditLogger2.log("second audit event");
            System.out.println("AuditLogger (prototype) same instance? " + (auditLogger1 == auditLogger2));

            NotificationManager notificationManager1 = context.getBean(NotificationManager.class);
            NotificationManager notificationManager2 = context.getBean(NotificationManager.class);
            System.out.println("NotificationManager (singleton) same instance? " + (notificationManager1 == notificationManager2));

            System.out.println("\n-- Closing context (triggers @PreDestroy) --");
        } finally {
            context.close();
        }
    }
}
