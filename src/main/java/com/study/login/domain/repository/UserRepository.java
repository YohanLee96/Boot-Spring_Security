package com.study.login.domain.repository;

import com.study.login.domain.model.User;
import com.study.login.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);

    List<User> findAllByRole(UserRole userRole);

    boolean existsByUserId(String userId);
}
