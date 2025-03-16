package com.fitnesstracker.fitnessworld.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
public class ActivityLog {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String activityType;
private double value;
private LocalDate logDate;

@ManyToOne
@JoinColumn(name = "user_id", nullable = false)
@JsonBackReference
private User user;

public ActivityLog(Long id, User user, String activityType, double value, LocalDate logDate) {
    this.id = id;
    this.user = user;
    this.activityType = activityType;
    this.value = value;
    this.logDate = logDate;
    }
    }
    