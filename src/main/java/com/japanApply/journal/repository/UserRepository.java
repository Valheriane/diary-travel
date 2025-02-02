package com.japanApply.journal.repository;

import com.japanApply.journal.model.RangType;
import com.japanApply.journal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByCountryOfOrigin(String countryOfOrigin);
    List<User> findByRangType(RangType rangType);
    List<User> findByCountryOfOriginAndRangType(String countryOfOrigin, RangType rangType);
    User findUserByEmail(String email);
}
