package com.example.employeeapi.service;

import com.example.employeeapi.dto.EmployeeDTO;
import com.example.employeeapi.exception.DuplicateResourceException;
import com.example.employeeapi.exception.ResourceNotFoundException;
import com.example.employeeapi.model.Employee;
import com.example.employeeapi.model.Employee.EmploymentStatus;
import com.example.employeeapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Page<EmployeeDTO.Response> getAllEmployees(
            String search, String department, EmploymentStatus status,
            int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employees;

        if (search != null && !search.isBlank()) {
            employees = employeeRepository.searchEmployees(search.trim(), pageable);
        } else if (department != null && status != null) {
            employees = employeeRepository.findByDepartmentAndStatus(department, status, pageable);
        } else if (department != null) {
            employees = employeeRepository.findByDepartment(department, pageable);
        } else if (status != null) {
            employees = employeeRepository.findByStatus(status, pageable);
        } else {
            employees = employeeRepository.findAll(pageable);
        }

        return employees.map(this::toResponse);
    }

    public EmployeeDTO.Response getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public EmployeeDTO.Response createEmployee(EmployeeDTO.Request request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee with email already exists: " + request.getEmail());
        }

        Employee employee = toEntity(request);
        Employee saved = employeeRepository.save(employee);
        log.info("Created employee: {} {}", saved.getFirstName(), saved.getLastName());
        return toResponse(saved);
    }

    @Transactional
    public EmployeeDTO.Response updateEmployee(Long id, EmployeeDTO.Request request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (!employee.getEmail().equals(request.getEmail())
                && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setJobTitle(request.getJobTitle());
        employee.setSalary(request.getSalary());
        employee.setHireDate(request.getHireDate());
        employee.setPhone(request.getPhone());
        employee.setAddress(request.getAddress());
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }

        Employee updated = employeeRepository.save(employee);
        log.info("Updated employee id: {}", id);
        return toResponse(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Deleted employee id: {}", id);
    }

    public Map<String, Object> getDashboardStats() {
        long total = employeeRepository.count();
        long active = employeeRepository.countByStatus(EmploymentStatus.ACTIVE);
        long onLeave = employeeRepository.countByStatus(EmploymentStatus.ON_LEAVE);
        long inactive = employeeRepository.countByStatus(EmploymentStatus.INACTIVE);

        List<Object[]> deptStats = employeeRepository.getDepartmentStatistics();
        List<Map<String, Object>> departments = new ArrayList<>();
        for (Object[] row : deptStats) {
            Map<String, Object> dept = new LinkedHashMap<>();
            dept.put("name", row[0]);
            dept.put("count", row[1]);
            dept.put("avgSalary", row[2] != null ? ((Double) row[2]).longValue() : 0);
            dept.put("minSalary", row[3]);
            dept.put("maxSalary", row[4]);
            departments.add(dept);
        }

        List<Employee> recentHires = employeeRepository.findRecentlyHired(PageRequest.of(0, 5));

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalEmployees", total);
        stats.put("activeEmployees", active);
        stats.put("onLeave", onLeave);
        stats.put("inactiveEmployees", inactive);
        stats.put("departmentBreakdown", departments);
        stats.put("recentHires", recentHires.stream().map(this::toSummary).toList());

        return stats;
    }

    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }

    // Mapping methods
    private EmployeeDTO.Response toResponse(Employee e) {
        return EmployeeDTO.Response.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .fullName(e.getFirstName() + " " + e.getLastName())
                .email(e.getEmail())
                .department(e.getDepartment())
                .jobTitle(e.getJobTitle())
                .salary(e.getSalary())
                .hireDate(e.getHireDate())
                .status(e.getStatus())
                .phone(e.getPhone())
                .address(e.getAddress())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private EmployeeDTO.Summary toSummary(Employee e) {
        return EmployeeDTO.Summary.builder()
                .id(e.getId())
                .fullName(e.getFirstName() + " " + e.getLastName())
                .email(e.getEmail())
                .department(e.getDepartment())
                .jobTitle(e.getJobTitle())
                .status(e.getStatus())
                .build();
    }

    private Employee toEntity(EmployeeDTO.Request r) {
        return Employee.builder()
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .email(r.getEmail())
                .department(r.getDepartment())
                .jobTitle(r.getJobTitle())
                .salary(r.getSalary())
                .hireDate(r.getHireDate())
                .status(r.getStatus() != null ? r.getStatus() : EmploymentStatus.ACTIVE)
                .phone(r.getPhone())
                .address(r.getAddress())
                .build();
    }
}
