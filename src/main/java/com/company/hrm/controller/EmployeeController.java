package com.company.hrm.controller;

import com.company.hrm.dto.EmployeeRequestDTO;
import com.company.hrm.dto.EmployeeResponseDTO;
import com.company.hrm.entity.Employee;
import com.company.hrm.mapper.EmployeeMapper;
import com.company.hrm.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto){

        EmployeeResponseDTO employeeResponseDTO = employeeService.createEmployee(dto);

        return new ResponseEntity<>(employeeResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy){

        return ResponseEntity.ok(employeeService.getAllEmployees(page, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id){

        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByDepartment(
            @PathVariable Long departmentId){

        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }

    //Dynamic Search API
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDTO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Double minSalary){

        return ResponseEntity.ok(employeeService.searchEmployee(name, departmentId, minSalary));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDTO dto){
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee Deleted Successfully");
    }
}
