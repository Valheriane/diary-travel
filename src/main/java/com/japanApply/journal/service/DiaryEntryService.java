package com.japanApply.journal.service;

import com.japanApply.journal.dto.DiaryEntryDto;
import com.japanApply.journal.model.*;

import java.util.*;

public interface DiaryEntryService {

    List<DiaryEntry> getAllEntries();

    Optional<DiaryEntry> getDiaryEntryById(Long id);

    DiaryEntry createEntry(DiaryEntry diaryEntry);

    DiaryEntry updateEntry(DiaryEntry diaryEntry);

    void deleteEntryById(Long id);


    List<DiaryEntry> findEntriesByUser(User user);

    List<DiaryEntry> findEntriesByDiary(Diary diary);

    List<DiaryEntry> findEntriesByLocation(Location location);

    List<DiaryEntry> findEntriesByVocabulary(Vocabulary vocabulary);

    List<Vocabulary> getUserVocabulary(Long userId);



}
