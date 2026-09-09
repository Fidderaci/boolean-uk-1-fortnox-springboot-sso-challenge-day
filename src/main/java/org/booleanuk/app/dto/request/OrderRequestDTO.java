package org.booleanuk.app.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequestDTO(
        BigDecimal totalAmount,
        Long customerId,
        List<Long> productIds
) {}