package com.prepwise.repository;

import com.prepwise.entity.PasswordResetToken;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetToken p SET p.used = true WHERE p.user = :user AND p.used = false")
    void invalidateAllActiveTokensForUser(User user);

    void deleteByUser(User user);
}
