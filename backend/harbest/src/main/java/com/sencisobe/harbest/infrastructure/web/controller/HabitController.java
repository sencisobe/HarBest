// infrastructure/web/controller/HabitController.java
package com.sencisobe.harbest.infrastructure.web.controller;

import com.sencisobe.harbest.application.usecase.CreateHabit;
import com.sencisobe.harbest.application.usecase.ListHabits;
import com.sencisobe.harbest.application.usecase.RegisterWater;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.infrastructure.web.dto.CreateHabitRequest;
import com.sencisobe.harbest.infrastructure.web.dto.HabitResponse;
import com.sencisobe.harbest.infrastructure.web.dto.RegisterWaterRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {

    private final CreateHabit createHabit;
    private final RegisterWater registerWater;
    private final ListHabits listHabits;

    public HabitController(CreateHabit createHabit, RegisterWater registerWater, ListHabits listHabits) {
        this.createHabit = createHabit;
        this.registerWater = registerWater;
        this.listHabits = listHabits;
    }

    @PostMapping
    public HabitResponse create(@RequestBody CreateHabitRequest request) {
        Habit habit = createHabit.execute(request.getName(), request.getDailyObjectiveTime());
        return new HabitResponse(habit);
    }

    @PostMapping("/{id}/water")
    public HabitResponse water(@PathVariable Long id, @RequestBody RegisterWaterRequest request) {
        Water water = new Water(request.getDuration());
        Habit habit = registerWater.execute(id, water);
        return new HabitResponse(habit);
    }

    @GetMapping
    public List<HabitResponse> list() {
        return listHabits.execute().stream()
                .map(HabitResponse::new)
                .toList();
    }
}