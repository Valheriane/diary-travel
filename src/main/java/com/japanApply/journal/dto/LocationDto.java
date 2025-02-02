package com.japanApply.journal.dto;

import java.time.LocalDate;

public record LocationDto(
        String locationName,
        Long countryId,
        String region,
        Float latitude,
        Float longitude,
        String locationType

) {
}