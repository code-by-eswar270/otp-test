package com.Project.lmsbackendtest.repository;

import com.Project.lmsbackendtest.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByEmailAndTypeOrderByCreatedAtDesc(String email, String type);
}
