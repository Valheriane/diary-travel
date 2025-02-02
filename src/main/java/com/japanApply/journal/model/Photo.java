package com.japanApply.journal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "photos")
public class Photo implements Ownable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private LocalDate uploadedAt;

    // Relation with User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "photos")
    private Set<DiaryEntry> diaryEntries;

    public Photo() {}

    public Photo(Long id, String url, LocalDate uploadedAt, User user, Set<DiaryEntry> diaryEntries) {
        this.id = id;
        this.url = url;
        this.uploadedAt = uploadedAt;
        this.user = user;
        this.diaryEntries = diaryEntries;

    }



    public Long getid() { return id; }
    public void setid(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public LocalDate getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDate uploadedAt) { this.uploadedAt = uploadedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<DiaryEntry> getDiaryEntries() { return diaryEntries; }
    public void setDiaryEntries(Set<DiaryEntry> diaryEntries) { this.diaryEntries = diaryEntries; }

    @Override
    public User getOwner() {
        return user;

    }
}
