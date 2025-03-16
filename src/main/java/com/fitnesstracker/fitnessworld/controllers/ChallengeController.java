package com.fitnesstracker.fitnessworld.controllers;

import com.fitnesstracker.fitnessworld.entities.Challenge;
import com.fitnesstracker.fitnessworld.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<Challenge> createChallenge(@RequestBody Challenge challenge) {
    Challenge createdChallenge = challengeService.createChallenge(challenge);
    return ResponseEntity.ok(createdChallenge);
    }

    @GetMapping
    public ResponseEntity<List<Challenge>> getAllChallenges(
    @RequestParam(required = false) String reward,
    @RequestParam(defaultValue = "startDate") String sortBy,
    @RequestParam(defaultValue = "asc") String order) {
    List<Challenge> challenges = challengeService.getAllChallenges(reward, sortBy, order);
    return ResponseEntity.ok(challenges);
    }

    @PostMapping("/participate/{challengeId}")
    public ResponseEntity<String> participateInChallenge(
    @PathVariable Long challengeId,
    @RequestParam Long userId) {
    challengeService.participateInChallenge(challengeId, userId);
    return ResponseEntity.ok("Successfully joined the challenge!");
    }

    @GetMapping("/startDate/{startDate}")
    public ResponseEntity<List<Challenge>> getChallengesByStartDate(@PathVariable LocalDate startDate) {
    List<Challenge> challenges = challengeService.getChallengesByStartDateAfter(startDate);
    return ResponseEntity.ok(challenges);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Challenge> updateChallenge(@PathVariable Long id, @RequestBody Challenge challenge) {
    Challenge updatedChallenge = challengeService.updateChallenge(id, challenge);
    return ResponseEntity.ok(updatedChallenge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
    challengeService.deleteChallenge(id);
    return ResponseEntity.noContent().build();
    }
    }
    