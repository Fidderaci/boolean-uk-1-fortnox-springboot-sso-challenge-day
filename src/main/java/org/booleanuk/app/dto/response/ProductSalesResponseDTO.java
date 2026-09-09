package org.booleanuk.app.dto.response;

import java.math.BigDecimal;

public record ProductSalesResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        long quantitySold
) {}