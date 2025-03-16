package com.fitnesstracker.fitnessworld.services;

import com.fitnesstracker.fitnessworld.entities.FitnessGoal;
import com.fitnesstracker.fitnessworld.repositories.FitnessGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessGoalService {

@Autowired
private FitnessGoalRepository fitnessGoalRepository;

public FitnessGoal createGoal(FitnessGoal fitnessGoal) {
return fitnessGoalRepository.save(fitnessGoal);
}

public List<FitnessGoal> getAllGoals(String sortBy, String order, int page, int size) {
Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
return fitnessGoalRepository.findAll(PageRequest.of(page, size, sort)).getContent();
}

public List<FitnessGoal> getGoalsByUserId(Long userId, String goalType, int page, int size) {
if (goalType != null) {
return (List<FitnessGoal>) fitnessGoalRepository.findByUserIdAndGoalType(userId, goalType,
    PageRequest.of(page, size));
    }
    return (List<FitnessGoal>) fitnessGoalRepository.findByUserId(userId, PageRequest.of(page, size));
    }
    
    public FitnessGoal updateGoal(Long id, FitnessGoal fitnessGoal) {
    FitnessGoal existingGoal = fitnessGoalRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Fitness goal not found"));
    existingGoal.setGoalType(fitnessGoal.getGoalType());
    existingGoal.setTargetValue(fitnessGoal.getTargetValue());
    existingGoal.setStartDate(fitnessGoal.getStartDate());
    existingGoal.setEndDate(fitnessGoal.getEndDate());
    return fitnessGoalRepository.save(existingGoal);
    }
    
    public void deleteGoal(Long id) {
    fitnessGoalRepository.deleteById(id);
    }
    }