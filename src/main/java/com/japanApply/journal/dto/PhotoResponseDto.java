package com.japanApply.journal.dto;

import java.time.LocalDate;

public record PhotoResponseDto(
        Long id,
        String url,
        LocalDate uploadedAt,
        Long userID
) {
}
