package com.japanApply.journal.service;

import com.japanApply.journal.model.RangType;
import com.japanApply.journal.model.User;
import com.japanApply.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> createUser(User user) {
        user.setCreationDate(LocalDate.now());
        return Optional.of(this.userRepository.save(user));
    }


    @Override
    public User updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setCountryOfOrigin(user.getCountryOfOrigin());
            updatedUser.setPhone(user.getPhone());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setRangType(user.getRangType());
            return userRepository.save(updatedUser);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByCountryOfOrigin(String countryOfOrigin) {
        return userRepository.findByCountryOfOrigin(countryOfOrigin);
    }

    @Override
    public List<User> getUsersByRangType(RangType rangType) {
        return userRepository.findByRangType(rangType);
    }

    @Override
    public List<User> getUsersByCountryAndRang(String countryOfOrigin, RangType rangType) {
        return userRepository.findByCountryOfOriginAndRangType(countryOfOrigin, rangType);
    }

    @Override
    public Optional<User> getByUsername(String email) {
        // -- In our context, the username represent email
        // -- But in spring security username can be email, id, pseudo, phone number and so on...
        return Optional.ofNullable(this.userRepository.findUserByEmail(email));
    }



}
