package com.aipoweredplacementassistant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aipoweredplacementassistant.entity.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByUserEmail(String email);
}