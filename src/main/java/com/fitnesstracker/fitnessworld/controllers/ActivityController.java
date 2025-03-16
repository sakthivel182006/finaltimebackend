package com.fitnesstracker.fitnessworld.controllers;

import com.fitnesstracker.fitnessworld.entities.ActivityLog;
import com.fitnesstracker.fitnessworld.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

@Autowired
private ActivityService activityService;

@PostMapping
public ResponseEntity<ActivityLog> createActivity(@RequestBody ActivityLog activityLog) {
ActivityLog createdActivity = activityService.createActivity(activityLog);
return ResponseEntity.ok(createdActivity);
}

@GetMapping
public ResponseEntity<List<ActivityLog>> getAllActivities() {
List<ActivityLog> activities = activityService.getAllActivities();
return ResponseEntity.ok(activities);
}

@GetMapping("/user/{userId}")
public ResponseEntity<List<ActivityLog>> getActivitiesByUserId(@PathVariable Long userId) {
List<ActivityLog> activities = activityService.getActivitiesByUserId(userId);
return ResponseEntity.ok(activities);
}

@PutMapping("/{id}")
public ResponseEntity<ActivityLog> updateActivity(@PathVariable Long id, @RequestBody ActivityLog activityLog) {
ActivityLog updatedActivity = activityService.updateActivity(id, activityLog);
return ResponseEntity.ok(updatedActivity);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
activityService.deleteActivity(id);
return ResponseEntity.noContent().build();
}
}
