package com.japanApply.journal.service;

import com.japanApply.journal.model.Diary;
import com.japanApply.journal.model.User;
import com.japanApply.journal.repository.DiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Diary service implementation
 */
@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryServiceImpl(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Override
    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    @Override
    public List<Diary> getAllDiaries() {
        return diaryRepository.findAll();
    }

    @Override
    public Diary createDiary(Diary diary) {
        // Ajouter la logique métier ici si nécessaire
        return diaryRepository.save(diary);
    }

    @Override
    public Diary updateDiary(Diary diary) {
        // Vérifie si le journal existe avant de le mettre à jour
        if (diaryRepository.existsById(diary.getId())) {
            return diaryRepository.save(diary);
        } else {
            throw new IllegalArgumentException("Diary not found");
        }
    }

    @Override
    public void deleteDiaryById(Long id) {
        if (diaryRepository.existsById(id)) {
            diaryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Diary not found");
        }
    }

    @Override
    public List<Diary> findDiariesByUser(User user) {
        return diaryRepository.findByUser(user);
    }

    @Override
    public List<Diary> findPublicDiaries() {
        return diaryRepository.findByPublicDiaryTrue();
    }

    // Cette méthode dans le service devrait retourner un Optional<Diary>
    public Optional<Diary> findByIdAndUser(Long diaryId, User user) {
        return diaryRepository.findByIdAndUser(diaryId, user);
    }




}
