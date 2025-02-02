package com.japanApply.journal.controller;

import com.japanApply.journal.model.Country;
import com.japanApply.journal.model.RegionType;
import com.japanApply.journal.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The CountryController class provides REST endpoints for managing Country entities.
 * This includes operations such as retrieving all countries, retrieving a specific country by ID or name,
 * creating, updating, and deleting countries, as well as filtering countries by region.
 */
@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Retrieves all countries in the system.
     *
     * @return A list of all countries as a ResponseEntity.
     */
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @Operation(summary = "Get all countries", description = "Fetches a list of all countries available in the system.")
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryService.getAll();
        return ResponseEntity.ok(countries);
    }

    /**
     * Retrieves a specific country by its unique ID.
     *
     * @param id The ID of the country to retrieve.
     * @return The country with the specified ID, or a 404 status if not found.
     */
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @Operation(summary = "Get country by ID", description = "Fetches a country from the system by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        Optional<Country> country = countryService.get(id);
        return country.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new country in the system.
     *
     * @param country The country to be created.
     * @return A ResponseEntity with the created country or a 400 status if the creation fails.
     */
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @Operation(summary = "Create a new country", description = "Creates a new country in the system.")
    @PostMapping
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        Optional<Country> createdCountry = countryService.create(country);
        return createdCountry.map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    /**
     * Updates an existing country based on its ID.
     *
     * @param id     The ID of the country to update.
     * @param country The updated country information.
     * @return The updated country, or a 404 status if the country with the specified ID was not found.
     */
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @Operation(summary = "Update an existing country", description = "Updates a country based on the provided ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @RequestBody Country country) {
        // Ensure the ID matches the path variable
        if (!id.equals(country.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Country> updatedCountry = countryService.update(country);
        return updatedCountry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a country by its ID.
     *
     * @param id The ID of the country to delete.
     * @return A ResponseEntity with no content if successful, or a 404 status if the country was not found.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @Operation(summary = "Delete a country", description = "Deletes a country from the system by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        Optional<Country> country = countryService.get(id);
        if (country.isPresent()) {
            countryService.delete(country.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves countries by their region type.
     *
     * @param region The region type to filter countries by.
     * @return A list of countries belonging to the specified region.
     */
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @Operation(summary = "Get countries by region", description = "Fetches a list of countries filtered by the specified region.")
    @GetMapping("/region/{region}")
    public ResponseEntity<List<Country>> getCountriesByRegion(@PathVariable RegionType region) {
        List<Country> countries = countryService.getByRegion(region);
        return ResponseEntity.ok(countries);
    }

    /**
     * Retrieves a country by its name.
     *
     * @param name The name of the country to retrieve.
     * @return The country with the specified name, or a 404 status if not found.
     */
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @Operation(summary = "Get country by name", description = "Fetches a country from the system by its name.")
    @GetMapping("/name/{name}")
    public ResponseEntity<Country> getCountryByName(@PathVariable String name) {
        Optional<Country> country = countryService.getByName(name);
        return country.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
