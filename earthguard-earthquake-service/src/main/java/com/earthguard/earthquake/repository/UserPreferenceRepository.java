package com.earthguard.earthquake.repository;

import com.earthguard.common.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, String> {

    Optional<UserPreference> findByUserId(String userId);

    @Query("SELECT p FROM UserPreference p WHERE p.user.username = :username")
    Optional<UserPreference> findByUsername(String username);
}