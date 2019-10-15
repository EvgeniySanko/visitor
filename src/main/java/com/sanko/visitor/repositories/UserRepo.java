package com.sanko.visitor.repositories;

import com.sanko.visitor.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Override
    Optional<User> findById(Long aLong);
}