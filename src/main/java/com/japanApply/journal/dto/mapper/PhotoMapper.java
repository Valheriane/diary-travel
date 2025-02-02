package com.japanApply.journal.dto.mapper;

import com.japanApply.journal.dto.PhotoDto;
import com.japanApply.journal.dto.PhotoResponseDto;
import com.japanApply.journal.model.Photo;
import com.japanApply.journal.model.User;
import com.japanApply.journal.repository.UserRepository;

public class PhotoMapper {

    public static Photo toPhoto(PhotoDto photoDto, UserRepository userRepository) {
        Photo photo = new Photo();
        photo.setUrl(photoDto.url());
        photo.setUploadedAt(photoDto.uploadedAt());

        User user = userRepository.findById(photoDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        photo.setUser(user);

        return photo;
    }

    public static PhotoDto toPhotoDto(Photo photo) {
        return new PhotoDto(
                photo.getUrl(),
                photo.getUploadedAt(),
                photo.getUser().getId()
        );
    }

    public static PhotoResponseDto toPhotoResponseDto(Photo photo){
        return new PhotoResponseDto(
                photo.getid(),
                photo.getUrl(),
                photo.getUploadedAt(),
                photo.getUser().getId()
        );
    }
}
