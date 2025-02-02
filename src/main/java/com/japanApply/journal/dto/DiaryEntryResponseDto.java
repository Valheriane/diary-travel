package com.japanApply.journal.dto;

import java.time.LocalDate;
import java.util.Set;

public record DiaryEntryResponseDto(
        Long id,
        String title,
        String note,
        LocalDate dateCreated,
        String description,
        Long userID,
        String username,
        Long diaryId,
        String diaryName,
        Long locationId,
        String locationName,
        Set<String> vocabularyExpressions,
        Set<String> url

) { }
