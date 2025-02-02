package com.japanApply.journal.service;

import com.japanApply.journal.configuration.InUseExeption.LocationInUseException;
import com.japanApply.journal.configuration.InUseExeption.PhotoInUseException;
import com.japanApply.journal.model.Photo;
import com.japanApply.journal.model.User;
import com.japanApply.journal.repository.*;
import com.japanApply.journal.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final DiaryEntryRepository diaryEntryRepository;
    private final DiaryRepository diaryRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository, DiaryEntryRepository diaryEntryRepository, DiaryRepository diaryRepository) {
        this.photoRepository = photoRepository;
        this.diaryEntryRepository = diaryEntryRepository;
        this.diaryRepository = diaryRepository;
    }

    @Override
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    @Override
    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with ID: " + id));
    }

    @Override
    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public void deletePhoto(Long id) {
        boolean isInUse = diaryEntryRepository.existsByPhotoId(id) ||
                diaryRepository.existsByPhotoCoverId(id);

        if (isInUse) {
            throw new PhotoInUseException("Photo is currently in use and cannot be deleted.");
        }

        photoRepository.deleteById(id);
    }


    @Override
    public List<Photo> getPhotosByUserId(Long userId) {
        return photoRepository.findByUserId(userId);
    }

}
