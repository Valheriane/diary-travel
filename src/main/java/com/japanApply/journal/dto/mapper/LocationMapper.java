package com.japanApply.journal.dto.mapper;

import com.japanApply.journal.dto.DiaryDto;
import com.japanApply.journal.dto.DiaryResponseDto;
import com.japanApply.journal.dto.LocationDto;
import com.japanApply.journal.model.*;
import com.japanApply.journal.repository.*;

public class LocationMapper {

    public static Location toLocation(LocationDto locationDto, CountryRepository countryRepository){
        Location location = new Location();
        location.setLocationName(locationDto.locationName());
        location.setLatitude(locationDto.latitude());
        location.setLongitude(location.getLongitude());
        location.setRegion(location.getRegion());

        Country country = countryRepository.findById(locationDto.countryId())
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));
        location.setCountry(country);

        try {
            location.setLocationType(LocationType.valueOf(locationDto.locationType().toUpperCase())); // On met en majuscules pour éviter des erreurs de casse
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid location type: " + locationDto.locationType());
        }

        return location;
    }






}