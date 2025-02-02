package com.japanApply.journal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "diary")
public class Diary implements Ownable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameDiary;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "photo_cover_id")
    private Photo photoCover;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    private LocalDate dateCreateDiary;

    private LocalDate dateStartDiary;

    private LocalDate dateEndDiary;

    @Column(length = 2000)
    private String descriptionDiary;

    private Boolean publicDiary;


    public Diary() {}


    public Diary(Long id, String nameDiary, User user, Photo photoCover, Country country,
                 LocalDate dateCreateDiary, LocalDate dateStartDiary, LocalDate dateEndDiary,
                 String descriptionDiary, Boolean publicDiary) {
        this.id = id;
        this.nameDiary = nameDiary;
        this.user = user;
        this.photoCover = photoCover;
        this.country = country;
        this.dateCreateDiary = dateCreateDiary;
        this.dateStartDiary = dateStartDiary;
        this.dateEndDiary = dateEndDiary;
        this.descriptionDiary = descriptionDiary;
        this.publicDiary = publicDiary;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameDiary() {
        return nameDiary;
    }

    public void setNameDiary(String nameDiary) {
        this.nameDiary = nameDiary;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Photo getPhotoCover() {
        return photoCover;
    }

    public void setPhotoCover(Photo photoCover) {
        this.photoCover = photoCover;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public LocalDate getDateCreateDiary() {
        return dateCreateDiary;
    }

    public void setDateCreateDiary(LocalDate dateCreateDiary) {
        this.dateCreateDiary = dateCreateDiary;
    }

    public LocalDate getDateStartDiary() {
        return dateStartDiary;
    }

    public void setDateStartDiary(LocalDate dateStartDiary) {
        this.dateStartDiary = dateStartDiary;
    }

    public LocalDate getDateEndDiary() {
        return dateEndDiary;
    }

    public void setDateEndDiary(LocalDate dateEndDiary) {
        this.dateEndDiary = dateEndDiary;
    }

    public String getDescriptionDiary() {
        return descriptionDiary;
    }

    public void setDescriptionDiary(String descriptionDiary) {
        this.descriptionDiary = descriptionDiary;
    }

    public Boolean getPublicDiary() {
        return publicDiary;
    }

    public void setPublicDiary(Boolean publicDiary) {
        this.publicDiary = publicDiary;
    }

    @Override
    public User getOwner() {
        return user;

    }
}
