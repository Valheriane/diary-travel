package com.japanApply.journal.repository;

import com.japanApply.journal.model.Diary;
import com.japanApply.journal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Diary repository
 */
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    /**
     * Find diaries by user ID.
     * @param user the ID of the user.
     * @return a list of diaries owned by the user.
     */
    List<Diary> findByUser(User user);

    boolean existsByPhotoCoverId(Long photoCoverId);

    /**
     * Find public diaries.
     * @return a list of public diaries.
     */
    List<Diary> findByPublicDiaryTrue();

    public Optional<Diary> findByIdAndUser(Long diaryId, User user);


}
