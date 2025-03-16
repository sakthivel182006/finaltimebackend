package com.fitnesstracker.fitnessworld.services;

import com.fitnesstracker.fitnessworld.entities.ActivityLog;
import com.fitnesstracker.fitnessworld.repositories.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

@Autowired
private ActivityLogRepository activityLogRepository;

public ActivityLog createActivity(ActivityLog activityLog) {
return activityLogRepository.save(activityLog);
}

public List<ActivityLog> getAllActivities() {
return activityLogRepository.findAll();
}

public List<ActivityLog> getActivitiesByUserId(Long userId) {
return activityLogRepository.findByUserId(userId);
}

public Page<ActivityLog> getActivitiesByUserId(Long userId, int page, int size, String sortBy, String order) {
Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
PageRequest pageRequest = PageRequest.of(page, size, sort);
return activityLogRepository.findByUserId(userId, pageRequest);
}

public ActivityLog updateActivity(Long id, ActivityLog activityLog) {
ActivityLog existingActivity = activityLogRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Activity log not found with id: " + id));
        existingActivity.setActivityType(activityLog.getActivityType());
        existingActivity.setValue(activityLog.getValue());
        existingActivity.setLogDate(activityLog.getLogDate());
        return activityLogRepository.save(existingActivity);
        }

        public void deleteActivity(Long id) {
        activityLogRepository.deleteById(id);
    }
}