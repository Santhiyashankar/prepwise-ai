package com.prepwise.repository;

import com.prepwise.entity.Resume;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserOrderByUploadedAtDesc(User user);

    Optional<Resume> findFirstByUserOrderByUploadedAtDesc(User user);

    Optional<Resume> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    void deleteByUser(User user);
}
