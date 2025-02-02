package com.japanApply.journal.repository;

import com.japanApply.journal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    /**
     * Find diary entries by user ID.
     * @param user the user entity.
     * @return a list of diary entries owned by the user.
     */
    List<DiaryEntry> findByUser(User user);

    // Find diary entries by diary ID
    List<DiaryEntry> findByDiary(Diary diary);

    // Find diary entries by location ID
    List<DiaryEntry> findByLocation(Location location);

    // Find diary entries containing specific vocabulary
    List<DiaryEntry> findByVocabulariesContaining(Vocabulary vocabularies);

    boolean existsByLocationId(Long locationId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM DiaryEntry d JOIN d.photos p WHERE p.id = :photoId")
    boolean existsByPhotoId(@Param("photoId") Long photoId);

    @Query("SELECT DISTINCT v FROM Vocabulary v " +
            "JOIN v.diaryEntries d " +
            "WHERE d.user.id = :userId")
    List<Vocabulary> findVocabulariesByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM DiaryEntry d JOIN d.vocabularies p WHERE p.id = :vocabularyId")
    boolean existsByVocabularyId(@Param("vocabularyId") Long vocabularyId);

}
