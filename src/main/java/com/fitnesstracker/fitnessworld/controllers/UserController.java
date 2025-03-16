package com.fitnesstracker.fitnessworld.controllers;

import com.fitnesstracker.fitnessworld.entities.User;
import com.fitnesstracker.fitnessworld.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

@Autowired
private UserService userService;

@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
User savedUser = userService.registerUser(user);
return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
}

@GetMapping
public ResponseEntity<List<User>> getAllUsers() {
return ResponseEntity.ok(userService.getAllUsers());
}

@GetMapping("/all")
public ResponseEntity<Page<User>> getUsers(
@RequestParam(defaultValue = "0") int page,
@RequestParam(defaultValue = "10") int size) {
Pageable pageable = PageRequest.of(page, size);
Page<User> users = userService.getUsers(pageable);
return ResponseEntity.ok(users);
}

@GetMapping("/email/{email}")
public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
return ResponseEntity
    .ok(userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
    return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    try {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    }
    }
    