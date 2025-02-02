package com.japanApply.journal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users") // Évite "user", mot-clé réservé
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String email;
    private String countryOfOrigin;
    private String phone;

    @Enumerated(EnumType.STRING)
    private RangType rangType = RangType.BASE;

    @JsonIgnore
    private String password;

    private LocalDate creationDate;

    public User() {}

    /*public User(Long id, String username, String email, String countryOfOrigin,
                String phone, String password, LocalDate creationDate, RangType rangType) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.countryOfOrigin = countryOfOrigin;
        this.phone = phone;
        this.password = password;
        this.creationDate = creationDate;
        this.rangType = rangType;
    }*/

    public User(String username, String email, String countryOfOrigin,
                String phone,String password, LocalDate creationDate, RangType rangType) {
        this.username = username;
        this.email = email;
        this.countryOfOrigin = countryOfOrigin;
        this.phone = phone;
        this.password = password;
        this.creationDate = creationDate;
        this.rangType = rangType;
    }



    // Getters et Setters

    public long getId() { return id; }
    public void setId(long idUser) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCountryOfOrigin() { return countryOfOrigin; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public RangType getRangType() { return rangType; }
    public void setRangType(RangType rangType) { this.rangType = rangType; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }



}

