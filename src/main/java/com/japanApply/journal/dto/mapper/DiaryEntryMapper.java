package com.japanApply.journal.dto.mapper;

import com.japanApply.journal.dto.DiaryEntryDto;
import com.japanApply.journal.dto.DiaryEntryResponseDto;
import com.japanApply.journal.model.*;
import com.japanApply.journal.repository.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DiaryEntryMapper {

    public static DiaryEntry toDiaryEntry(DiaryEntryDto diaryEntryDto, UserRepository userRepository, DiaryRepository diaryRepository, LocationRepository locationRepository, VocabularyRepository vocabularyRepository, PhotoRepository photoRepository) {
        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setTitle(diaryEntryDto.titleDiaryEntry());
        diaryEntry.setNote(diaryEntryDto.noteDiaryEntry());
        diaryEntry.setDateCreated(diaryEntryDto.dateCreatedDiaryEntry());
        diaryEntry.setDescription(diaryEntryDto.descriptionDiaryEntry());

        User user = userRepository.findById(diaryEntryDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        diaryEntry.setUser(user);

        Diary diary = diaryRepository.findById(diaryEntryDto.diaryId())
                .orElseThrow(() -> new IllegalArgumentException("Diary not found"));
        diaryEntry.setDiary(diary);

        Location location = locationRepository.findById(diaryEntryDto.locationId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        diaryEntry.setLocation(location);

        Set<Vocabulary> vocabularies = new HashSet<>();
        for (Long vocabularyId : diaryEntryDto.vocabularyIds()) {
            // Change variable name here
            Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                    .orElseThrow(() -> new IllegalArgumentException("Vocabulary not found for ID: " + vocabularyId));
            vocabularies.add(vocabulary);
        }

        // Assign the Set to the diary entry
        diaryEntry.setVocabularies(vocabularies);

        Set<Photo> photos = new HashSet<>();
        for (Long photoId : diaryEntryDto.photoIds()) {
            // Change variable name here
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new IllegalArgumentException("Photo not found for ID: " + photoId));
            photos.add(photo);
        }
        // Assign the Set to the diary entry
        diaryEntry.setPhotos(photos);


        return diaryEntry;
    }


    public static DiaryEntryResponseDto toDiaryEntryResponseDto(DiaryEntry diaryEntry) {
        return new DiaryEntryResponseDto(
                diaryEntry.getId(),
                diaryEntry.getTitle(),
                diaryEntry.getNote(),
                diaryEntry.getDateCreated(),
                diaryEntry.getDescription(),
                diaryEntry.getUser().getId(),  // Added user ID
                diaryEntry.getUser().getUsername(),
                diaryEntry.getDiary().getId(),  // Added diary ID
                diaryEntry.getDiary().getNameDiary(),
                diaryEntry.getLocation().getId(),  // Added location ID
                diaryEntry.getLocation().getLocationName(),
                diaryEntry.getVocabularies().stream()
                        .map(Vocabulary::getExpression)
                        .collect(Collectors.toSet()),
                diaryEntry.getPhotos().stream()
                        .map(Photo::getUrl)
                        .collect(Collectors.toSet())
        );
    }

}
