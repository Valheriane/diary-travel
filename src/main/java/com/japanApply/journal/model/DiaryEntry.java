package com.japanApply.journal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "diary_entry")
public class DiaryEntry implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String note;
    private LocalDate dateCreated;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany
    @JoinTable(
            name = "diary_entry_vocabulary",
            joinColumns = @JoinColumn(name = "diary_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_id")
    )
    private Set<Vocabulary> vocabularies;

    @ManyToMany
    @JoinTable(
            name = "diary_entry_photo",
            joinColumns = @JoinColumn(name = "diary_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private Set<Photo> photos;

    public DiaryEntry() {}

    public DiaryEntry(Long id, String title, String note, LocalDate dateCreated, String description, User user, Diary diary, Location location, Set<Vocabulary> vocabularies) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.dateCreated = dateCreated;
        this.description = description;
        this.user = user;
        this.diary = diary;
        this.location = location;
        this.vocabularies = vocabularies;
        this.photos = photos;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDate getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Diary getDiary() { return diary; }
    public void setDiary(Diary diary) { this.diary = diary; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Set<Vocabulary> getVocabularies() { return vocabularies; }
    public void setVocabularies(Set<Vocabulary> vocabularies) { this.vocabularies = vocabularies; }

    public Set<Photo> getPhotos() { return photos; }
    public void setPhotos(Set<Photo> photos) { this.photos = photos; }

    @Override
    public User getOwner() {
        return user;

    }
}
