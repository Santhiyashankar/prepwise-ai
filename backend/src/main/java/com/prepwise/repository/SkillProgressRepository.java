package com.prepwise.repository;

import com.prepwise.entity.SkillProgress;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillProgressRepository extends JpaRepository<SkillProgress, Long> {

    List<SkillProgress> findByUser(User user);

    void deleteByUser(User user);
}
