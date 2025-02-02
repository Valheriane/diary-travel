package com.japanApply.journal.service;

import com.japanApply.journal.model.RangType;
import com.japanApply.journal.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
    List<User> getUsersByCountryOfOrigin(String countryOfOrigin);
    List<User> getUsersByRangType(RangType rangType);
    List<User> getUsersByCountryAndRang(String countryOfOrigin, RangType rangType);

    /**
     * Get user by username (email)
     * @param username the user's email
     * @return the user find by email
     */
    Optional<User> getByUsername(String username);
}
