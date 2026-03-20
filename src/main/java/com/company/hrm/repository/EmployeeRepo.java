package com.company.hrm.repository;

import com.company.hrm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentId(Long departmentId);

    //QUERY METHOD
    @Query("""
            SELECT e FROM Employee e
            WHERE (:name IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:departmentId IS NULL OR e.department.id = :departmentId)
            AND (:minSalary IS NULL OR e.salary >= :minSalary)
            """)
    List<Employee> searchEmployees(String name, Long departmentId, Double minSalary);
}
