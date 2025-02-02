package com.japanApply.journal.controller;

import com.japanApply.journal.dto.AuthenticationRequestCreateDto;
import com.japanApply.journal.model.RangType;
import com.japanApply.journal.model.User;
import com.japanApply.journal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles HTTP requests related to user management: create, update, delete, retrieve, and filter users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/test")
    public String testUsersEndpoint() {
        return "Endpoint /api/users/test is working!";
    }

    /**
     * Creates a new user.
     *
     * @param authenticationRequestCreateDto User details for creation.
     * @return The created user.
     */
    @Operation(summary = "Create a new user", description = "Adds a new user to the system.")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody AuthenticationRequestCreateDto authenticationRequestCreateDto) {
        userService.getByUsername(authenticationRequestCreateDto.email())
                .ifPresent(user -> {
                    throw new RuntimeException("Error: user already exists, please sign in");
                });

        User newUser = new User();
        newUser.setEmail(authenticationRequestCreateDto.email());
        newUser.setUsername(authenticationRequestCreateDto.username());
        newUser.setPassword(encoder.encode(authenticationRequestCreateDto.password()));
        newUser.setRangType(RangType.BASE); // Default rank
        newUser.setCountryOfOrigin(authenticationRequestCreateDto.countryOfOrigin());
        newUser.setPhone(authenticationRequestCreateDto.phone());
        newUser.setCreationDate(LocalDate.now());

        User createdUser = userService.createUser(newUser)
                .orElseThrow(() -> new RuntimeException("Error occurred while adding user"));

        return ResponseEntity.ok(createdUser);
    }

    /**
     * Updates an existing user by ID.
     *
     * @param id   User ID.
     * @param user Updated user details.
     * @return The updated user.
     */
    //@PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')") // tour de passe passe en attendant d'améliorer l'api
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Update an existing user", description = "Updates the details of a user based on their ID.")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user, @AuthenticationPrincipal User authenticatedUser) {
        /*if (!Long.valueOf(authenticatedUser.getId()).equals(id) && !authenticatedUser.getRangType().equals(RangType.ADMINISTRATOR)) {
            throw new AccessDeniedException("You do not have permission to modify this user.");
        }*/

        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id User ID.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Delete a user", description = "Removes a user from the system by their ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id User ID.
     * @return The user.
     */
    //@PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')") // tour de passe passe en attendant d'améliorer l'api
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Get user by ID", description = "Fetches a user from the system by their unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        /*if (authenticatedUser == null) {
            throw new AccessDeniedException("You are not authenticated.");
        }

        if (!Long.valueOf(authenticatedUser.getId()).equals(id) && !authenticatedUser.getRangType().equals(RangType.ADMINISTRATOR)) {
            throw new AccessDeniedException("You do not have permission to view this user.");
        }*/

        return ResponseEntity.ok(userService.getUserById(id));
    }


    /**
     * Retrieves all users in the system.
     *
     * @return List of all users.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Get all users", description = "Fetches all users available in the system.")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves users filtered by their country of origin.
     *
     * @param country Country to filter by.
     * @return List of users from the specified country.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Filter users by country", description = "Fetches all users who originate from a specific country.")
    @GetMapping("/filterByCountry/{country}")
    public ResponseEntity<List<User>> getUsersByCountry(@PathVariable String country) {
        return ResponseEntity.ok(userService.getUsersByCountryOfOrigin(country));
    }

    /**
     * Retrieves users filtered by their rank type.
     *
     * @param rang Rank type to filter by.
     * @return List of users with the specified rank type.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Filter users by rank", description = "Fetches all users with a specific rank type.")
    @GetMapping("/filterByRang/{rang}")
    public ResponseEntity<List<User>> getUsersByRang(@PathVariable RangType rang) {
        return ResponseEntity.ok(userService.getUsersByRangType(rang));
    }

    /**
     * Retrieves users filtered by both their country of origin and rank type.
     *
     * @param country Country to filter by.
     * @param rang    Rank type to filter by.
     * @return List of users matching both filters.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Filter users by country and rank", description = "Fetches all users who match both the specified country and rank type.")
    @GetMapping("/filterByCountryAndRang")
    public ResponseEntity<List<User>> getUsersByCountryAndRang(@RequestParam String country, @RequestParam RangType rang) {
        return ResponseEntity.ok(userService.getUsersByCountryAndRang(country, rang));
    }
}
