// infrastructure/persistence/HabitRepositoryImpl.java
package com.sencisobe.harbest.infrastructure.persistence;

import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.repository.HabitRepository;
import com.sencisobe.harbest.infrastructure.persistence.entity.HabitEntity;
import com.sencisobe.harbest.infrastructure.persistence.entity.WaterEntity;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HabitRepositoryImpl implements HabitRepository {

    private final HabitJpaRepository jpaRepository;

    public HabitRepositoryImpl(HabitJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Habit save(Habit habit) {
        HabitEntity entity = toEntity(habit);
        HabitEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

@Override
public Optional<Habit> findByUserIdAndId(Long userId, Long id) {
    return jpaRepository.findByUserIdAndId(userId, id).map(this::toDomain);
}


@Override
public List<Habit> findAllByUserId(Long userId) {
    return jpaRepository.findAllByUserId(userId).stream().map(this::toDomain).toList();
}

@Override
@Transactional
public void deleteByUserIdAndId(Long userId, Long id) {
    jpaRepository.deleteByUserIdAndId(userId, id);
}

@Override
public boolean existsByUserIdAndName(Long userId, String name) {
    return jpaRepository.existsByUserIdAndNameIgnoreCase(userId, name);
}

    private HabitEntity toEntity(Habit habit) {
        HabitEntity entity = new HabitEntity(
                habit.getUserId(),
                habit.getName(),
                habit.getDailyObjectiveTime(),
                habit.getGrowthStage(),
                habit.getStreak(),
                habit.getTotalExperience(),
                habit.getCreationDate()
        );
        entity.setId(habit.getId());

        List<WaterEntity> waterEntities = habit.getWaterHistory().stream()
                .map(w -> new WaterEntity(w.getDate(), w.getDuration(), entity))
                .toList();
        entity.getWaterHistory().addAll(waterEntities);

        return entity;
    }
    private Habit toDomain(HabitEntity entity) {
        List<Water> waters = entity.getWaterHistory().stream()
                .map(w -> new Water(w.getDate(), w.getDuration()))
                .toList();

        return new Habit(
                entity.getUserId(),
                entity.getId(),
                entity.getName(),
                entity.getDailyObjectiveTime(),
                entity.getGrowthStage(),
                entity.getStreak(),
                entity.getTotalExperience(),
                waters,
                entity.getCreationDate()
        );
    }
   
}