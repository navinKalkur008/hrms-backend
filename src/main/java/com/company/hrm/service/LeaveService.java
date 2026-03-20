package com.company.hrm.service;

import com.company.hrm.dto.LeaveRequestDTO;
import com.company.hrm.dto.LeaveResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LeaveService {

    public LeaveResponseDTO applyLeave(LeaveRequestDTO leaveRequestDTO);

    public Page<LeaveResponseDTO> getAllLeaves(int page, int size, String status, String sortBy, String direction);

    public Page<LeaveResponseDTO> getLeavesByEmployee(Long employeeId, int page, int size);

    LeaveResponseDTO approveLeave(Long id);

    LeaveResponseDTO rejectLeave(Long id);
}
