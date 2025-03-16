package com.fitnesstracker.fitnessworld.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;

import javax.persistence.Id;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@Data
@NoArgsConstructor
public class Challenge {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String challengeName;
private LocalDate startDate;
private LocalDate endDate;
private String reward;

@OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<ChallengeParticipation> participants;

public Challenge(Long id, String challengeName, LocalDate startDate, LocalDate endDate, String reward) {
this.id = id;
this.challengeName = challengeName;
this.startDate = startDate;
this.endDate = endDate;
this.reward = reward;
}
}
