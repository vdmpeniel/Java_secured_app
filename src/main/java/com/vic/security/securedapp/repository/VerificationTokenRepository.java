package com.vic.security.securedapp.repository;

import com.vic.security.securedapp.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends
        JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
}
