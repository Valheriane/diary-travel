package com.japanApply.journal.controller;

import com.japanApply.journal.dto.DiaryEntryResponseDto;
import com.japanApply.journal.dto.DiaryResponseDto;
import com.japanApply.journal.dto.mapper.DiaryEntryMapper;
import com.japanApply.journal.model.Diary;
import com.japanApply.journal.model.User;
import com.japanApply.journal.dto.DiaryDto;
import com.japanApply.journal.dto.mapper.DiaryMapper;
import com.japanApply.journal.repository.CountryRepository;
import com.japanApply.journal.repository.PhotoRepository;
import com.japanApply.journal.repository.UserRepository;
import com.japanApply.journal.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This class is a REST controller that handles operations related to the 'Diary' entity.
 * It provides endpoints for creating, reading, updating, and deleting diary entries.
 * Additionally, it includes routes to fetch diaries based on user ID or their public status.
 */
@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final CountryRepository countryRepository;
    private final SecurityService securityService;

    public DiaryController(DiaryService diaryService, UserService userService,
                           UserRepository userRepository, PhotoRepository photoRepository, CountryRepository countryRepository, SecurityService securityService) {
        this.diaryService = diaryService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.countryRepository = countryRepository;
        this.securityService = securityService;
    }

    @Operation(summary = "Retrieve a diary by its ID", description = "Fetch a specific diary entry using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diary found",
                    content = @Content(schema = @Schema(implementation = Diary.class))),
            @ApiResponse(responseCode = "404", description = "Diary not found")
    })
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DiaryResponseDto> getDiaryById(@PathVariable Long id) {
        Optional<Diary> diary = diaryService.getDiaryById(id);

        // If the diary exists
        if (diary.isPresent()) {
            // Checks if the current user can access it
            boolean canAccess = securityService.canAccessResource(diary.get());

            if (canAccess) {
                return ResponseEntity.ok(DiaryMapper.toDiaryResponseDto(diary.get()));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Forbidden access
            }
        }

        // If the diary does not exist
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrieve all diaries", description = "Fetch a list of all diary entries.")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @ApiResponse(responseCode = "200", description = "List of diaries retrieved",
            content = @Content(schema = @Schema(implementation = Diary.class)))
    @GetMapping
    public ResponseEntity<List<Diary>> getAllDiaries() {
        List<Diary> diaries = diaryService.getAllDiaries();
        List<DiaryResponseDto> responseDtos = diaries.stream()
                .map(DiaryMapper::toDiaryResponseDto)
                .toList();
        return ResponseEntity.ok(diaries);
    }

    @Operation(summary = "Create a new diary entry", description = "Add a new diary entry to the database.")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Diary created successfully",
                    content = @Content(schema = @Schema(implementation = Diary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<DiaryResponseDto> createDiary(@RequestBody DiaryDto diaryDto) {
        Diary diary = DiaryMapper.toDiary(diaryDto, userRepository, photoRepository, countryRepository);
        Diary createdDiary = diaryService.createDiary(diary);
        return ResponseEntity.status(HttpStatus.CREATED).body(DiaryMapper.toDiaryResponseDto(createdDiary));
    }

    @Operation(summary = "Update an existing diary entry", description = "Modify an existing diary entry by its ID.")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diary updated successfully",
                    content = @Content(schema = @Schema(implementation = Diary.class))),
            @ApiResponse(responseCode = "404", description = "Diary not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DiaryResponseDto> updateDiary(@PathVariable Long id, @RequestBody DiaryDto diaryDto) {
        Optional<Diary> existingDiary = diaryService.getDiaryById(id);

        // Checks if the diary exists
        if (existingDiary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Checks if the current user can modify this diary
        if (!securityService.canAccessResource(existingDiary.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // User cannot modify this diary
        }

        // Converts the DTO into Diary entity
        Diary diary = DiaryMapper.toDiary(diaryDto, userRepository, photoRepository, countryRepository);
        diary.setId(id); // Assigns the existing ID to avoid creation

        // Updates the diary
        Diary updatedDiary = diaryService.updateDiary(diary);

        // Returns the updated diary as a DTO
        return ResponseEntity.ok(DiaryMapper.toDiaryResponseDto(updatedDiary));
    }

    @Operation(summary = "Delete a diary entry", description = "Remove a diary entry from the database by its ID.")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Diary deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Diary not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiaryById(@PathVariable Long id) {
        Optional<Diary> diary = diaryService.getDiaryById(id);

        // Checks if the diary exists
        if (diary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Checks if the current user can delete this diary
        if (!securityService.canAccessResource(diary.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // User cannot delete this diary
        }

        // If the user can access the resource, deletes it
        diaryService.deleteDiaryById(id);

        return ResponseEntity.noContent().build(); // Successful deletion
    }

    @Operation(summary = "Retrieve diaries by user ID", description = "Fetch all diaries belonging to a specific user.")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diaries retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Diary.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DiaryResponseDto>> getEntryByUserId(@PathVariable Long userId) {
        Optional<User> user = Optional.ofNullable(userService.getUserById(userId));
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Checks if the current user can access this information
        if (!securityService.canAccessResource(user.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<DiaryResponseDto> diary = diaryService.findDiariesByUser(user.get()).stream()
                .map(DiaryMapper::toDiaryResponseDto)
                .toList();
        return ResponseEntity.ok(diary);
    }

    @Operation(summary = "Retrieve all public diaries", description = "Fetch all diaries that are marked as public.")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @ApiResponse(responseCode = "200", description = "Public diaries retrieved successfully",
            content = @Content(schema = @Schema(implementation = Diary.class)))
    @GetMapping("/public")
    public ResponseEntity<List<Diary>> getPublicDiaries() {
        List<Diary> publicDiaries = diaryService.findPublicDiaries();
        return ResponseEntity.ok(publicDiaries);
    }
}
