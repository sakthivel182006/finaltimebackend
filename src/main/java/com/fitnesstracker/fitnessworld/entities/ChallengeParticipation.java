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
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeParticipation {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String status;

@ManyToOne
@JoinColumn(name = "user_id", nullable = false)
@JsonBackReference
private User user;

@ManyToOne
@JoinColumn(name = "challenge_id", nullable = false)
@JsonBackReference
private Challenge challenge;
}
