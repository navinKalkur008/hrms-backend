package com.company.hrm.service.impl;

import com.company.hrm.dto.LeaveRequestDTO;
import com.company.hrm.dto.LeaveResponseDTO;
import com.company.hrm.entity.Employee;
import com.company.hrm.entity.LeaveRequest;
import com.company.hrm.entity.User;
import com.company.hrm.exception.EmployeeNotFoundException;
import com.company.hrm.exception.InvalidLeaveStateException;
import com.company.hrm.exception.LeaveNotFoundException;
import com.company.hrm.exception.ResourceNotFoundException;
import com.company.hrm.repository.EmployeeRepo;
import com.company.hrm.repository.LeaveRequestRepo;
import com.company.hrm.repository.UserRepo;
import com.company.hrm.service.LeaveService;
import com.company.hrm.utility.LeaveStatus;
import com.company.hrm.utility.SecurityUtil;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    private LeaveRequestRepo leaveRequestRepo;
    private EmployeeRepo employeeRepo;
    private UserRepo userRepo;

    public LeaveServiceImpl(LeaveRequestRepo leaveRequestRepo, EmployeeRepo employeeRepo, UserRepo userRepo) {
        this.leaveRequestRepo = leaveRequestRepo;
        this.employeeRepo = employeeRepo;
        this.userRepo = userRepo;
    }

    @Override
    public LeaveResponseDTO applyLeave(LeaveRequestDTO dto) {

        String email = SecurityUtil.getLoggedInUserEmail();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User Not found"));

        Employee employee = user.getEmployee();

        LeaveRequest leave = new LeaveRequest();

        leave.setEmployee(employee);
        leave.setLeaveType(dto.getLeaveType());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setAppliedDate(LocalDate.now());
        leave.setStatus(LeaveStatus.PENDING);

        LeaveRequest save = leaveRequestRepo.save(leave);

        LeaveResponseDTO response = mapToResponseDTO(save);

        return response;
    }

    @Override
    public Page<LeaveResponseDTO> getAllLeaves(int page, int size, String status, String sortBy, String direction) {

        // SORTING
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        // GET LOGGED IN USER

        String email = SecurityUtil.getLoggedInUserEmail();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Page<LeaveRequest> leaves;

        //Convert status if PRESENT
        LeaveStatus leaveStatus = null;
        if (status != null) {
            try {
                leaveStatus = LeaveStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidLeaveStateException("Invalid Leave Status");
            }
        }

        //MAIN LOGIC ROLE BASED
        if (user.getRole().name().equals("ADMIN")) {
            // ADMIN --> SEE ALL LEAVES
            if (leaveStatus != null) {
                leaves = leaveRequestRepo.findByStatus(leaveStatus, pageable);
            } else {
                leaves = leaveRequestRepo.findAll(pageable);
            }
        } else {

            //EMPLOYEE --> ONLY THEIR LEAVES
            Long employeeId = user.getEmployee().getId();

            if (leaveStatus != null) {
                leaves = leaveRequestRepo.findByEmployeeIdAndStatus(employeeId, leaveStatus, pageable);
            } else {
                leaves = leaveRequestRepo.findByEmployeeId(employeeId, pageable);
            }

        }

        return leaves.map(this::mapToResponseDTO);
    }

    @Override
    public Page<LeaveResponseDTO> getLeavesByEmployee(Long employeeId, int page, int size) {

        PageRequest pageable =  PageRequest.of(page, size);

        Page<LeaveRequest> leaves = leaveRequestRepo.findByEmployeeId(employeeId, pageable);

        return leaves.map(this::mapToResponseDTO);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public LeaveResponseDTO approveLeave(Long id) {
        LeaveRequest leave = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found for id: " + id));

        if (leave.getStatus() == LeaveStatus.APPROVED){
            throw new InvalidLeaveStateException("Only pending leaves can be approved");
        }

        leave.setStatus(LeaveStatus.APPROVED);
        LeaveRequest updatedLeave = leaveRequestRepo.save(leave);
        return mapToResponseDTO(updatedLeave);

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public LeaveResponseDTO rejectLeave(Long id) {
        LeaveRequest leave = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found with id: " + id));

        if (leave.getStatus() != LeaveStatus.PENDING){
            throw  new InvalidLeaveStateException("Only pending leaves can be rejected.");
        }

        leave.setStatus(LeaveStatus.REJECTED);
        LeaveRequest updatedLeave = leaveRequestRepo.save(leave);
        return mapToResponseDTO(updatedLeave);
    }

    private LeaveResponseDTO mapToResponseDTO (LeaveRequest leave){
        LeaveResponseDTO responseDTO = new LeaveResponseDTO();

        responseDTO.setId(leave.getId());
        responseDTO.setEmployeeId(leave.getEmployee().getId());
        responseDTO.setEmployeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName());
        responseDTO.setLeaveType(leave.getLeaveType());
        responseDTO.setStartDate(leave.getStartDate());
        responseDTO.setEndDate(leave.getEndDate());
        responseDTO.setReason(leave.getReason());
        responseDTO.setAppliedDate(leave.getAppliedDate());
        responseDTO.setStatus(leave.getStatus().name());

        return responseDTO;
    }
}
