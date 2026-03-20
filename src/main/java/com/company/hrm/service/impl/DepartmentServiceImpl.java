package com.company.hrm.service.impl;

import com.company.hrm.entity.Department;
import com.company.hrm.exception.ResourceNotFoundException;
import com.company.hrm.repository.DepartmentRepo;
import com.company.hrm.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepo departmentRepo;

    public DepartmentServiceImpl(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Override
    public Department createDepartment(Department department) {
        return departmentRepo.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department Not found with id: "+id));
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Department Not found with id: " + id));
        departmentRepo.delete(department);
    }
}
