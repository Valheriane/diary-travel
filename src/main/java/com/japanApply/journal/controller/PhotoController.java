package com.japanApply.journal.controller;

import com.japanApply.journal.configuration.InUseExeption.LocationInUseException;
import com.japanApply.journal.configuration.InUseExeption.PhotoInUseException;
import com.japanApply.journal.dto.PhotoDto;
import com.japanApply.journal.dto.PhotoResponseDto;
import com.japanApply.journal.dto.mapper.PhotoMapper;
import com.japanApply.journal.model.DiaryEntry;
import com.japanApply.journal.model.Photo;
import com.japanApply.journal.model.User;
import com.japanApply.journal.service.*;
import com.japanApply.journal.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/photos")
@Tag(name = "Photo Management", description = "APIs for managing photos in the application.")
public class PhotoController {

    private final PhotoService photoService;
    private final UserRepository userRepository; // Correction: pass a UserRepository
    private final SecurityService securityService;
    private final UserService userService;

    public PhotoController(PhotoService photoService, UserRepository userRepository, SecurityService securityService, UserService userService) {
        this.photoService = photoService;
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Get all photos", description = "Retrieve a list of all photos available in the system.")
    public List<PhotoResponseDto> getAllPhotos() {
        List<Photo> photos = photoService.getAllPhotos();
        return photos.stream()
                .map(PhotoMapper::toPhotoResponseDto) // Correction
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @Operation(
            summary = "Get photo by ID",
            description = "Retrieve details of a photo using its unique ID."
    )
    public ResponseEntity<PhotoResponseDto> getPhotoById(
            @PathVariable
            @Parameter(description = "The unique ID of the photo to retrieve.", example = "1")
            Long id
    ) {
        Photo photo = photoService.getPhotoById(id);

        if (photo == null) {
            return ResponseEntity.notFound().build();
        }

        // Checks if the current user can access this photo
        if (!securityService.canAccessResource(photo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(PhotoMapper.toPhotoResponseDto(photo));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @Operation(
            summary = "Create a new photo",
            description = "Add a new photo to the system by providing its details."
    )
    public ResponseEntity<PhotoResponseDto> createPhoto(
            @RequestBody
            @Parameter(description = "The photo object containing the details of the photo to be created.")
            PhotoDto photoDto
    ) {
        Photo photo = PhotoMapper.toPhoto(photoDto, userRepository); // Correction
        Photo savedPhoto = photoService.savePhoto(photo);
        return ResponseEntity.ok(PhotoMapper.toPhotoResponseDto(savedPhoto)); // Correction
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a photo by ID",
            description = "Remove a photo from the system using its unique ID."
    )
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Photo successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Photo not found"),
            @ApiResponse(responseCode = "400", description = "Photo is in use and cannot be deleted")
    })
    public ResponseEntity<String> deletePhoto(@PathVariable Long id) {
        Photo photo = photoService.getPhotoById(id);

        if (photo == null) {
            return ResponseEntity.notFound().build();
        }

        // Checks if the current user can delete this photo
        if (!securityService.canAccessResource(photo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this photo.");
        }

        try {
            photoService.deletePhoto(id);
            return ResponseEntity.noContent().build();
        } catch (PhotoInUseException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve Photos by User ID",
            description = "Fetch all photos uploaded by a specific user. This operation ensures that the user exists and checks if the current user has the necessary permissions to access these photos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Photos retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PhotoResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden access, user cannot access these photos"
            )
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('PREMIUM','ADMINISTRATOR')")
    public ResponseEntity<List<PhotoResponseDto>> getPhotosByUserId(
            @PathVariable
            @Parameter(description = "The ID of the user whose photos are to be retrieved.")
            Long userId
    ) {
        // Retrieve the user
        Optional<User> user = Optional.ofNullable(userService.getUserById(userId));

        // Checks if the user exists
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Checks if the current user can access the photos
        if (!securityService.canAccessResource(user.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Retrieve and convert the photos
        List<PhotoResponseDto> photos = photoService.getPhotosByUserId(user.get().getId()).stream()
                .map(PhotoMapper::toPhotoResponseDto)
                .toList();

        return ResponseEntity.ok(photos);
    }

}
