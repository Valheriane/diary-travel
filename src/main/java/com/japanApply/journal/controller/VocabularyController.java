package com.japanApply.journal.controller;

import com.japanApply.journal.configuration.InUseExeption.LocationInUseException;
import com.japanApply.journal.configuration.InUseExeption.VocabularyInUseException;
import com.japanApply.journal.dto.VocabularyResponseDto;
import com.japanApply.journal.dto.VocabularyDto;
import com.japanApply.journal.dto.mapper.VocabularyMapper;
import com.japanApply.journal.model.Vocabulary;
import com.japanApply.journal.model.Language;
import com.japanApply.journal.service.VocabularyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for managing vocabulary-related operations.
 * This class provides endpoints for CRUD operations and filtering vocabularies by language.
 */
@RestController
@RequestMapping("/api/vocabularies")
@Tag(name = "Vocabulary Management", description = "APIs for managing vocabularies in the system.")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    /**
     * Constructor for VocabularyController.
     *
     * @param vocabularyService Service to handle vocabulary-related operations.
     */
    @Autowired
    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    /**
     * Retrieve all vocabularies.
     *
     * @return List of all vocabularies.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @Operation(summary = "Get all vocabularies", description = "Retrieve a list of all vocabularies available in the system.")
    public List<VocabularyResponseDto> getAllVocabularies() {
        return vocabularyService.getAllVocabularies()
                .stream()
                .map(VocabularyMapper::toVocabularyResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a specific vocabulary by its ID.
     *
     * @param id The ID of the vocabulary to retrieve.
     * @return The requested vocabulary, or a 404 response if not found.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @Operation(
            summary = "Get vocabulary by ID",
            description = "Retrieve details of a specific vocabulary using its unique ID."
    )
    public ResponseEntity<VocabularyResponseDto> getVocabularyById(
            @PathVariable @Parameter(description = "The unique ID of the vocabulary to retrieve.", example = "1") Long id
    ) {
        Optional<Vocabulary> vocabulary = vocabularyService.getVocabularyById(id);
        return vocabulary.map(v -> ResponseEntity.ok(VocabularyMapper.toVocabularyResponseDto(v)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new vocabulary.
     *
     * @param vocabularyDto The vocabulary object to be created.
     * @return The created vocabulary.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @Operation(
            summary = "Create a new vocabulary",
            description = "Add a new vocabulary entry to the system."
    )
    public ResponseEntity<VocabularyResponseDto> createVocabulary(
            @RequestBody @Parameter(description = "The vocabulary object containing details of the new entry.") VocabularyDto vocabularyDto
    ) {
        Vocabulary vocabulary = VocabularyMapper.toVocabulary(vocabularyDto);
        Vocabulary savedVocabulary = vocabularyService.createVocabulary(vocabulary);
        return ResponseEntity.status(HttpStatus.CREATED).body(VocabularyMapper.toVocabularyResponseDto(savedVocabulary));
    }

    /**
     * Update an existing vocabulary by its ID.
     *
     * @param id         The ID of the vocabulary to update.
     * @param vocabularyDto The updated vocabulary object.
     * @return The updated vocabulary, or a 404 response if not found.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @Operation(
            summary = "Update a vocabulary by ID",
            description = "Modify the details of an existing vocabulary using its unique ID."
    )
    public ResponseEntity<VocabularyResponseDto> updateVocabulary(
            @PathVariable @Parameter(description = "The unique ID of the vocabulary to update.", example = "1") Long id,
            @RequestBody @Parameter(description = "The vocabulary object containing the updated details.") VocabularyDto vocabularyDto
    ) {
        Optional<Vocabulary> existingVocabulary = vocabularyService.getVocabularyById(id);
        if (existingVocabulary.isPresent()) {
            Vocabulary updatedVocabulary = VocabularyMapper.toVocabulary(vocabularyDto);
            updatedVocabulary.setId(id); // Ensure the ID does not change
            Vocabulary savedVocabulary = vocabularyService.updateVocabulary(id, updatedVocabulary).orElseThrow();
            return ResponseEntity.ok(VocabularyMapper.toVocabularyResponseDto(savedVocabulary));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete a vocabulary by its ID.
     *
     * @param id The ID of the vocabulary to delete.
     * @return A 204 response if the deletion is successful, or a 404 response if the vocabulary is not found.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(
            summary = "Delete a vocabulary by ID",
            description = "Remove a vocabulary entry from the system using its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Location successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "400", description = "Location is in use and cannot be deleted")

    })
    public ResponseEntity<Void> deleteVocabulary(
            @PathVariable @Parameter(description = "The unique ID of the vocabulary to delete.", example = "1") Long id
    ) {
        try {
            vocabularyService.deleteVocabulary(id);
            return ResponseEntity.noContent().build();
        } catch (VocabularyInUseException ex) {
            // Return a 400 error with a custom message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Retrieve all vocabularies associated with a specific language.
     *
     * @param language The language filter for vocabularies.
     * @return List of vocabularies associated with the specified language.
     */
    @GetMapping("/language/{language}")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @Operation(
            summary = "Get vocabularies by language",
            description = "Retrieve all vocabularies filtered by the specified language."
    )
    public List<VocabularyResponseDto> getVocabulariesByLanguage(
            @PathVariable @Parameter(description = "The language used to filter vocabularies.") Language language
    ) {
        return vocabularyService.getVocabulariesByLanguage(language)
                .stream()
                .map(VocabularyMapper::toVocabularyResponseDto)
                .collect(Collectors.toList());
    }
}
