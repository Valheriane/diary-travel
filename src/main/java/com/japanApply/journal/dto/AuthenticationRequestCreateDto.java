package com.japanApply.journal.dto;

import com.japanApply.journal.model.RangType;

import java.util.List;

/**
 * Authentication request DTO record
 */
public record AuthenticationRequestCreateDto(
        String username,
        String email,
        String password,
        String countryOfOrigin,
        String phone
) { }

