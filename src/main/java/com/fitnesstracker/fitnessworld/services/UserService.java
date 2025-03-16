package com.fitnesstracker.fitnessworld.services;

import com.fitnesstracker.fitnessworld.entities.User;
import com.fitnesstracker.fitnessworld.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

@Autowired
private UserRepository userRepository;

public User registerUser(User user) {
return userRepository.save(user);
}

public List<User> getAllUsers() {
return userRepository.findAll();
}

public Page<User> getUsers(Pageable pageable) {
return userRepository.findAll(pageable);
}

public Optional<User> getUserByEmail(String email) {
return userRepository.findByEmail(email);
}

public User updateUser(Long id, User userDetails) {
User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
user.setUsername(userDetails.getUsername());
user.setEmail(userDetails.getEmail());
return userRepository.save(user);
}

public void deleteUser(Long id) {
User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        userRepository.delete(user);
        }
        }
        