package com.japanApply.journal.service;

import com.japanApply.journal.model.Photo;
import com.japanApply.journal.model.User;

import java.util.List;

public interface PhotoService {
    List<Photo> getAllPhotos(); // Obtenir toutes les photos
    Photo getPhotoById(Long id); // Trouver une photo par ID
    Photo savePhoto(Photo photo); // Sauvegarder une photo
    void deletePhoto(Long id); // Supprimer une photo
    List<Photo> getPhotosByUserId(Long userId); // Trouver les photos par user_id
}
