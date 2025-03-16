package com.fitnesstracker.fitnessworld.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class FitnessGoal {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String goalType;
private double targetValue;
private LocalDate startDate;
private LocalDate endDate;

@ManyToOne
@JoinColumn(name = "user_id", nullable = false)
@JsonBackReference
private User user;


public FitnessGoal(Long id, User user, String goalType, double targetValue, LocalDate startDate,
LocalDate endDate) {
this.id = id;
this.user = user;
this.goalType = goalType;
this.targetValue = targetValue;
this.startDate = startDate;
this.endDate = endDate;
}
}
