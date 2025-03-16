package com.fitnesstracker.fitnessworld;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.fitnessworld.constant.Role;
import com.fitnesstracker.fitnessworld.controllers.UserController;
import com.fitnesstracker.fitnessworld.entities.ActivityLog;
import com.fitnesstracker.fitnessworld.entities.Challenge;
import com.fitnesstracker.fitnessworld.entities.FitnessGoal;
import com.fitnesstracker.fitnessworld.entities.User;
import com.fitnesstracker.fitnessworld.repositories.ActivityLogRepository;
import com.fitnesstracker.fitnessworld.repositories.UserRepository;
import com.fitnesstracker.fitnessworld.services.ActivityService;
import com.fitnesstracker.fitnessworld.services.ChallengeService;
import com.fitnesstracker.fitnessworld.services.FitnessGoalService;
import com.fitnesstracker.fitnessworld.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mockito;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private FitnessGoalService fitnessGoalService;

    @MockBean
    private ChallengeService challengeService;

    @MockBean
    private ActivityService activityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Order(2)
    @Test
    void Annotation_testUserHasJPAAnnotations() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/entities/User.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("@Entity"), "User entity should contain @Entity annotation");
        assertTrue(entityFileContent.contains("@Id"), "User entity should contain @Id annotation");
        assertTrue(entityFileContent.contains("@GeneratedValue(strategy = GenerationType.IDENTITY)"), "User entity should contain @GeneratedValue annotation");
    }

    @Order(3)
    @Test
    void Annotation_testUserHasJsonIgnoreAnnotations() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/entities/User.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("@JsonIgnore"), "User entity should contain @JsonIgnore annotation");
    }

    @Order(4)
    @Test
    void Annotation_testUserHasEntityAnnotations() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/entities/User.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("@Entity"), "User entity should contain @Entity annotation");
    }

    @Order(5)
    @Test
    void Annotation_testFitnessGoalHasJsonBackReference() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/entities/FitnessGoal.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("@JsonBackReference"), "FitnessGoal entity should contain @JsonBackReference annotation");
    }

    @Order(24)
    @Test
    void Repository_testChallengeRepository() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/repositories/ChallengeRepository.java");
        assertTrue(Files.exists(entityFilePath), "ChallengeRepository file should exist");
    }

    @Order(25)
    @Test
    void Repository_testActivityLogRepositoryExists() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/repositories/ActivityLogRepository.java");
        assertTrue(Files.exists(entityFilePath), "ActivityLogRepository file should exist");
    }

    @Order(20)
    @Test
    void Repository_testActivityLogRepositoryMethods() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/repositories/ActivityLogRepository.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("List<ActivityLog> findByUserId(Long userId);"), "ActivityLogRepository should contain findByUserId method");
    }

    @Order(6)
    @Test
    void CRUD_testCreateUser() throws Exception {
        User user = new User(null, "testuser1", "testuser1@example.com", "password", Set.of(Role.USER));
        User savedUser = new User(1L, "testuser1", "testuser1@example.com", "password", Set.of(Role.USER));
        when(userService.registerUser(Mockito.any(User.class))).thenReturn(savedUser);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.username").value(savedUser.getUsername()))
                .andExpect(jsonPath("$.email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$.password").value(savedUser.getPassword()))
                .andExpect(jsonPath("$.roles").value("USER"));
        verify(userService, times(1)).registerUser(Mockito.any(User.class));
    }

    @Order(7)
    @Test
    void CRUD_testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User(1L, "testuser1", "testuser1@example.com", "password", Set.of(Role.USER)),
                new User(2L, "testuser2", "testuser2@example.com", "password", Set.of(Role.ADMIN))
        );
        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].username").value("testuser1"))
                .andExpect(jsonPath("$[1].username").value("testuser2"))
                .andExpect(jsonPath("$[0].email").value("testuser1@example.com"))
                .andExpect(jsonPath("$[1].email").value("testuser2@example.com"));
        verify(userService, times(1)).getAllUsers();
    }

    @Order(8)
    @Test
    void CRUD_testGetUsersPagination() throws Exception {
        List<User> users = Arrays.asList(
                new User(1L, "testuser1", "testuser1@example.com", "password", Set.of(Role.USER)),
                new User(2L, "testuser2", "testuser2@example.com", "password", Set.of(Role.ADMIN))
        );
        Page<User> page = new PageImpl<>(users);
        when(userService.getUsers(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/api/users/all")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(users.size()))
                .andExpect(jsonPath("$.content[0].username").value("testuser1"))
                .andExpect(jsonPath("$.content[1].username").value("testuser2"));
        verify(userService, times(1)).getUsers(any(Pageable.class));
    }

    @Order(9)
    @Test
    void CRUD_testGetUserByEmail() throws Exception {
        User user = new User(1L, "testuser1", "testuser1@example.com", "password", Set.of(Role.USER));
        when(userService.getUserByEmail("testuser1@example.com")).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/email/testuser1@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser1"))
                .andExpect(jsonPath("$.email").value("testuser1@example.com"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.roles").value("USER"));
        verify(userService, times(1)).getUserByEmail("testuser1@example.com");
    }

    @Order(10)
    @Test
    void CRUD_testUpdateUser() throws Exception {
        User updatedUser = new User(1L, "updateduser", "updateduser@example.com", "newpassword", Set.of(Role.ADMIN));
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updateduser@example.com"))
                .andExpect(jsonPath("$.password").value("newpassword"))
                .andExpect(jsonPath("$.roles").value("ADMIN"));
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Order(11)
    @Test
    void CRUD_testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Order(12)
    @Test
    void CRUD_testDeleteUserNotFound() throws Exception {
        doThrow(new RuntimeException("User not found with id 1")).when(userService).deleteUser(1L);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Order(13)
    @Test
    void CRUD_testGetUserWithRelationships() throws Exception {
        User user = new User(1L, "testuser1", "testuser1@example.com", "password", Set.of(Role.USER));
        ActivityLog log = new ActivityLog(1L, user, "exercise", 100.0, LocalDate.now());
        user.setActivitieslog(Arrays.asList(log));
        when(userService.getUserByEmail("testuser1@example.com")).thenReturn(Optional.of(user));
        MvcResult result = mockMvc.perform(get("/api/users/email/testuser1@example.com"))
                .andExpect(status().isOk()).andReturn();
        verify(userService, times(1)).getUserByEmail("testuser1@example.com");
    }

    @Order(14)
    @Test
    void CRUD_ateUserAndGoal() throws Exception {
        User user = new User(null, "testuser1", "testuser1@example.com", "password123", Set.of(Role.USER));
        User savedUser = new User(1L, "testuser1", "testuser1@example.com", "password123", Set.of(Role.USER));
        when(userService.registerUser(Mockito.any(User.class))).thenReturn(savedUser);
        String userJson = "{\n" +
                "\"username\": \"testuser1\",\n" +
                "\"email\": \"testuser1@example.com\",\n" +
                "\"password\": \"password123\",\n" +
                "\"roles\": [\"USER\"]\n" +
                "}";
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser1"))
                .andExpect(jsonPath("$.email").value("testuser1@example.com"));
        FitnessGoal goal = new FitnessGoal(null, savedUser, "weight loss", 10.0, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31));
        when(fitnessGoalService.createGoal(Mockito.any(FitnessGoal.class))).thenReturn(goal);
        String goalJson = "{\n" +
                "\"user\": {\n" +
                "    \"id\": 1,\n" +
                "    \"username\": \"testuser1\",\n" +
                "    \"email\": \"testuser1@example.com\",\n" +
                "    \"password\": \"password123\",\n" +
                "    \"roles\": [\"USER\"]\n" +
                "},\n" +
                "\"goalType\": \"weight loss\",\n" +
                "\"targetValue\": 10.0,\n" +
                "\"startDate\": \"2022-01-01\",\n" +
                "\"endDate\": \"2022-12-31\"\n" +
                "}";
        mockMvc.perform(post("/api/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(goalJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Goal created successfully!"))
                .andExpect(jsonPath("$.data.goalType").value("weight loss"))
                .andExpect(jsonPath("$.data.targetValue").value(10.0))
                .andExpect(jsonPath("$.data.startDate").value("2022-01-01"))
                .andExpect(jsonPath("$.data.endDate").value("2022-12-31"));
    }

    @Order(15)
    @Test
    void CRUD_testGetAllGoals() throws Exception {
        List<FitnessGoal> goals = Arrays.asList(
                new FitnessGoal(1L, new User(1L, "testuser", "testuser@example.com", "password", Set.of(Role.USER)), "Weight Loss", 10.0, LocalDate.now(), LocalDate.now().plusMonths(2)),
                new FitnessGoal(2L, new User(2L, "testuser2", "testuser2@example.com", "password", Set.of(Role.ADMIN)), "Muscle Gain", 5.0, LocalDate.now(), LocalDate.now().plusMonths(1))
        );
        Pageable pageable = PageRequest.of(0, 10, Sort.by("startDate").ascending());
        when(fitnessGoalService.getAllGoals("goalType", "asc", 0, 10)).thenReturn(goals);
        mockMvc.perform(get("/api/goals")
                .param("sortBy", "goalType")
                .param("order", "asc")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(goals.size()))
                .andExpect(jsonPath("$[0].goalType").value("Weight Loss"))
                .andExpect(jsonPath("$[1].goalType").value("Muscle Gain"));
        verify(fitnessGoalService, times(1)).getAllGoals("goalType", "asc", 0, 10);
    }

    @Order(16)
    @Test
    void CRUD_testGetGoalsByUserId() throws Exception {
        List<FitnessGoal> goals = Arrays.asList(
                new FitnessGoal(1L, new User(1L, "testuser", "testuser@example.com", "password", Set.of(Role.USER)), "Weight Loss", 10.0, LocalDate.now(), LocalDate.now().plusMonths(2)),
                new FitnessGoal(2L, new User(1L, "testuser", "testuser@example.com", "password", Set.of(Role.USER)), "Muscle Gain", 5.0, LocalDate.now(), LocalDate.now().plusMonths(1))
        );
        when(fitnessGoalService.getGoalsByUserId(1L, "Weight Loss", 0, 10)).thenReturn(goals);
        mockMvc.perform(get("/api/goals/user/{userId}", 1L)
                .param("goalType", "Weight Loss")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(goals.size()))
                .andExpect(jsonPath("$[0].goalType").value("Weight Loss"));
        verify(fitnessGoalService, times(1)).getGoalsByUserId(1L, "Weight Loss", 0, 10);
    }

    @Order(17)
    @Test
    void CRUD_testDeleteGoal() throws Exception {
        doNothing().when(fitnessGoalService).deleteGoal(1L);
        mockMvc.perform(delete("/api/goals/{id}", 1L))
                .andExpect(status().isNoContent());
        verify(fitnessGoalService, times(1)).deleteGoal(1L);
    }

    @Order(18)
    @Test
    void CRUD_testParticipateInChallenge() throws Exception {
        Long challengeId = 1L;
        Long userId = 1L;
        doNothing().when(challengeService).participateInChallenge(challengeId, userId);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/participate/{challengeId}", challengeId)
                .param("userId", String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully joined the challenge!"));
        verify(challengeService, times(1)).participateInChallenge(challengeId, userId);
    }

    @Order(19)
    @Test
    void CRUD_testGetChallengesByStartDate() throws Exception {
        LocalDate startDate = LocalDate.parse("2022-01-01");
        List<Challenge> challenges = Arrays.asList(
                new Challenge(1L, "Challenge 1", startDate, LocalDate.parse("2022-02-01"), "Reward1"),
                new Challenge(2L, "Challenge 2", startDate.plusDays(1), LocalDate.parse("2022-02-02"), "Reward2"));
        when(challengeService.getChallengesByStartDateAfter(startDate)).thenReturn(challenges);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/startDate/{startDate}", startDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(challenges.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].challengeName").value("Challenge 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].challengeName").value("Challenge 2"));
        verify(challengeService, times(1)).getChallengesByStartDateAfter(startDate);
    }

    @Order(22)
    @Test
    void Mapping_testUserHasOneToMany() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/entities/User.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("@OneToMany"), "User entity should contain @OneToMany annotation");
    }

    @Order(23)
    @Test
    void Mapping_testActivityLogHasManyToOne() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/entities/ActivityLog.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("@ManyToOne"), "ActivityLog entity should contain @ManyToOne annotation");
    }

    @Order(24)
    @Test
    void Mapping_testFitnessGoalHasManyToOne() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/entities/FitnessGoal.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("@ManyToOne"), "FitnessGoal entity should contain @ManyToOne annotation");
    }

    @Order(25)
    @Test
    @Transactional
    public void JPQL_testFindActivityLogsByUser() {
        User user1 = new User();
        user1.setUsername("testuser1_" + System.currentTimeMillis());
        user1.setEmail("testuser1_" + System.currentTimeMillis() + "@example.com");
        user1.setPassword("password");
        user1 = userRepository.save(user1);
        ActivityLog activityLog1 = new ActivityLog();
        activityLog1.setUser(user1);
        activityLog1.setActivityType("Workout");
        activityLog1.setValue(30.0);
        activityLog1.setLogDate(LocalDate.now());
        activityLogRepository.save(activityLog1);
        List<ActivityLog> result = activityLogRepository.findByUserId(user1.getId());
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @Order(26)
    @Test
    @Transactional
    public void JPQL_testFindActivityLogsByUserEmpty() {
        User user1 = new User();
        user1.setUsername("testuser1_" + System.currentTimeMillis());
        user1.setEmail("testuser1_" + System.currentTimeMillis() + "@example.com");
        user1.setPassword("password");
        user1 = userRepository.save(user1);
        List<ActivityLog> result = activityLogRepository.findByUserId(user1.getId());
        assertThat(result).isEmpty();
    }

    @Order(27)
    @Test
    void PaginateSorting_testPaginateUsersService() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/services/UserService.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("Pageable"), "UserService should use Pageable for pagination");
    }

    @Order(28)
    @Test
    void PaginateSorting_testPaginateGoalsService() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/services/FitnessGoalService.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("Pageable"), "FitnessGoalService should use Pageable for pagination");
    }

    @Order(29)
    @Test
    void PaginateSorting_testPaginateUsersServiceEmpty() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/services/UserService.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("Pageable"), "UserService should handle empty pagination results");
    }

    @Order(30)
    @Test
    public void LOG_testLogFolderAndFileCreation() {
        String LOG_FOLDER_PATH = "logs";
        String LOG_FILE_PATH = "logs/application.log";
        File logFolder = new File(LOG_FOLDER_PATH);
        assertTrue(logFolder.exists(), "Log folder should be created");
        File logFile = new File(LOG_FILE_PATH);
        assertTrue(logFile.exists(), "Log file should be created inside 'logs' folder");
    }

    @Order(31)
    @Test
    void AOP_testAOPConfigFile() {
        String filePath = "src/main/java/com/fitnesstracker/fitnessworld/aspect/UserLoggingAspect.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }

    @Order(32)
    @Test
    void AOP_testAOPConfigFileAspect() throws Exception {
        Path entityFilePath = Paths.get("src/main/java/com/fitnesstracker/fitnessworld/aspect/UserLoggingAspect.java");
        String entityFileContent = Files.readString(entityFilePath);
        assertTrue(entityFileContent.contains("Aspect"), "Book entity should contain @RequestBody annotation");
    }

    @Order(33)
    @Test
    public void SwaggerUI_testConfigurationFolder() {
        String directoryPath = "src/main/java/com/fitnesstracker/fitnessworld/config";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory());
    }

    @Order(34)
    @Test
    void SwaggerUI_testSwaggerConfigFile() {
        String filePath = "src/main/java/com/fitnesstracker/fitnessworld/config/SwaggerConfig.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile());
    }
}
