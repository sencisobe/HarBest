package com.sencisobe.harbest.infrastructure.persistence;

import com.sencisobe.harbest.infrastructure.persistence.entity.HabitEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitJpaRepository extends JpaRepository<HabitEntity, Long> {
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
    Optional<HabitEntity> findByUserIdAndId(Long userId, Long id);
    List<HabitEntity> findAllByUserId(Long userId);
    void deleteByUserIdAndId(Long userId, Long id);
}