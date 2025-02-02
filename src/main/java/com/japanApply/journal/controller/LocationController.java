package com.japanApply.journal.controller;

import com.japanApply.journal.configuration.InUseExeption.LocationInUseException;
import com.japanApply.journal.dto.LocationDto;
import com.japanApply.journal.dto.mapper.LocationMapper;
import com.japanApply.journal.model.*;
import com.japanApply.journal.service.*;
import com.japanApply.journal.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;
    private final CountryRepository countryRepository;  // Added the CountryRepository to map the country

    @Autowired
    public LocationController(LocationService locationService, CountryRepository countryRepository) {
        this.locationService = locationService;
        this.countryRepository = countryRepository;
    }

    /**
     * Create a new location.
     *
     * @param locationDto The LocationDto object to be mapped to a Location entity and created.
     * @return The created Location object.
     */
    @Operation(summary = "Create a new location", description = "Save a new location entity in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location successfully created", content = @Content(schema = @Schema(implementation = Location.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody LocationDto locationDto) {
        Location location = LocationMapper.toLocation(locationDto, countryRepository);
        Location savedLocation = locationService.saveLocation(location);
        return ResponseEntity.ok(savedLocation);
    }

    /**
     * Retrieve a location by its ID.
     *
     * @param id The ID of the location to retrieve.
     * @return The Location object if found.
     */
    @Operation(summary = "Retrieve a location by ID", description = "Fetch a location entity based on its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location found", content = @Content(schema = @Schema(implementation = Location.class))),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Optional<Location> location = locationService.getLocationById(id);
        return location.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve all locations.
     *
     * @return A list of all Location objects.
     */
    @Operation(summary = "Retrieve all locations", description = "Fetch all available location entities.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all locations")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    /**
     * Delete a location by its ID.
     *
     * @param id The ID of the location to delete.
     * @return HTTP 204 if the operation was successful.
     */
    @Operation(summary = "Delete a location by ID", description = "Remove a location entity based on its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Location successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "400", description = "Location is in use and cannot be deleted")
    })
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build();
        } catch (LocationInUseException ex) {
            // Returns a 400 error with the custom message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Update an existing location.
     *
     * @param id The ID of the location to update.
     * @param locationDto The LocationDto containing the updated data.
     * @return The updated Location object.
     */
    @Operation(summary = "Update an existing location", description = "Update a location entity in the database by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location successfully updated", content = @Content(schema = @Schema(implementation = Location.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @PreAuthorize("hasAnyRole('PRENIUM','ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody LocationDto locationDto) {
        Optional<Location> locationOptional = locationService.getLocationById(id);
        if (locationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Location location = locationOptional.get();
        // Map the DTO to the Location entity, assuming you have a method for this
        Location updatedLocation = LocationMapper.toLocation(locationDto, countryRepository);

        // Update the fields of the location with the new data
        location.setLocationName(updatedLocation.getLocationName());
        location.setLatitude(updatedLocation.getLatitude());
        location.setLongitude(updatedLocation.getLongitude());
        location.setRegion(updatedLocation.getRegion());
        location.setCountry(updatedLocation.getCountry());
        location.setLocationType(updatedLocation.getLocationType());

        Location savedLocation = locationService.saveLocation(location);

        return ResponseEntity.ok(savedLocation);
    }

    /**
     * Filter locations based on optional parameters.
     *
     * @param locationName Filter by location name.
     * @param locationType Filter by location type.
     * @param country Filter by country.
     * @return A filtered list of locations.
     */
    @Operation(summary = "Filter locations", description = "Fetch locations based on optional filters such as name, type, or country.")
    @ApiResponse(responseCode = "200", description = "Filtered locations retrieved successfully")
    @PreAuthorize("hasAnyRole('BASE','PRENIUM','ADMINISTRATOR')")
    @GetMapping("/filter")
    public List<Location> filterLocations(
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) LocationType locationType,
            @RequestParam(required = false) Country country) {

        if (locationName != null && locationType != null && country != null) {
            return locationService.getLocationsByNameTypeAndCountry(locationName, locationType, country);
        } else if (locationName != null) {
            return locationService.getLocationsByName(locationName);
        } else if (locationType != null) {
            return locationService.getLocationsByType(locationType);
        } else if (country != null) {
            return locationService.getLocationsByCountry(country);
        } else {
            return locationService.getAllLocations();
        }
    }
}
