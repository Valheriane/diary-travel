package com.japanApply.journal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "country")
public class Country { // Correction de l'orthographe
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private RegionType region;


    public Country() {}


    public Country(Long id, String name, RegionType region) {
        this.id = id;
        this.name = name;
        this.region = region;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RegionType getRegion() {
        return region;
    }

    public void setRegion(RegionType region) {
        this.region = region;
    }
}
