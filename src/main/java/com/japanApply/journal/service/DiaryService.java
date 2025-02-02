package com.japanApply.journal.service;

import com.japanApply.journal.model.Diary;
import com.japanApply.journal.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Diary service interface
 */
public interface DiaryService {

    /**
     * Find a diary by its ID.
     * @param id the ID of the diary.
     * @return an Optional containing the diary if found.
     */
    Optional<Diary> getDiaryById(Long id);

    /**
     * Find all diaries.
     * @return a list of all diaries.
     */
    List<Diary> getAllDiaries();

    /**
     * Create a new diary.
     * @param diary the diary to create.
     * @return the created diary.
     */
    Diary createDiary(Diary diary);

    /**
     * Update an existing diary.
     * @param diary the diary to update.
     * @return the updated diary.
     */
    Diary updateDiary(Diary diary);

    /**
     * Delete a diary by its ID.
     * @param id the ID of the diary to delete.
     */
    void deleteDiaryById(Long id);

    /**
     * Find diaries by user ID.
     * @param user the ID of the user.
     * @return a list of diaries owned by the user.
     */
    List<Diary> findDiariesByUser(User user);

    /**
     * Find public diaries.
     * @return a list of public diaries.
     */
    List<Diary> findPublicDiaries();

    Optional<Diary> findByIdAndUser(Long diaryId,  User user);
}
