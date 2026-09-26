# Spring Core — Mini Project Assignment

**Employee Management System — Applying IoC, DI & Bean Configuration**

---

## Project Overview

Now that you have covered the fundamentals of the Spring Core module — the IoC container, Dependency Injection, bean configuration, scopes, and the bean lifecycle — you will apply that knowledge by building a small, working Employee Management System. The application itself is intentionally simple: the real goal is to demonstrate that you understand how the Spring container creates, wires, and manages your objects.

You will build a plain Spring (non-Boot) application using a Java-based configuration class and the Spring `ApplicationContext`, then extend it with annotation-based configuration, bean scopes, and lifecycle callbacks.

## Learning Objectives

By completing this project, you should be able to:

- Explain and demonstrate the Inversion of Control (IoC) principle in a working application.
- Configure beans using both Java-based configuration (`@Configuration`/`@Bean`) and annotation-based configuration (`@Component`, `@Service`, `@Repository`).
- Apply Dependency Injection using constructor injection, setter injection, and field injection, and explain when each is appropriate.
- Use `@Autowired`, `@Qualifier`, and `@Primary` to resolve ambiguous dependencies.
- Configure and justify the use of different bean scopes (singleton vs. prototype).
- Use bean lifecycle callbacks (`@PostConstruct` / `@PreDestroy` or `InitializingBean` / `DisposableBean`).
- Externalize configuration values using `@Value` and a `.properties` file.

---

## Project Requirements

### 1. Domain & Repository Layer

- Create an `Employee` class with fields such as `id`, `name`, `department`, and `salary`.
- Create an `EmployeeRepository` interface with methods like `save(Employee)`, `findById(int)`, and `findAll()`.
- Provide at least one implementation, e.g. `InMemoryEmployeeRepository`, storing data in a `List` or `Map` (no real database needed).

### 2. Service Layer

- Create an `EmployeeService` interface and an implementation (e.g. `EmployeeServiceImpl`) that depends on `EmployeeRepository` — injected, not instantiated with `new`.
- The service should expose at least: `addEmployee()`, `getEmployeeById()`, `getAllEmployees()`, and one method with real business logic (e.g. `giveRaise(int id, double percentage)`).

### 3. Configuration

- Create a `@Configuration` class (`AppConfig`) that defines your beans using `@Bean` methods for at least one part of the app.
- Enable component scanning with `@ComponentScan` and annotate your repository and service classes with `@Repository` and `@Service` so the rest of the beans are auto-discovered.
- Provide two repository implementations — `InMemoryEmployeeRepository` and `FileBackedEmployeeRepository` (it can just read/write a simple `.txt` or `.csv` file) — both implementing `EmployeeRepository`. Use `@Profile` so that the `dev` profile wires the in-memory repository and the `prod` profile wires the file-backed one, and select the active profile from `Main`.

### 4. Notification System (collection injection)

> This is a core part of the assignment, not decoration — your service must actually use it.

- Create a `Notifier` interface with a method such as `send(String message)`, and provide at least three implementations: `EmailNotifier`, `SmsNotifier`, and `PushNotifier`.
- Create a `NotificationManager` bean that has Spring inject all `Notifier` beans at once as a `List<Notifier>` (collection injection) — do not wire them one by one.
- Use `@Order` (or the `Ordered` interface) on the `Notifier` implementations so they fire in a defined sequence, and have `NotificationManager` loop through the list and call `send()` on each.
- `EmployeeServiceImpl` must depend on `NotificationManager` and actually call it: fire a notification when a new employee is added, and a different notification when an employee receives a raise. The console output must clearly show all three notifiers firing for each event.

### 5. Validation Component

- Create an `EmployeeValidator` bean, injected into `EmployeeServiceImpl`, with a `validate(Employee)` method.
- It should throw a custom checked or unchecked exception (e.g. `InvalidEmployeeException`) if the name is blank or the salary is negative, and `EmployeeServiceImpl` must call it before saving or giving a raise.

### 6. Scopes, Lifecycle & the Scoped-Bean Problem

- Create a prototype-scoped `AuditLogger` bean that records a timestamped line every time it is used, and demonstrate, with printed output, that a new instance is created every time it is requested — while your singleton beans are not.
- Because `EmployeeServiceImpl` (a singleton) needs a fresh `AuditLogger` on every call, injecting it directly would only give you one instance for the lifetime of the app. Solve this properly using either an `ObjectProvider<AuditLogger>` / `ObjectFactory<AuditLogger>`, or a scoped-proxy (`proxyMode = ScopedProxyMode.TARGET_CLASS`), and explain your choice in the README.
- Add a `@PostConstruct` method that logs when a bean is initialized, and a `@PreDestroy` method that logs when it is destroyed. Trigger destruction by closing the context (`ConfigurableApplicationContext.close()`).

### 7. Externalized Configuration

- Create an `application.properties` file with at least four custom values (e.g. `company.name`, `company.currency`, `notification.retry-count`, `raise.max-percentage`).
- Load it with `@PropertySource` and inject the values into the relevant beans using `@Value` — for example, `EmployeeServiceImpl` should reject a raise request that exceeds `raise.max-percentage`.

### 8. Main Application

Write a `Main` class that sets the active profile, creates the `ApplicationContext`, and retrieves the `EmployeeService` bean. It must demonstrate every feature above through console output, in this rough order:

1. Adding a valid employee (triggering validation, then all three notifiers)
2. Attempting to add an invalid employee (validation failure handled gracefully)
3. Giving a raise within the allowed limit and one exceeding it
4. Requesting the prototype `AuditLogger` bean multiple times to prove new instances are created
5. Listing all employees
6. Printing the injected `@Value` properties
7. Finally closing the context to show the `@PreDestroy` callbacks firing

---

## Suggested Package Structure

```
com.training.empmanager
├── config/       AppConfig.java
├── model/        Employee.java
├── repository/   EmployeeRepository.java, InMemoryEmployeeRepository.java,
│                 FileBackedEmployeeRepository.java
├── service/      EmployeeService.java, EmployeeServiceImpl.java,
│                 EmployeeValidator.java, InvalidEmployeeException.java
├── notify/       Notifier.java, EmailNotifier.java, SmsNotifier.java,
│                 PushNotifier.java, NotificationManager.java
├── audit/        AuditLogger.java
└── Main.java
```

---

## Deliverables

- Complete source code (as a zipped Maven/Gradle project, or a link to a Git repository).
- The `application.properties` file used for externalized configuration.
- A short README (about one page) explaining:
  - which beans are singleton vs. prototype and why,
  - which injection type you used where and why,
  - how the scoped-bean problem was solved,
  - and how the `dev` vs `prod` profile changes the wired repository.
- A screenshot or copy-paste of the full console output showing all features running end to end.

---

## Ground Rules

- Do not use `new` to create your Repository, Service, or Notifier objects — if you find yourself doing that, the container is not managing that bean.
- You may use either XML configuration or Java configuration for the `@Bean`-defined part of the app, but Java-based configuration is recommended.
- Spring Boot is not required for this exercise — plain Spring (`spring-context` dependency) is enough and keeps the focus on the core concepts.
- Comment your code briefly wherever a Spring concept is being demonstrated, so it's easy to spot during grading.

---

*Submission deadline and submission method: to be announced by your instructor. Questions about the requirements should be asked before the deadline, not after.*
