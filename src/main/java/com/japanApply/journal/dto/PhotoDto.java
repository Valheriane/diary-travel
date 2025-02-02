package com.japanApply.journal.dto;

import java.time.LocalDate;

public record PhotoDto(
        String url,
        LocalDate uploadedAt,
        Long userId
) {
}
