package com.japanApply.journal.repository;

import com.japanApply.journal.model.Location;
import com.japanApply.journal.model.Country;
import com.japanApply.journal.model.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    // Find by locationName
    List<Location> findByLocationNameContainingIgnoreCase(String locationName);

    // Find by  locationType
    List<Location> findByLocationType(LocationType locationType);

    // Find by  country
    List<Location> findByCountry(Country country);

    // Find by locationName, locationType and country
    List<Location> findByLocationNameContainingIgnoreCaseAndLocationTypeAndCountry(
            String locationName, LocationType locationType, Country country);

}
