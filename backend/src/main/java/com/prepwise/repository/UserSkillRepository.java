package com.prepwise.repository;

import com.prepwise.entity.User;
import com.prepwise.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUser(User user);

    void deleteByUser(User user);
}
