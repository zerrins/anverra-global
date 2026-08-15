package com.anverraglobal.reporting.application.dto;

public record PolicyStatisticsResponse(
        Long totalPolicies,
        Long draftCount,
        Long activeCount,
        Long inactiveCount
) {
}
