package com.sencisobe.harbest.infrastructure.persistence;

import com.sencisobe.harbest.infrastructure.persistence.entity.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitJpaRepository extends JpaRepository<HabitEntity, Long> {
}