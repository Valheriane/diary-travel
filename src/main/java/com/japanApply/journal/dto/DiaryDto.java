package com.japanApply.journal.dto;

import java.time.LocalDate;

public record DiaryDto(
        String nameDiary,
        Long userId,
        Long photoCoverId,
        Long countryId,
        LocalDate dateCreateDiary,
        LocalDate dateStartDiary,
        LocalDate dateEndDiary,
        String descriptionDiary,
        Boolean publicDiary
) {}
