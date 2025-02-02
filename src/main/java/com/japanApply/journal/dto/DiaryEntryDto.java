package com.japanApply.journal.dto;

import java.util.*;
import java.time.LocalDate;


public record DiaryEntryDto (
        String titleDiaryEntry,
        String noteDiaryEntry,
        LocalDate dateCreatedDiaryEntry,
        String descriptionDiaryEntry,
        Long userId,
        Long diaryId,
        List<Long> vocabularyIds,
        List<Long> photoIds,
        Long locationId
) { }


