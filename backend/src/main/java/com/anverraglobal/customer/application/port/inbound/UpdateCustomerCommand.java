package com.anverraglobal.customer.application.port.inbound;

import com.anverraglobal.customer.domain.CustomerType;
import java.util.UUID;

public record UpdateCustomerCommand(
        CustomerType customerType,
        String name,
        String contactInfo,
        String addressInfo,
        String individualInfo,
        String businessInfo
) {}
