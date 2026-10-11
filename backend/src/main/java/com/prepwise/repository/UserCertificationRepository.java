package com.prepwise.repository;

import com.prepwise.entity.User;
import com.prepwise.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCertificationRepository extends JpaRepository<UserCertification, Long> {

    List<UserCertification> findByUser(User user);

    void deleteByUser(User user);
}
