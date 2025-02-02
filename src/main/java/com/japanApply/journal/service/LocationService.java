package com.japanApply.journal.service;

import com.japanApply.journal.model.Location;
import com.japanApply.journal.model.Country;
import com.japanApply.journal.model.LocationType;
import com.japanApply.journal.model.User;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    Location saveLocation(Location location);

    Optional<Location> getLocationById(Long id);

    List<Location> getAllLocations();

    void deleteLocation(Long id);

    // Méthodes
    List<Location> getLocationsByName(String locationName);

    List<Location> getLocationsByType(LocationType locationType);

    List<Location> getLocationsByCountry(Country country);

    List<Location> getLocationsByNameTypeAndCountry(String locationName, LocationType locationType, Country country);


}
