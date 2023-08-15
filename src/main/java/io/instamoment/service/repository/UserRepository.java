package io.instamoment.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.instamoment.service.entity.User;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndActiveTrue(String email);
    Optional<User> findUserByUserIdAndCodeAccessAndActiveTrue(Long userId, Integer codeAccess); 
}
