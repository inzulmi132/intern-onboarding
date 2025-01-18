package com.sparta.internonboarding.user.repository;

import com.sparta.internonboarding.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
