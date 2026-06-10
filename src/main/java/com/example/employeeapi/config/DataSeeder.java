package com.example.employeeapi.config;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.model.Employee.EmploymentStatus;
import com.example.employeeapi.model.User;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedEmployees();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@employeeapi.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of("ROLE_ADMIN", "ROLE_USER"))
                    .build();

            User user = User.builder()
                    .username("user")
                    .email("user@employeeapi.com")
                    .password(passwordEncoder.encode("user123"))
                    .roles(Set.of("ROLE_USER"))
                    .build();

            userRepository.saveAll(List.of(admin, user));
            log.info("Seeded default users: admin / user");
        }
    }

    private void seedEmployees() {
        if (employeeRepository.count() == 0) {
            List<Employee> employees = List.of(
                Employee.builder().firstName("Sarah").lastName("Johnson").email("sarah.johnson@company.com")
                    .department("Engineering").jobTitle("Senior Software Engineer")
                    .salary(new BigDecimal("95000")).hireDate(LocalDate.of(2020, 3, 15))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0101").build(),

                Employee.builder().firstName("Marcus").lastName("Chen").email("marcus.chen@company.com")
                    .department("Engineering").jobTitle("Backend Developer")
                    .salary(new BigDecimal("82000")).hireDate(LocalDate.of(2021, 7, 1))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0102").build(),

                Employee.builder().firstName("Priya").lastName("Patel").email("priya.patel@company.com")
                    .department("Product").jobTitle("Product Manager")
                    .salary(new BigDecimal("105000")).hireDate(LocalDate.of(2019, 11, 20))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0103").build(),

                Employee.builder().firstName("James").lastName("Wilson").email("james.wilson@company.com")
                    .department("Sales").jobTitle("Sales Director")
                    .salary(new BigDecimal("120000")).hireDate(LocalDate.of(2018, 5, 10))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0104").build(),

                Employee.builder().firstName("Emily").lastName("Rodriguez").email("emily.rodriguez@company.com")
                    .department("HR").jobTitle("HR Manager")
                    .salary(new BigDecimal("78000")).hireDate(LocalDate.of(2020, 9, 8))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0105").build(),

                Employee.builder().firstName("David").lastName("Kim").email("david.kim@company.com")
                    .department("Engineering").jobTitle("DevOps Engineer")
                    .salary(new BigDecimal("90000")).hireDate(LocalDate.of(2022, 1, 17))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0106").build(),

                Employee.builder().firstName("Aisha").lastName("Thompson").email("aisha.thompson@company.com")
                    .department("Marketing").jobTitle("Marketing Lead")
                    .salary(new BigDecimal("72000")).hireDate(LocalDate.of(2021, 4, 5))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0107").build(),

                Employee.builder().firstName("Carlos").lastName("Mendoza").email("carlos.mendoza@company.com")
                    .department("Finance").jobTitle("Financial Analyst")
                    .salary(new BigDecimal("85000")).hireDate(LocalDate.of(2019, 8, 22))
                    .status(EmploymentStatus.ON_LEAVE).phone("+1-555-0108").build(),

                Employee.builder().firstName("Nina").lastName("Okafor").email("nina.okafor@company.com")
                    .department("Product").jobTitle("UX Designer")
                    .salary(new BigDecimal("88000")).hireDate(LocalDate.of(2022, 6, 13))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0109").build(),

                Employee.builder().firstName("Robert").lastName("Anderson").email("robert.anderson@company.com")
                    .department("Sales").jobTitle("Account Executive")
                    .salary(new BigDecimal("68000")).hireDate(LocalDate.of(2023, 2, 28))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0110").build(),

                Employee.builder().firstName("Lisa").lastName("Park").email("lisa.park@company.com")
                    .department("Engineering").jobTitle("Frontend Developer")
                    .salary(new BigDecimal("79000")).hireDate(LocalDate.of(2022, 10, 3))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0111").build(),

                Employee.builder().firstName("Michael").lastName("Brown").email("michael.brown@company.com")
                    .department("Finance").jobTitle("CFO")
                    .salary(new BigDecimal("180000")).hireDate(LocalDate.of(2017, 1, 10))
                    .status(EmploymentStatus.ACTIVE).phone("+1-555-0112").build()
            );

            employeeRepository.saveAll(employees);
            log.info("Seeded {} employees", employees.size());
        }
    }
}
