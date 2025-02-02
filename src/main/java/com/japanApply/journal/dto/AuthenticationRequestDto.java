package com.japanApply.journal.dto;

/**
 * Authentication request DTO record
 */
public record AuthenticationRequestDto(
        String email,
        String password
) { }
