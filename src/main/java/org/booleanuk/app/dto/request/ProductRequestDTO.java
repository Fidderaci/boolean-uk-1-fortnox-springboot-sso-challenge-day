package org.booleanuk.app.dto.request;

import java.math.BigDecimal;

public record ProductRequestDTO(
        String name,
        BigDecimal price
) {}