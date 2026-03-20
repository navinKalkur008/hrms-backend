package com.company.hrm.service.impl;

import com.company.hrm.dto.EmployeeRequestDTO;
import com.company.hrm.dto.EmployeeResponseDTO;
import com.company.hrm.entity.Department;
import com.company.hrm.entity.Employee;
import com.company.hrm.exception.EmployeeNotFoundException;
import com.company.hrm.exception.ResourceNotFoundException;
import com.company.hrm.mapper.EmployeeMapper;
import com.company.hrm.repository.DepartmentRepo;
import com.company.hrm.repository.EmployeeRepo;
import com.company.hrm.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepo employeeRepo;

    private DepartmentRepo departmentRepo;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        Department department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department Not Found with id: "+dto.getDepartmentId()));

        Employee employee = EmployeeMapper.toEntity(dto, department);

        Employee save = employeeRepo.save(employee);

        return EmployeeMapper.toDTO(save);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {

        Employee employee = employeeRepo.findById(id).
                orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        return EmployeeMapper.toDTO(employee);
    }

//    @Override
//    public List<Employee> getAllEmployees() {
//        return employeeRepo.findAll();
//    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee emp = employeeRepo.findById(id).
                orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: "+id));

        Department department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department Not found with id: "+dto.getDepartmentId()));

        emp.setFirstName(dto.getFirstName());
        emp.setLastName(dto.getLastName());
        emp.setEmail(dto.getEmail());
        emp.setPhone(dto.getPhone());
        emp.setGender(dto.getGender());
        emp.setDateOfBirth(dto.getDateOfBirth());
        emp.setEmployeeCode(dto.getEmployeeCode());
        emp.setDepartment(department);
        emp.setDesignation(dto.getDesignation());
        emp.setJoiningDate(dto.getJoiningDate());
        emp.setSalary(dto.getSalary());
        emp.setStatus(dto.getStatus());

        Employee save = employeeRepo.save(emp);
        return EmployeeMapper.toDTO(save);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));
        employeeRepo.delete(employee);
    }

    @Override
    public Page<EmployeeResponseDTO> getAllEmployees(int page, int size, String sortBy) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Employee> employees = employeeRepo.findAll(pageable);

        return employees.map(EmployeeMapper::toDTO);
    }

    @Override
    public List<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId) {
        List<Employee> employees = employeeRepo.findByDepartmentId(departmentId);

        return employees.stream()
                .map(EmployeeMapper::toDTO)
                .toList();
    }

    @Override
    public List<EmployeeResponseDTO> searchEmployee(String name, Long departmentId, Double minSalary) {
        List<Employee> employees = employeeRepo.searchEmployees(name, departmentId, minSalary);

        return employees.stream()
                .map(EmployeeMapper::toDTO)
                .toList();
    }
}
