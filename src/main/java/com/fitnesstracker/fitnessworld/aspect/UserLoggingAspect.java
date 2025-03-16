package com.fitnesstracker.fitnessworld.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserLoggingAspect {

@Before("execution(* com.fitnesstracker.fitnessworld.controllers.UserController.*(..))")
public void logUserActivity() {
System.out.println("Logging user activity...");
}
}
