package com.japanApply.journal.dto;

import java.time.LocalDate;

public record VocabularyResponseDto(
        Long id,
        String expression,
        String translation,
        String usageExample,
        String languageExpression,
        String languageTranslation

) {
}