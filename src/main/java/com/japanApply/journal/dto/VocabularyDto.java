package com.japanApply.journal.dto;

import java.time.LocalDate;

public record VocabularyDto(
        Long id,
        String expression,
        String translation,
        String usageExample,
        String languageExpression,
        String languageTranslation

) {
}