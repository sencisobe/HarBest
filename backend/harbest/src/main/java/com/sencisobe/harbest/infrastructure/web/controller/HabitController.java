// infrastructure/web/controller/HabitController.java
package com.sencisobe.harbest.infrastructure.web.controller;

import com.sencisobe.harbest.application.dto.DailyProgress;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{id}")
        public HabitResponse getById(@PathVariable Long id) {
        Habit habit = getHabitById.execute(id);
        return new HabitResponse(habit);
    }

    @GetMapping
    public List<HabitResponse> list() {
        return listHabits.execute().stream()
                .map(HabitResponse::new)
                .toList();
    }
    @GetMapping("/{id}/weekly-progress")
    public List<DailyProgress> weeklyProgress(@PathVariable Long id) {
        return getWeeklyProgress.execute(id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteHabit.execute(id);
}
}