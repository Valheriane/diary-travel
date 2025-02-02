package com.japanApply.journal.repository;

import com.japanApply.journal.model.Photo;
import com.japanApply.journal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUserId(Long userId); // Recherche par utilisateur
}
