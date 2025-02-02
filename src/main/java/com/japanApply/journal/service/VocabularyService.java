package com.japanApply.journal.service;

import com.japanApply.journal.model.Vocabulary;
import com.japanApply.journal.model.Language;

import java.util.List;
import java.util.Optional;

public interface VocabularyService {

    List<Vocabulary> getAllVocabularies();

    Optional<Vocabulary> getVocabularyById(Long id);

    Vocabulary createVocabulary(Vocabulary vocabulary);

    Optional<Vocabulary> updateVocabulary(Long id, Vocabulary vocabulary);

    void deleteVocabulary(Long id);

    List<Vocabulary> getVocabulariesByLanguage(Language language);


}
