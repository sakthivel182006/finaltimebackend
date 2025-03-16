package com.fitnesstracker.fitnessworld.controllers;

import com.fitnesstracker.fitnessworld.entities.FitnessGoal;
import com.fitnesstracker.fitnessworld.services.FitnessGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class FitnessController {

    @Autowired
    private FitnessGoalService fitnessGoalService;

    @PostMapping
    public ResponseEntity<FitnessGoal> createGoal(@RequestBody FitnessGoal fitnessGoal) {
    FitnessGoal createdGoal = fitnessGoalService.createGoal(fitnessGoal);
    return ResponseEntity.ok(createdGoal);
    }

    @GetMapping
    public ResponseEntity<List<FitnessGoal>> getAllGoals(
        @RequestParam(defaultValue = "goalType") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
                @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "10") int size) {
                    List<FitnessGoal> goals = fitnessGoalService.getAllGoals(sortBy, order, page, size);
                    return ResponseEntity.ok(goals);
                    }

                    @GetMapping("/user/{userId}")
                    public ResponseEntity<List<FitnessGoal>> getGoalsByUserId(
                            @PathVariable Long userId,
                                @RequestParam(required = false) String goalType,
                                    @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
                                        List<FitnessGoal> goals = fitnessGoalService.getGoalsByUserId(userId, goalType, page, size);
                                        return ResponseEntity.ok(goals);
                                        }

                                        @PutMapping("/{id}")
                                        public ResponseEntity<FitnessGoal> updateGoal(@PathVariable Long id, @RequestBody FitnessGoal fitnessGoal) {
                                        FitnessGoal updatedGoal = fitnessGoalService.updateGoal(id, fitnessGoal);
                                        return ResponseEntity.ok(updatedGoal);
                                        }

                                        @DeleteMapping("/{id}")
                                        public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
                                        fitnessGoalService.deleteGoal(id);
                                        return ResponseEntity.noContent().build();
                                        }
                                        }
                                        