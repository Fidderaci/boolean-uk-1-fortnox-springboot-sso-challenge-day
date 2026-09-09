package org.booleanuk.app.dto.response;

import java.util.List;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        List<Long> orderIds
) {}
