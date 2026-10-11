package com.prepwise.repository;

import com.prepwise.entity.CompanyReadiness;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyReadinessRepository extends JpaRepository<CompanyReadiness, Long> {

    List<CompanyReadiness> findByUser(User user);

    void deleteByUser(User user);
}
