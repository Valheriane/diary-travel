package com.japanApply.journal.service;

import com.japanApply.journal.model.Country;
import com.japanApply.journal.model.RegionType;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Country entities.
 */
public interface CountryService {

    Optional<Country> get(Long id);

    Optional<Country> create(Country country);

    Optional<Country> update(Country country);

    void delete(Country country);

    List<Country> getAll();

    /**
     * Get countries by region.
     * @param region the region to filter by
     * @return list of countries in the specified region
     */
    List<Country> getByRegion(RegionType region);

    /**
     * Get a country by its name.
     * @param name the name of the country
     * @return the country with the specified name
     */
    Optional<Country> getByName(String name);
}
