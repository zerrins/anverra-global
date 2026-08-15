package com.anverraglobal.reporting.application.dto;

import java.math.BigDecimal;

public record CommissionStatisticsResponse(
        BigDecimal totalCommissionAmount,
        BigDecimal agentACommissionAmount,
        BigDecimal agentBCommissionAmount,
        Long configuredCommissionCount
) {
}
