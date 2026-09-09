package org.booleanuk.app.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        BigDecimal totalAmount,
        Long customerId,
        List<Long> productIds
) {}