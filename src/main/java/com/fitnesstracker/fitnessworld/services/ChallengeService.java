package com.fitnesstracker.fitnessworld.services;

import com.fitnesstracker.fitnessworld.entities.Challenge;
import com.fitnesstracker.fitnessworld.repositories.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChallengeService {

@Autowired
private ChallengeRepository challengeRepository;

public Challenge createChallenge(Challenge challenge) {
return challengeRepository.save(challenge);
}

public List<Challenge> getAllChallenges(String reward, String sortBy, String order) {
return challengeRepository.findAll();
}

public void participateInChallenge(Long challengeId, Long userId) {
}

public List<Challenge> getChallengesByStartDateAfter(LocalDate startDate) {
    return challengeRepository.findByStartDateAfter(startDate);
    }

    public Challenge updateChallenge(Long id, Challenge challenge) {
    Challenge existingChallenge = challengeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Challenge not found"));
        existingChallenge.setChallengeName(challenge.getChallengeName());
        existingChallenge.setStartDate(challenge.getStartDate());
        existingChallenge.setEndDate(challenge.getEndDate());
        existingChallenge.setReward(challenge.getReward());
        return challengeRepository.save(existingChallenge);
        }

        public void deleteChallenge(Long id) {
        challengeRepository.deleteById(id);
        }
        }
        