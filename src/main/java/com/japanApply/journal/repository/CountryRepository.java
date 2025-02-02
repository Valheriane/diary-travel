package com.japanApply.journal.repository;

import com.japanApply.journal.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for the Country entity.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    /**
     * Find all countries by region.
     * @param region the region to filter by
     * @return list of countries in the specified region
     */
    List<Country> findByRegion(Enum region);

    /**
     * Find a country by its name.
     * @param name the name of the country
     * @return the country with the specified name
     */
    Country findByName(String name);
}
