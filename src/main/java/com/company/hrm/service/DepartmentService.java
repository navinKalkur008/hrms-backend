package com.company.hrm.service;

import com.company.hrm.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department createDepartment (Department department);

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    void deleteDepartment(Long id);
}
