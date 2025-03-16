package com.fitnesstracker.fitnessworld.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitnesstracker.fitnessworld.constant.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String username;
private String password;
private String email;

@ElementCollection
private Set<Role> roles;

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonIgnore
private List<ActivityLog> activitiesLog;

public User(Long id, String username, String email, String password, Set<Role> roles) {
this.id = id;
this.username = username;
this.email = email;
this.password = password;
this.roles = roles;
}

public void setActivitieslog(List<ActivityLog> activitiesLog) {
this.activitiesLog = activitiesLog;
}
}
