package com.japanApply.journal.service;

import com.japanApply.journal.dto.DiaryEntryDto;
import com.japanApply.journal.dto.mapper.DiaryEntryMapper;
import com.japanApply.journal.model.*;
import com.japanApply.journal.repository.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiaryEntryServiceImpl implements DiaryEntryService {

    private final DiaryEntryRepository diaryEntryRepository;

    public DiaryEntryServiceImpl(DiaryEntryRepository diaryEntryRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
    }

    @Override
    public Optional<DiaryEntry> getDiaryEntryById(Long id) {
        return diaryEntryRepository.findById(id);
    }

    @Override
    public List<DiaryEntry> getAllEntries(){
        return diaryEntryRepository.findAll();
    }

    @Override
    public DiaryEntry createEntry(DiaryEntry diaryEntry){
        return diaryEntryRepository.save(diaryEntry);
    }

    @Override
    public DiaryEntry updateEntry(DiaryEntry diaryEntry){
        if(diaryEntryRepository.existsById(diaryEntry.getId())){
            return diaryEntryRepository.save(diaryEntry);
        } else {
            throw new IllegalArgumentException("DiaryEntry not found");
        }
    }

    @Override
    public void deleteEntryById(Long id){
        if (diaryEntryRepository.existsById(id)){
            diaryEntryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("DiaryEntry not found");
        }
    }

    @Override
    public List<DiaryEntry> findEntriesByUser (User user){
        return diaryEntryRepository.findByUser(user);
    }
    @Override
    public List<DiaryEntry> findEntriesByDiary (Diary diary){
        return diaryEntryRepository.findByDiary(diary);
    }
    @Override
    public List<DiaryEntry> findEntriesByLocation (Location location){
        return diaryEntryRepository.findByLocation(location);
    }
    @Override
    public List<DiaryEntry> findEntriesByVocabulary (Vocabulary vocabularies){
        return diaryEntryRepository.findByVocabulariesContaining(vocabularies);
    }

    @Override
    public List<Vocabulary> getUserVocabulary(Long userId) {
        return diaryEntryRepository.findVocabulariesByUserId(userId);
    }






}
