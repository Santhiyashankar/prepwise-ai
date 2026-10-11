package com.prepwise.repository;

import com.prepwise.entity.Resume;
import com.prepwise.entity.ResumeHistory;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeHistoryRepository extends JpaRepository<ResumeHistory, Long> {

    List<ResumeHistory> findByUserOrderByRecordedAtAsc(User user);

    void deleteByResume(Resume resume);

    void deleteByUser(User user);
}
