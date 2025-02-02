package com.japanApply.journal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Api Error response DTO record
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        @Schema(name = "status", example = "400 or 500")
        int status,
        String error,
        String path
) {
}
