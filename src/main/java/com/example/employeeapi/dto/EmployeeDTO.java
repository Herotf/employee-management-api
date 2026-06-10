package com.example.employeeapi.dto;

import com.example.employeeapi.model.Employee.EmploymentStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50)
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50)
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email")
        private String email;

        @NotBlank(message = "Department is required")
        private String department;

        @NotBlank(message = "Job title is required")
        private String jobTitle;

        @NotNull(message = "Salary is required")
        @DecimalMin(value = "0.0", inclusive = false)
        private BigDecimal salary;

        @NotNull(message = "Hire date is required")
        private LocalDate hireDate;

        private EmploymentStatus status;
        private String phone;
        private String address;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String firstName;
        private String lastName;
        private String fullName;
        private String email;
        private String department;
        private String jobTitle;
        private BigDecimal salary;
        private LocalDate hireDate;
        private EmploymentStatus status;
        private String phone;
        private String address;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String fullName;
        private String email;
        private String department;
        private String jobTitle;
        private EmploymentStatus status;
    }
}
