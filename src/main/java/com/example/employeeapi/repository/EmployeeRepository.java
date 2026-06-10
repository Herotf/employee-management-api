package com.example.employeeapi.repository;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.model.Employee.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    // Full-text search across name, email, department, job title
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.department) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Employee> searchEmployees(@Param("query") String query, Pageable pageable);

    // Filter by department with pagination
    Page<Employee> findByDepartment(String department, Pageable pageable);

    // Filter by status with pagination
    Page<Employee> findByStatus(EmploymentStatus status, Pageable pageable);

    // Filter by department and status
    Page<Employee> findByDepartmentAndStatus(String department, EmploymentStatus status, Pageable pageable);

    // Filter by salary range
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    Page<Employee> findBySalaryRange(
        @Param("minSalary") BigDecimal minSalary,
        @Param("maxSalary") BigDecimal maxSalary,
        Pageable pageable
    );

    // Department statistics
    @Query("SELECT e.department, COUNT(e), AVG(e.salary), MIN(e.salary), MAX(e.salary) " +
           "FROM Employee e GROUP BY e.department ORDER BY COUNT(e) DESC")
    List<Object[]> getDepartmentStatistics();

    // Count by status
    long countByStatus(EmploymentStatus status);

    // All distinct departments
    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department")
    List<String> findAllDepartments();

    // Recently hired
    @Query("SELECT e FROM Employee e ORDER BY e.hireDate DESC")
    List<Employee> findRecentlyHired(Pageable pageable);
}
