package com.japanApply.journal.service;

import com.japanApply.journal.model.Country;
import com.japanApply.journal.model.RegionType;
import com.japanApply.journal.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Optional<Country> get(Long id) {
        return countryRepository.findById(id);
    }

    @Override
    public Optional<Country> create(Country country) {
        return Optional.of(countryRepository.save(country));
    }

    @Override
    public Optional<Country> update(Country country) {
        return Optional.of(countryRepository.save(country));
    }

    @Override
    public void delete(Country country) {
        countryRepository.delete(country);
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public List<Country> getByRegion(RegionType region) {
        return countryRepository.findByRegion(region);
    }

    @Override
    public Optional<Country> getByName(String name) {
        return Optional.ofNullable(countryRepository.findByName(name));
    }
}
