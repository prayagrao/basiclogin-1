package com.example.basiclogin.Repository;

import com.example.basiclogin.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // To find user by username during login
}

