package com.sanko.visitor.repositories;

import com.sanko.visitor.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
