package com.japanApply.journal.service;

import com.japanApply.journal.configuration.InUseExeption.LocationInUseException;
import com.japanApply.journal.configuration.InUseExeption.VocabularyInUseException;
import com.japanApply.journal.model.Vocabulary;
import com.japanApply.journal.model.Language;
import com.japanApply.journal.repository.VocabularyRepository;
import com.japanApply.journal.repository.DiaryEntryRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final DiaryEntryRepository diaryEntryRepository;

    public VocabularyServiceImpl(VocabularyRepository vocabularyRepository, DiaryEntryRepository diaryEntryRepository) {
        this.vocabularyRepository = vocabularyRepository;
        this.diaryEntryRepository = diaryEntryRepository;
    }

    @Override
    public List<Vocabulary> getAllVocabularies() {
        return vocabularyRepository.findAll();
    }

    @Override
    public Optional<Vocabulary> getVocabularyById(Long id) {
        return vocabularyRepository.findById(id);
    }

    @Override
    public Vocabulary createVocabulary(Vocabulary vocabulary) {
        return vocabularyRepository.save(vocabulary);
    }

    @Override
    public Optional<Vocabulary> updateVocabulary(Long id, Vocabulary vocabulary) {
        return vocabularyRepository.findById(id).map(existingVocabulary -> {
            existingVocabulary.setExpression(vocabulary.getExpression());
            existingVocabulary.setTranslation(vocabulary.getTranslation());
            existingVocabulary.setUsageExample(vocabulary.getUsageExample());
            existingVocabulary.setLanguageExpression(vocabulary.getLanguageExpression());
            existingVocabulary.setLanguageTranslation(vocabulary.getLanguageTranslation());
            return vocabularyRepository.save(existingVocabulary);
        });
    }

    @Override
    public void deleteVocabulary(Long id) {
        boolean isInUse = diaryEntryRepository.existsByVocabularyId(id); // Suppose que tu as une méthode pour ça

        if (isInUse) {
            throw new VocabularyInUseException("Vocabulary is currently in use and cannot be deleted.");
        }

        diaryEntryRepository.deleteById(id);
    }

    @Override
    public List<Vocabulary> getVocabulariesByLanguage(Language language) {
        return vocabularyRepository.findByLanguageExpression(language);
    }

}
