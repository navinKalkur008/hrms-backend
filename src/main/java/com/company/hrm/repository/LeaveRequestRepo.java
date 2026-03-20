package com.company.hrm.repository;

import com.company.hrm.entity.LeaveRequest;
import com.company.hrm.utility.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByEmployeeId(Long employeeId, Pageable pageable);

    @Override
    Page<LeaveRequest> findAll(Pageable pageable);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);

    Page<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status, Pageable pageable);
}
