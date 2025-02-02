package com.japanApply.journal.dataloader;

import com.japanApply.journal.model.RangType;
import com.japanApply.journal.model.User;
import com.japanApply.journal.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injecter PasswordEncoder

    @PostConstruct
    public void initDatabase() {
        List<User> users = List.of(
                new User("admin_user", "admin@example.com", "France", "123456789",
                        passwordEncoder.encode("password123"), LocalDate.now(), RangType.ADMINISTRATOR),
                new User("john_doe", "john@example.com", "USA", "987654321",
                        passwordEncoder.encode("password123"), LocalDate.now(), RangType.BASE),
                new User("jane_doe", "jane@example.com", "Canada", "123123123",
                        passwordEncoder.encode("password123"), LocalDate.now(), RangType.BASE),
                new User("alice", "alice@example.com", "UK", "321321321",
                        passwordEncoder.encode("password123"), LocalDate.now(), RangType.PRENIUM),
                new User("bob", "bob@example.com", "Germany", "111222333",
                        passwordEncoder.encode("password123"), LocalDate.now(), RangType.BASE),
                new User("charlie", "charlie@example.com", "Japan", "444555666",
                        passwordEncoder.encode("password123"), LocalDate.now(), RangType.BASE),
                new User("dave", "dave@example.com", "Australia", "777888999",
                        passwordEncoder.encode("password123"), LocalDate.now(), RangType.PRENIUM)
        );

        users.forEach(user -> userService.createUser(user));
    }
}
