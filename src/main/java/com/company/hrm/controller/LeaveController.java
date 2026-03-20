package com.company.hrm.controller;

import com.company.hrm.dto.LeaveRequestDTO;
import com.company.hrm.dto.LeaveResponseDTO;
import com.company.hrm.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    //Apply Leave
    @PostMapping
    public ResponseEntity<LeaveResponseDTO> applyLeave(
            @Valid @RequestBody LeaveRequestDTO dto){

        return new ResponseEntity<>(leaveService.applyLeave(dto), HttpStatus.CREATED);
    }

    //Get All Leave Requests
    @GetMapping
    public ResponseEntity<Page<LeaveResponseDTO>> getAllLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction){

        return  ResponseEntity.ok(leaveService.getAllLeaves(page, size, status, sortBy, direction));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<LeaveResponseDTO>> getLeavesByEmployeeId(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
            ){

        return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId, page, size));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveResponseDTO> approveLeave(@PathVariable Long id){
        return ResponseEntity.ok(leaveService.approveLeave(id));
    }

    @PutMapping("/{id}/reject")
    public  ResponseEntity<LeaveResponseDTO> rejectLeave(@PathVariable Long id){
        return ResponseEntity.ok(leaveService.rejectLeave(id));
    }
}
