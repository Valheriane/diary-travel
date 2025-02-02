package com.japanApply.journal.dto.mapper;

import com.japanApply.journal.dto.DiaryDto;
import com.japanApply.journal.dto.DiaryResponseDto;
import com.japanApply.journal.model.*;
import com.japanApply.journal.repository.*;

public class DiaryMapper {

    public static Diary toDiary(DiaryDto diaryDto, UserRepository userRepository, PhotoRepository photoRepository, CountryRepository countryRepository) {
        Diary diary = new Diary();
        diary.setNameDiary(diaryDto.nameDiary());
        diary.setDateCreateDiary(diaryDto.dateCreateDiary());
        diary.setDateStartDiary(diaryDto.dateStartDiary());
        diary.setDateEndDiary(diaryDto.dateEndDiary());
        diary.setDescriptionDiary(diaryDto.descriptionDiary());
        diary.setPublicDiary(diaryDto.publicDiary());

        // Retrieve the existing user and associate it with the Diary
        User user = userRepository.findById(diaryDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        diary.setUser(user);

        // Associate the photo
        Photo photo = photoRepository.findById(diaryDto.photoCoverId())
                .orElseThrow(() -> new IllegalArgumentException("Photo not found"));
        diary.setPhotoCover(photo);

        // Associate the country
        Country country = countryRepository.findById(diaryDto.countryId())
                .orElseThrow(() -> new IllegalArgumentException("Country not found"));
        diary.setCountry(country);

        return diary;
    }

    public static DiaryResponseDto toDiaryResponseDto(Diary diary){
        return new DiaryResponseDto(
                diary.getId(),
                diary.getNameDiary(),
                diary.getUser().getId(),
                diary.getPhotoCover().getid(),
                diary.getCountry().getId(),
                diary.getDateCreateDiary(),
                diary.getDateStartDiary(),
                diary.getDateEndDiary(),
                diary.getDescriptionDiary(),
                diary.getPublicDiary()
        );
    }

}
