// infrastructure/web/controller/HabitController.java
package com.sencisobe.harbest.infrastructure.web.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sencisobe.harbest.application.dto.DailyProgress;
import com.sencisobe.harbest.application.dto.WaterResult;
import com.sencisobe.harbest.application.usecase.CreateHabit;
import com.sencisobe.harbest.application.usecase.DeleteHabit;
import com.sencisobe.harbest.application.usecase.GetHabitById;
import com.sencisobe.harbest.application.usecase.GetWeeklyProgress;
import com.sencisobe.harbest.application.usecase.ListHabits;
import com.sencisobe.harbest.application.usecase.RegisterWater;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.infrastructure.web.dto.CreateHabitRequest;
import com.sencisobe.harbest.infrastructure.web.dto.HabitResponse;
import com.sencisobe.harbest.infrastructure.web.dto.RegisterWaterRequest;
import com.sencisobe.harbest.infrastructure.web.dto.WaterResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/habits")
public class HabitController {

    private final CreateHabit createHabit;
    private final RegisterWater registerWater;
    private final ListHabits listHabits;
    private final DeleteHabit deleteHabit;
    private final GetHabitById getHabitById;
    private final GetWeeklyProgress getWeeklyProgress;


    public HabitController(CreateHabit createHabit, RegisterWater registerWater, 
        ListHabits listHabits, DeleteHabit deleteHabit,GetHabitById getHabitById,
        GetWeeklyProgress getWeeklyProgress) {
        this.createHabit = createHabit;
        this.registerWater = registerWater;
        this.listHabits = listHabits;
        this.deleteHabit = deleteHabit;
        this.getHabitById = getHabitById;
        this.getWeeklyProgress = getWeeklyProgress;
    }

@PostMapping
public HabitResponse create(@AuthenticationPrincipal Long userId,
                            @Valid @RequestBody CreateHabitRequest request) {
    Habit habit = createHabit.execute(userId, request.getName(), request.getDailyObjectiveTime());
    return new HabitResponse(habit);
}

@PostMapping("/{id}/water")
public WaterResponse water(@AuthenticationPrincipal Long userId, @PathVariable Long id,
                           @Valid @RequestBody RegisterWaterRequest request) {
    Water water = new Water(request.getDuration());
    WaterResult result = registerWater.execute(userId, id, water);
    return new WaterResponse(result.getHabit(), result.isObjectiveMetToday(), result.getRemainingMinutesToday());
}

@GetMapping("/{id}")
public HabitResponse getById(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
    return new HabitResponse(getHabitById.execute(userId, id));
}

@GetMapping
public List<HabitResponse> list(@AuthenticationPrincipal Long userId) {
    return listHabits.execute(userId).stream().map(HabitResponse::new).toList();
}

@GetMapping("/{id}/weekly-progress")
public List<DailyProgress> weeklyProgress(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
    return getWeeklyProgress.execute(userId, id);
}

@DeleteMapping("/{id}")
public void delete(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
    deleteHabit.execute(userId, id);
}
}