package com.company.hrm.service.impl;

import com.company.hrm.dto.UserRequestDTO;
import com.company.hrm.entity.Employee;
import com.company.hrm.entity.User;
import com.company.hrm.exception.EmployeeNotFoundException;
import com.company.hrm.repository.EmployeeRepo;
import com.company.hrm.repository.UserRepo;
import com.company.hrm.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private EmployeeRepo employeeRepo;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, EmployeeRepo employeeRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserRequestDTO dto) {

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(
                        () -> new EmployeeNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); //Encrypt the password
        user.setRole(dto.getRole());
        user.setEmployee(employee);

        userRepo.save(user);
    }
}
