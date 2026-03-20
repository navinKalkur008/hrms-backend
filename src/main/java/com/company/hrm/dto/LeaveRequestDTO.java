package com.company.hrm.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LeaveRequestDTO {

    @NotNull
    private String leaveType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String reason;

    public @NotNull String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(@NotNull String leaveType) {
        this.leaveType = leaveType;
    }

    public @NotNull LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotNull LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
