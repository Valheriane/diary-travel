package com.japanApply.journal.controller;

import com.japanApply.journal.dto.DiaryDto;
import com.japanApply.journal.dto.DiaryEntryDto;
import com.japanApply.journal.dto.DiaryEntryResponseDto;
import com.japanApply.journal.dto.VocabularyResponseDto;
import com.japanApply.journal.dto.mapper.DiaryEntryMapper;
import com.japanApply.journal.dto.mapper.DiaryMapper;
import com.japanApply.journal.dto.mapper.VocabularyMapper;
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
import com.japanApply.journal.service.*;
import com.japanApply.journal.model.*;
import com.japanApply.journal.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diary-entries")
@Tag(name = "DiaryEntry Management", description = "Operations related to managing diaries entry")
public class DiaryEntryController {
        private final DiaryEntryService diaryEntryService;
        private final UserService userService;
        private final DiaryService diaryService;
        private final LocationService locationService;
        private final VocabularyService vocabularyService;
        private final PhotoService photoService;
        private final UserRepository userRepository;
        private final DiaryRepository diaryRepository;
        private final LocationRepository locationRepository;
        private final VocabularyRepository vocabularyRepository;
        private final PhotoRepository photoRepository;
        private final SecurityService securityService;
        private final CurrentUserService currentUserService;


    public  DiaryEntryController(DiaryEntryService diaryEntryService, UserService userService, DiaryService diaryService, LocationService locationService, VocabularyService vocabularyService,PhotoService photoService, UserRepository userRepository, DiaryRepository diaryRepository, LocationRepository locationRepository, VocabularyRepository vocabularyRepository, PhotoRepository photoRepository, SecurityService securityService, CurrentUserService currentUserService) {
            this.diaryEntryService = diaryEntryService;
            this.userService = userService;
            this.diaryService = diaryService;
            this.locationService = locationService;
            this.vocabularyService = vocabularyService;
            this.photoService = photoService;
            this.userRepository = userRepository;
            this.diaryRepository = diaryRepository;
            this.locationRepository = locationRepository;
            this.vocabularyRepository = vocabularyRepository;
            this.photoRepository = photoRepository;
            this.securityService = securityService;
            this.currentUserService = currentUserService;
    }

        @Operation(summary = "Retrieve all diaries entries", description = "Fetch a list of all diary entries.")
        @ApiResponse(responseCode = "200", description = "List of diaries entries retrieved",
                content = @Content(schema = @Schema(implementation = DiaryEntry.class)))
        @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
        @GetMapping
        public ResponseEntity<List<DiaryEntry>> getAllEntries() {
            List<DiaryEntry> diaryEntries = diaryEntryService.getAllEntries();
            List<DiaryEntryResponseDto> responseDtos = diaryEntries.stream()
                    .map(DiaryEntryMapper::toDiaryEntryResponseDto)
                    .toList();
            return ResponseEntity.ok(diaryEntries);
        }

