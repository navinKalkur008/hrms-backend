package com.company.hrm.service;

import com.company.hrm.dto.EmployeeRequestDTO;
import com.company.hrm.dto.EmployeeResponseDTO;
import com.company.hrm.entity.Department;
import com.company.hrm.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);

    public EmployeeResponseDTO getEmployeeById(Long id);

//    List<Employee> getAllEmployees();

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);

    public void deleteEmployee(Long id);

    public Page<EmployeeResponseDTO> getAllEmployees(int page, int size, String sortBy);

    List<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId);

    //QUERY API or DYNAMIC API SEARCH
    List<EmployeeResponseDTO> searchEmployee(String name, Long departmentId, Double minSalary);
}
