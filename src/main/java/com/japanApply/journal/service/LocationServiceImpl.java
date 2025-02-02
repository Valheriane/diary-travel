package com.japanApply.journal.service;

import com.japanApply.journal.model.*;
import com.japanApply.journal.repository.*;
import com.japanApply.journal.configuration.InUseExeption.LocationInUseException;
import com.japanApply.journal.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final DiaryEntryRepository diaryEntryRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, DiaryEntryRepository diaryEntryRepository) {
        this.locationRepository = locationRepository;
        this.diaryEntryRepository = diaryEntryRepository;
    }

    @Override
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public void deleteLocation(Long id) {

        boolean isInUse = diaryEntryRepository.existsByLocationId(id); // Suppose que tu as une méthode pour ça

        if (isInUse) {
            throw new LocationInUseException("Location is currently in use and cannot be deleted.");
        }

        locationRepository.deleteById(id);
    }


    @Override
    public List<Location> getLocationsByName(String locationName) {
        return locationRepository.findByLocationNameContainingIgnoreCase(locationName);
    }

    @Override
    public List<Location> getLocationsByType(LocationType locationType) {
        return locationRepository.findByLocationType(locationType);
    }

    @Override
    public List<Location> getLocationsByCountry(Country country) {
        return locationRepository.findByCountry(country);
    }

    @Override
    public List<Location> getLocationsByNameTypeAndCountry(String locationName, LocationType locationType, Country country) {
        return locationRepository.findByLocationNameContainingIgnoreCaseAndLocationTypeAndCountry(locationName, locationType, country);
    }



}