    @Operation(summary = "Retrieve a diary entry by its ID", description = "Fetch a specific diary entry using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DiaryEntry found",
                    content = @Content(schema = @Schema(implementation = Diary.class))),
            @ApiResponse(responseCode = "404", description = "DiaryEntry not found")
    })
        @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
        @GetMapping("/{id}")
        public ResponseEntity<DiaryEntryResponseDto> getDiaryEntryById(@PathVariable Long id) {
            Optional<DiaryEntry> diaryEntry = diaryEntryService.getDiaryEntryById(id);

            if (diaryEntry.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Vérifie si l'utilisateur courant peut accéder à cette entrée de journal
            if (!securityService.canAccessResource(diaryEntry.get())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(DiaryEntryMapper.toDiaryEntryResponseDto(diaryEntry.get()));
        }


    @Operation(summary = "Create a new diary entry", description = "Add a new diary entry to the database.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "DiaryEntry created successfully",
                        content = @Content(schema = @Schema(implementation = Diary.class))),
                @ApiResponse(responseCode = "400", description = "Invalid request data")
        })
        @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
        @PostMapping
        public ResponseEntity<DiaryEntryResponseDto> createEntry(@RequestBody DiaryEntryDto diaryEntryDto) {
            DiaryEntry diaryEntry = DiaryEntryMapper.toDiaryEntry(diaryEntryDto, userRepository, diaryRepository, locationRepository, vocabularyRepository, photoRepository);
            DiaryEntry createEntry = diaryEntryService.createEntry(diaryEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(DiaryEntryMapper.toDiaryEntryResponseDto(createEntry));
        }


        @Operation(summary = "Update an existing diary entry", description = "Modify an existing diary entry by its ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "DiaryEntry updated successfully",
                        content = @Content(schema = @Schema(implementation = Diary.class))),
                @ApiResponse(responseCode = "404", description = "DiaryEntry not found")
        })
        @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
        @PutMapping("/{id}")
        public ResponseEntity<DiaryEntryResponseDto> updateEntry(@PathVariable Long id, @RequestBody DiaryEntryDto diaryEntryDto) {
            Optional<DiaryEntry> diaryEntry = diaryEntryService.getDiaryEntryById(id);

            if (diaryEntry.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Vérifie si l'utilisateur courant peut modifier cette entrée de journal
            if (!securityService.canAccessResource(diaryEntry.get())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            DiaryEntry updatedDiaryEntry = DiaryEntryMapper.toDiaryEntry(diaryEntryDto, userRepository, diaryRepository, locationRepository, vocabularyRepository, photoRepository);
            updatedDiaryEntry.setId(id);

            DiaryEntry savedEntry = diaryEntryService.updateEntry(updatedDiaryEntry);
            return ResponseEntity.ok(DiaryEntryMapper.toDiaryEntryResponseDto(savedEntry));
        }

    @Operation(summary = "Delete a diary entry", description = "Remove a diary entry from the database by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "DiaryENtry deleted successfully"),
            @ApiResponse(responseCode = "404", description = "DiaryEntry not found")
    })
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntryById(@PathVariable Long id) {
        Optional<DiaryEntry> diaryEntry = diaryEntryService.getDiaryEntryById(id);

        // Si l'entrée n'existe pas, on retourne une erreur 404
        if (diaryEntry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Vérifie si l'utilisateur courant peut supprimer cette entrée
        if (!securityService.canAccessResource(diaryEntry.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        diaryEntryService.deleteEntryById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve diaries by user ID", description = "Fetch all diariesEntry belonging to a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DiariesEntry retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Diary.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DiaryEntryResponseDto>> getEntryByUserId(@PathVariable Long userId) {
        Optional<User> user = Optional.ofNullable(userService.getUserById(userId));

        // Si l'utilisateur n'existe pas
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Vérifie si l'utilisateur courant peut accéder à ces entrées de journal
        if (!securityService.canAccessResource(user.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<DiaryEntryResponseDto> diaryEntries = diaryEntryService.findEntriesByUser(user.get()).stream()
                .map(DiaryEntryMapper::toDiaryEntryResponseDto)
                .toList();
        return ResponseEntity.ok(diaryEntries);
    }


    @Operation(summary = "Retrieve diary entries by diary ID", description = "Fetch all diary entries belonging to a specific diary.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diary entries retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DiaryEntry.class))),
            @ApiResponse(responseCode = "404", description = "Diary not found")
    })
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping("/diary/{diaryId}")
    public ResponseEntity<List<DiaryEntryResponseDto>> getEntryByDiaryId(@PathVariable Long diaryId) {
        Optional<Diary> diary = diaryService.getDiaryById(diaryId);  // Pas besoin de Optional.ofNullable ici

        if (diary == null) {
            return ResponseEntity.notFound().build();
        }
        List<DiaryEntryResponseDto> diaryEntries = diaryEntryService.findEntriesByDiary(diary.get()).stream()
                .map(DiaryEntryMapper::toDiaryEntryResponseDto)
                .toList();
        return ResponseEntity.ok(diaryEntries);
    }

    @Operation(summary = "Retrieve diary entries by location ID", description = "Fetch all diary entries belonging to a specific location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diary entries retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DiaryEntry.class))),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<DiaryEntryResponseDto>> getEntryByLocationId(@PathVariable Long locationId) {
        Optional<Location> location = locationService.getLocationById(locationId);  // Pas besoin de Optional.ofNullable ici

        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        List<DiaryEntryResponseDto> diaryEntries = diaryEntryService.findEntriesByLocation(location.get()).stream()
                .map(DiaryEntryMapper::toDiaryEntryResponseDto)
                .toList();
        return ResponseEntity.ok(diaryEntries);
    }


    @Operation(summary = "Retrieve diary entries by vocabulary ID", description = "Fetch all diary entries associated with a specific vocabulary.")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diary entries retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DiaryEntry.class))),
            @ApiResponse(responseCode = "404", description = "Vocabulary not found")
    })
    @GetMapping("/vocabulary/{vocabularyId}")
    public ResponseEntity<List<DiaryEntryResponseDto>> getEntryByVocabularyId(@PathVariable Long vocabularyId) {
        Optional<Vocabulary> vocabularies = vocabularyService.getVocabularyById(vocabularyId);  // Pas besoin de Optional.ofNullable ici

        if (vocabularies == null) {
            return ResponseEntity.notFound().build();
        }
        List<DiaryEntryResponseDto> diaryEntries = diaryEntryService.findEntriesByVocabulary(vocabularies.get()).stream()
                .map(DiaryEntryMapper::toDiaryEntryResponseDto)
                .toList();
        return ResponseEntity.ok(diaryEntries);
    }



    @Operation(summary = "Retrieve diary entries by vocabulary ID", description = "Fetch all diary entries associated with a specific vocabulary.")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diary entries retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DiaryEntry.class))),
            @ApiResponse(responseCode = "404", description = "Vocabulary not found")
    })
    @GetMapping("/user/{userId}/vocabulary")
    public ResponseEntity<List<VocabularyResponseDto>> getUserVocabulary(@PathVariable Long userId) {
        // Vérification si l'utilisateur connecté est celui demandé
        if (currentUserService.getCurrentUser().getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Si l'ID ne correspond pas, réponse 403
        }

        // Récupérer toutes les entrées de journal de l'utilisateur
        List<DiaryEntry> diaryEntries = diaryEntryService.findEntriesByUser(currentUserService.getCurrentUser());

        // Extraire les vocabulaire utilisés dans ces entrées
        List<Vocabulary> vocabularyList = diaryEntries.stream()
                .flatMap(entry -> entry.getVocabularies().stream())  // Aplanir les Set<Vocabulary> en une seule liste
                .distinct()  // Enlever les doublons
                .collect(Collectors.toList());

        // Convertir les vocabulaire en DTO
        List<VocabularyResponseDto> vocabularyResponseDtos = vocabularyList.stream()
                .map(VocabularyMapper::toVocabularyResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vocabularyResponseDtos);
    }

   /* @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping("/user/{userId}/diary/{diaryId}")
    public ResponseEntity<List<DiaryEntryResponseDto>> getDiaryEntriesByUserAndDiary(
            @PathVariable Long userId,
            @PathVariable Long diaryId) {

        // Vérification si l'utilisateur connecté est celui demandé
        if (currentUserService.getCurrentUser().getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Si l'ID ne correspond pas, réponse 403
        }

        // Récupérer l'objet User
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si l'utilisateur n'existe pas
        }
        User user = userOptional.get();

        // Récupérer le Diary pour l'utilisateur donné
        Optional<Diary> diaryOptional = diaryService.findByIdAndUser(diaryId, user);

        // Si le Diary n'est pas trouvé ou s'il n'appartient pas à l'utilisateur
        if (diaryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 si le journal n'existe pas ou n'appartient pas à l'utilisateur
        }

        // Récupérer toutes les entrées de journal associées à ce Diary
        List<DiaryEntry> diaryEntries = diaryEntryService.findEntriesByDiary(diaryOptional.get());

        // Convertir les entrées de journal en DTO
        List<DiaryEntryResponseDto> diaryEntryResponseDtos = diaryEntries.stream()
                .map(DiaryEntryMapper::toDiaryEntryResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(diaryEntryResponseDtos);
    }*/


    /*@PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping("/user/{userId}/location/{locationId}")
    public ResponseEntity<List<Location>> getUserLocation(@PathVariable Long userId, @PathVariable Long locationId) {
        // Vérification si l'utilisateur connecté est celui demandé
        if (currentUserService.getCurrentUser().getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Si l'ID ne correspond pas, réponse 403
        }

        // Récupérer la Location pour l'utilisateur donné
        Optional<Location> locationOptional = locationService.findByIdAndUser(locationId, userId);

        // Si la Location n'est pas trouvée ou qu'elle n'appartient pas à l'utilisateur
        if (locationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 si l'utilisateur n'a pas accès à cette Location
        }

        // Récupérer toutes les entrées de location associées à cette Location
        List<DiaryEntryResponseDto> diaryEntryResponseDtos = locationService.findEntriesByLocation(locationOptional.get());

        // Convertir les entrées de location en DTO
        List<Location> locationEntryResponseDtos = locationEntries.stream()
                .map(LocationEntryMapper::toLocationEntryResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(locationEntryResponseDtos);
    }*/


}
