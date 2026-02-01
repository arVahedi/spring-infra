# Domain Service Architecture Pattern - Developer Guide

**Version:** 1.0  
**Last Updated:** 2026  
**Audience:** Developers, Code Agents, Team Members

---

## 📋 Table of Contents

1. [Overview](#overview)
2. [Core Principles](#core-principles)
3. [Layer Responsibilities](#layer-responsibilities)
4. [Implementation Guidelines](#implementation-guidelines)
5. [Code Examples](#code-examples)
6. [Common Mistakes](#common-mistakes)
7. [Testing Strategy](#testing-strategy)
8. [Decision Trees](#decision-trees)
9. [Best Practices](#best-practices)
10. [FAQ](#faq)

---

## Overview

### What is the Domain Service Pattern?

The Domain Service Pattern is a pragmatic architectural approach that separates business logic from infrastructure concerns while avoiding over-engineering. It sits between simple service layers (anemic domain) and full hexagonal architecture.

### When to Use This Pattern

✅ **Use this pattern when:**
- Building Spring Boot microservices with moderate business logic
- You need testable business logic without infrastructure dependencies
- Your team values maintainability over architectural purity
- You want clear separation of concerns without excessive abstraction
- You're using JPA/Hibernate for persistence

❌ **Don't use this pattern when:**
- Building simple CRUD APIs with no business logic
- You need framework independence (use full hexagonal)
- Domain logic is extremely complex (use full DDD)
- You need multiple persistence strategies simultaneously

### Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     HTTP Layer (Controllers)                │
│  - Handles HTTP requests/responses                          │
│  - Input validation (DTO level)                             │
│  - Delegates to application services                        │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              Application Service Layer                      │
│  - Orchestrates use cases                                   │
│  - Manages transactions                                     │
│  - Handles infrastructure concerns (DB, uniqueness)         │
│  - Maps DTOs ↔ Entities                                     │
│  - Coordinates domain services                              │
└──────────┬─────────────────────────┬────────────────────────┘
           │                         │
           ▼                         ▼
┌──────────────────────┐   ┌─────────────────────────────────┐
│  Domain Service      │   │  Repository Layer               │
│  - Pure business     │   │  - Spring Data JPA              │
│    logic             │   │  - Database queries             │
│  - NO infrastructure │   │  - Entity persistence           │
│  - Operates on       │   │                                 │
│    objects in memory │   │                                 │
└──────────────────────┘   └────────────┬────────────────────┘
                                        │
                                        ▼
                         ┌──────────────────────────────────┐
                         │  Entity Layer (JPA)              │
                         │  - Data structure                │
                         │  - JPA annotations               │
                         │  - Basic validation              │
                         │  - Helper methods                │
                         └────────────┬─────────────────────┘
                                      │
                                      ▼
                         ┌──────────────────────────────────┐
                         │         Database                 │
                         └──────────────────────────────────┘
```

---

## Core Principles

### 1. **Separation of Concerns**

Each layer has a single, well-defined responsibility:

| Layer | Responsibility | Dependencies |
|-------|---------------|--------------|
| **Entity** | Data structure + basic validation | JPA only |
| **Domain Service** | Business logic on objects | Entities only |
| **Application Service** | Orchestration + infrastructure | Domain Services + Repositories |
| **Repository** | Data access | JPA, Database |
| **Controller** | HTTP handling | Application Services + DTOs |

### 2. **Dependency Rule**

**Critical:** Dependencies flow in ONE direction only:

```
Controller → Application Service → Domain Service
                ↓                       ↑
           Repository              Entities
```

**Never:**
- Domain Service → Repository ❌
- Entity → Domain Service ❌
- Domain Service → Application Service ❌

### 3. **Domain Services Have NO Infrastructure Dependencies**

This is the **most important principle**:

```java
// ❌ WRONG - Domain service with infrastructure dependency
@Component
public class UserDomainService {
    private final UserRepository repository; // ❌ NO!
}

// ✅ CORRECT - Domain service with no dependencies
@Component
public class UserDomainService {
    // No repositories, no external dependencies
    // Only methods that operate on objects passed to them
}
```

**Why?** Domain services must be testable without any infrastructure (no database, no Spring context).

### 4. **Application Services Handle All Infrastructure**

```java
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;        // ✅ OK
    private final OrganizationRepository orgRepository; // ✅ OK
    private final UserDomainService userDomainService;  // ✅ OK
    
    // Handles DB access, uniqueness checks, transaction management
}
```

---

## Layer Responsibilities

### Entity Layer

**Purpose:** Define data structure and persistence mapping

**Responsibilities:**
- ✅ JPA annotations (`@Entity`, `@Column`, `@ManyToOne`, etc.)
- ✅ Simple getter/setter methods
- ✅ Helper methods (e.g., `getFullName()`)
- ✅ Lifecycle callbacks (`@PrePersist`, `@PreUpdate`)

**Anti-Patterns:**
- ❌ Complex business logic
- ❌ Cross-entity or simple validation
- ❌ External dependencies (repositories, services)
- ❌ State transition logic

**Example:**

```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    // ✅ Helper method
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
```

---

### Domain Service Layer

**Purpose:** Encapsulate pure business logic that operates on domain objects

**Critical Rule:** **NO REPOSITORY DEPENDENCIES**

**Responsibilities:**
- ✅ Business rules and validations (on objects in memory)
- ✅ State transitions
- ✅ Complex calculations
- ✅ Business policy enforcement
- ✅ Format validation

**Anti-Patterns:**
- ❌ Database queries
- ❌ Uniqueness checks (requires DB)
- ❌ Loading entities by ID
- ❌ Transaction management
- ❌ DTO mapping

**Example:**

```java
@Component
public class UserDomainService {
    
    // ✅ NO repository dependencies
    
    /**
     * Activates a user
     * Business rule: Cannot activate an already active user
     * 
     * @param user The user object to activate (already loaded in memory)
     */
    public void activateUser(User user) {
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        user.setStatus(UserStatus.ACTIVE);
    }
    
    /**
     * Validates user can be deleted
     * Business rule: Can only delete inactive users
     * 
     * @param user The user object to validate
     */
    public void validateUserDeletion(User user) {
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException(
                "Cannot delete active user. Deactivate first."
            );
        }
    }
    
    /**
     * Validates user data completeness and format
     * Pure business logic - no database access
     * 
     * @param user The user object to validate
     */
    public void validateUserData(User user) {
        
        // Additional business validations
        if (user.getPhoneNumber() != null) {
            if (!user.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
                throw new IllegalArgumentException(
                    "Invalid phone number format"
                );
            }
        }
        
        if (user.getEmail() != null) {
            if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException(
                    "Invalid email format"
                );
            }
        }
    }
    
    /**
     * Checks if user is eligible for promotion
     * Business calculation based on user state
     * 
     * @param user The user to check
     * @return true if eligible
     */
    public boolean isEligibleForPromotion(User user) {
        return user.getStatus() == UserStatus.ACTIVE 
            && user.getCreatedAt().isBefore(
                LocalDateTime.now().minusYears(1)
            );
    }
}
```

**Key Characteristics:**
1. Methods receive domain objects as parameters
2. No database access
3. Pure business logic
4. Easily testable without infrastructure

---

### Application Service Layer

**Purpose:** Orchestrate use cases, manage infrastructure, coordinate domain services

**Responsibilities:**
- ✅ Load entities from database
- ✅ Uniqueness validation (requires DB queries)
- ✅ Transaction management (`@Transactional`)
- ✅ Coordinate multiple domain services
- ✅ Map DTOs ↔ Entities
- ✅ Handle exceptions
- ✅ Orchestrate complex workflows

**Anti-Patterns:**
- ❌ Business logic (delegate to domain services)
- ❌ HTTP concerns (belongs in controllers)
- ❌ Direct entity manipulation without domain service validation

**Example:**

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    // ✅ Infrastructure dependencies
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    
    // ✅ Domain service dependency
    private final UserDomainService userDomainService;
    
    // ✅ Mapper
    private final UserMapper userMapper;
    
    /**
     * Creates a new user
     * Orchestrates the entire use case
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // 1. ✅ APPLICATION: Validate uniqueness (infrastructure)
        validateUniqueEmail(request.email());
        
        // 2. ✅ APPLICATION: Load dependencies from database
        Organization organization = organizationRepository
            .findById(request.organizationId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Organization not found"
            ));
        
        // 3. ✅ APPLICATION: Create entity and set relationships
        User user = userMapper.toEntity(request);
        user.setOrganization(organization);
        
        // 4. ✅ DOMAIN: Validate business rules
        userDomainService.validateUserData(user);
        
        // 5. ✅ APPLICATION: Persist to database
        User savedUser = userRepository.save(user);
        
        // 6. ✅ APPLICATION: Map to response DTO
        return userMapper.toResponse(savedUser);
    }
    
    /**
     * Changes user status
     */
    @Transactional
    public UserResponse changeUserStatus(UUID id, UserStatus newStatus) {
        // 1. ✅ APPLICATION: Load from database
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found"
            ));
        
        // 2. ✅ DOMAIN: Handle state transition (business logic)
        switch (newStatus) {
            case ACTIVE -> userDomainService.activateUser(user);
            case INACTIVE -> userDomainService.deactivateUser(user);
            case SUSPENDED -> userDomainService.suspendUser(user);
        }
        
        // 3. ✅ APPLICATION: Persist changes
        User updated = userRepository.save(user);
        
        return userMapper.toResponse(updated);
    }
    
    /**
     * Deletes a user
     */
    @Transactional
    public void deleteUser(UUID id) {
        // ✅ APPLICATION: Load entity
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found"
            ));
        
        // ✅ DOMAIN: Validate business rules for deletion
        userDomainService.validateUserDeletion(user);
        
        // ✅ APPLICATION: Delete from database
        userRepository.delete(user);
    }
    
    // ========== Private Helper Methods ==========
    
    /**
     * ✅ APPLICATION SERVICE responsibility
     * Uniqueness check requires database access
     */
    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                "Email already exists: " + email
            );
        }
    }
}
```

**Key Characteristics:**
1. Has repository dependencies
2. Manages transactions
3. Delegates business logic to domain services
4. Handles infrastructure concerns
5. Orchestrates complex workflows

---

### Repository Layer

**Purpose:** Abstract database access using Spring Data JPA

**Responsibilities:**
- ✅ CRUD operations
- ✅ Custom queries
- ✅ Fetch strategies (LAZY, EAGER)
- ✅ Query optimization

**Example:**

```java
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.organization WHERE u.id = :id")
    Optional<User> findByIdWithOrganization(@Param("id") UUID id);
    
    List<User> findByOrganizationId(UUID organizationId);
    
    List<User> findByStatus(UserStatus status);
}
```

---

### Controller Layer

**Purpose:** Handle HTTP requests and responses

**Responsibilities:**
- ✅ HTTP request/response handling
- ✅ DTO validation (`@Valid`)
- ✅ HTTP status codes
- ✅ Delegate to application services
- ✅ Exception handling (via `@RestControllerAdvice`)

**Anti-Patterns:**
- ❌ Business logic
- ❌ Direct repository access
- ❌ Transaction management

**Example:**

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeStatusRequest request) {
        UserResponse updated = userService.changeUserStatus(
            id, 
            request.status()
        );
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## Implementation Guidelines

### Guideline 1: Identify Business Logic vs Infrastructure

**Ask yourself:** "Does this require database access?"

**If YES → Application Service**
```java
// Checking uniqueness = requires DB query
private void validateUniqueEmail(String email) {
    if (userRepository.existsByEmail(email)) {
        throw new IllegalArgumentException("Email exists");
    }
}
```

**If NO → Domain Service**
```java
// Format validation = pure logic
public void validateEmailFormat(String email) {
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        throw new IllegalArgumentException("Invalid email");
    }
}
```

### Guideline 2: Domain Services Receive Objects, Not IDs

**❌ WRONG:**
```java
public void activateUser(UUID userId) {
    User user = repository.findById(userId).orElseThrow(); // ❌
    user.setStatus(ACTIVE);
}
```

**✅ CORRECT:**
```java
public void activateUser(User user) {
    if (user.getStatus() == ACTIVE) {
        throw new IllegalStateException("Already active");
    }
    user.setStatus(ACTIVE);
}
```

### Guideline 3: Application Services Orchestrate

Application services should follow this pattern:

```java
@Transactional
public ResponseDTO performUseCase(RequestDTO request) {
    // 1. Validate infrastructure concerns (uniqueness, existence)
    validateInfrastructure(request);
    
    // 2. Load required entities from database
    Entity entity = loadEntities(request);
    
    // 3. Delegate business logic to domain service
    domainService.applyBusinessRules(entity);
    
    // 4. Persist changes
    Entity saved = repository.save(entity);
    
    // 5. Map to response DTO
    return mapper.toResponse(saved);
}
```

### Guideline 4: Use Private Methods for Infrastructure Concerns

Keep application service clean by extracting infrastructure concerns:

```java
@Service
public class UserService {
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        validateUniqueEmail(request.email());      // ✅ Private method
        Organization org = loadOrganization(request.orgId()); // ✅ Private
        // ...
    }
    
    // ✅ Infrastructure concern - private helper
    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email exists");
        }
    }
    
    // ✅ Database access - private helper
    private Organization loadOrganization(UUID id) {
        return organizationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Org not found"));
    }
}
```

### Guideline 5: Keep Entities Simple

Entities should be primarily data structures:

```java
@Entity
public class User {
    // ✅ Fields with JPA annotations
    // ✅ Simple helper methods (getFullName())
    
    // ❌ NO complex business logic
    // ❌ NO repository dependencies
    // ❌ NO state transition logic
    // ❌ Basic validation in validate() method
}
```

---

## Code Examples

### Complete Example: User Activation Flow

**1. Controller (HTTP Layer)**

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activateUser(@PathVariable UUID id) {
        UserResponse activated = userService.activateUser(id);
        return ResponseEntity.ok(activated);
    }
}
```

**2. Application Service (Orchestration + Infrastructure)**

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final UserMapper userMapper;
    
    @Transactional
    public UserResponse activateUser(UUID id) {
        // 1. ✅ Load from database (infrastructure)
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found: " + id
            ));
        
        // 2. ✅ Delegate to domain service (business logic)
        userDomainService.activateUser(user);
        
        // 3. ✅ Save to database (infrastructure)
        User updated = userRepository.save(user);
        
        // 4. ✅ Map to DTO (infrastructure)
        return userMapper.toResponse(updated);
    }
}
```

**3. Domain Service (Pure Business Logic)**

```java
@Component
public class UserDomainService {
    
    /**
     * Activates a user
     * Business rule: User must not already be active
     */
    public void activateUser(User user) {
        // ✅ Pure business logic - no infrastructure
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException(
                "User is already active"
            );
        }
        
        // State transition
        user.setStatus(UserStatus.ACTIVE);
    }
}
```

**4. Entity (Data Structure)**

```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;
    
    @Column(nullable = false)
    private String email;
    
    // Getters, setters
}
```

---

### Complete Example: User Creation Flow

**Request DTO:**

```java
public record CreateUserRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    @NotNull UUID organizationId
) {}
```

**Controller:**

```java
@PostMapping
public ResponseEntity<UserResponse> createUser(
        @Valid @RequestBody CreateUserRequest request) {
    UserResponse created = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

**Application Service:**

```java
@Transactional
public UserResponse createUser(CreateUserRequest request) {
    // 1. Infrastructure: Check email uniqueness
    validateUniqueEmail(request.email());
    
    // 2. Infrastructure: Load organization
    Organization org = organizationRepository.findById(request.organizationId())
        .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
    
    // 3. Infrastructure: Create entity
    User user = new User();
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEmail(request.email());
    user.setOrganization(org);
    user.setStatus(UserStatus.PENDING);
    
    // 4. Domain: Validate business rules
    userDomainService.validateUserData(user);
    
    // 5. Infrastructure: Persist
    User saved = userRepository.save(user);
    
    // 6. Infrastructure: Map to DTO
    return userMapper.toResponse(saved);
}

private void validateUniqueEmail(String email) {
    if (userRepository.existsByEmail(email)) {
        throw new IllegalArgumentException("Email already exists");
    }
}
```

**Domain Service:**

```java
public void validateUserData(User user) {
    // Simple validation
    // ...
    
    // Business rules
    if (user.getEmail() != null && 
        !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        throw new IllegalArgumentException("Invalid email format");
    }
    
    if (user.getFirstName() != null && user.getFirstName().length() < 2) {
        throw new IllegalArgumentException("First name too short");
    }
}
```

---

## Common Mistakes

### Mistake 1: Domain Service with Repository Dependency

**❌ WRONG:**

```java
@Component
public class UserDomainService {
    private final UserRepository userRepository; // ❌ NO!
    
    public void activateUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(ACTIVE);
    }
}
```

**Why wrong?**
- Domain service depends on infrastructure
- Can't test without database
- Violates separation of concerns

**✅ CORRECT:**

```java
@Component
public class UserDomainService {
    // No repository dependency
    
    public void activateUser(User user) {
        if (user.getStatus() == ACTIVE) {
            throw new IllegalStateException("Already active");
        }
        user.setStatus(ACTIVE);
    }
}
```

---

### Mistake 2: Business Logic in Application Service

**❌ WRONG:**

```java
@Service
public class UserService {
    
    @Transactional
    public void activateUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        
        // ❌ Business logic in application service
        if (user.getStatus() == ACTIVE) {
            throw new IllegalStateException("Already active");
        }
        user.setStatus(ACTIVE);
        
        userRepository.save(user);
    }
}
```

**Why wrong?**
- Business logic not reusable
- Hard to test in isolation
- Violates single responsibility

**✅ CORRECT:**

```java
@Service
public class UserService {
    
    @Transactional
    public void activateUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        
        // ✅ Delegate to domain service
        userDomainService.activateUser(user);
        
        userRepository.save(user);
    }
}
```

---

### Mistake 3: Complex Logic in Entity

**❌ WRONG:**

```java
@Entity
public class User {
    
    @Transient
    private UserRepository repository; // ❌ NO!
    
    public void activate() {
        if (repository.existsByEmailAndStatus(email, ACTIVE)) {
            throw new IllegalStateException("Cannot activate");
        }
        this.status = ACTIVE;
    }
}
```

**Why wrong?**
- Entity has infrastructure dependency
- Violates JPA entity contract
- Impossible to test

**✅ CORRECT:**

```java
@Entity
public class User {
    // Just data + basic validation
    
}

// Business logic in domain service
@Component
public class UserDomainService {
    public void activateUser(User user) {
        if (user.getStatus() == ACTIVE) {
            throw new IllegalStateException("Already active");
        }
        user.setStatus(ACTIVE);
    }
}
```

---

### Mistake 4: Skipping Domain Service Entirely

**❌ WRONG:**

```java
@Service
public class UserService {
    
    @Transactional
    public void updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow();
        
        // ❌ Direct manipulation without business rule validation
        user.setStatus(request.status());
        user.setEmail(request.email());
        
        userRepository.save(user);
    }
}
```

**Why wrong?**
- No validation of business rules
- State transitions not controlled
- Business logic scattered

**✅ CORRECT:**

```java
@Service
public class UserService {
    
    @Transactional
    public void updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow();
        
        user.setEmail(request.email());
        
        // ✅ Validate through domain service
        userDomainService.validateUserData(user);
        
        // ✅ Use domain service for status change
        userDomainService.changeStatus(user, request.status());
        
        userRepository.save(user);
    }
}
```

---

## Testing Strategy

### Unit Testing Domain Services (Fast, Isolated)

**Goal:** Test business logic without any infrastructure

```java
class UserDomainServiceTest {
    
    private UserDomainService domainService;
    
    @BeforeEach
    void setUp() {
        // ✅ No Spring context needed
        // ✅ No database needed
        // ✅ No mocks needed
        this.domainService = new UserDomainService();
    }
    
    @Test
    void activateUser_shouldThrowException_whenAlreadyActive() {
        // Given
        User user = new User();
        user.setStatus(UserStatus.ACTIVE);
        
        // When & Then
        assertThrows(IllegalStateException.class,
            () -> this.domainService.activateUser(user));
    }
    
    @Test
    void activateUser_shouldSetStatusActive_whenUserInactive() {
        // Given
        User user = new User();
        user.setStatus(UserStatus.INACTIVE);
        
        // When
        this.domainService.activateUser(user);
        
        // Then
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }
    
    @Test
    void validateUserData_shouldThrowException_whenInvalidPhone() {
        // Given
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("invalid");
        
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> this.domainService.validateUserData(user));
    }
}
```

**Benefits:**
- ⚡ Extremely fast (milliseconds)
- 🎯 Tests pure business logic
- 🔧 Easy to write and maintain
- 🚀 No infrastructure setup needed

---

### Integration Testing Application Services

**Goal:** Test orchestration and database interactions

```java
@SpringBootTest
@Transactional
class UserServiceIntegrationTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Test
    void createUser_shouldPersistUser_whenValidRequest() {
        // Given
        Organization org = new Organization();
        org.setName("Test Corp");
        org.setTaxId("12345");
        org = this.organizationRepository.save(org);
        
        CreateUserRequest request = new CreateUserRequest(
            "John",
            "Doe",
            "john@example.com",
            org.getId()
        );
        
        // When
        UserResponse response = this.userService.createUser(request);
        
        // Then
        assertNotNull(response.id());
        assertEquals("John", response.firstName());
        
        // Verify persistence
        User savedUser = this.userRepository.findById(response.id()).orElseThrow();
        assertEquals("john@example.com", savedUser.getEmail());
    }
    
    @Test
    void createUser_shouldThrowException_whenEmailExists() {
        // Given
        Organization org = createTestOrganization();
        
        CreateUserRequest request1 = new CreateUserRequest(
            "John", "Doe", "john@example.com", org.getId()
        );
        this.userService.createUser(request1);
        
        CreateUserRequest request2 = new CreateUserRequest(
            "Jane", "Smith", "john@example.com", org.getId()
        );
        
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> this.userService.createUser(request2));
    }
}
```

**Benefits:**
- 🔍 Tests full workflow
- 💾 Verifies database interactions
- 🔗 Tests transaction management
- 📊 Integration between layers

---

### Controller Testing (API Layer)

**Goal:** Test HTTP endpoints

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void createUser_shouldReturn201_whenValidRequest() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
            "John", "Doe", "john@example.com", UUID.randomUUID()
        );
        
        UserResponse response = new UserResponse(
            UUID.randomUUID(),
            "John",
            "Doe",
            "john@example.com",
            UserStatus.ACTIVE
        );
        
        when(this.userService.createUser(any())).thenReturn(response);
        
        // When & Then
        this.mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }
    
    @Test
    void createUser_shouldReturn400_whenInvalidRequest() throws Exception {
        // Given - missing required fields
        String invalidJson = "{}";
        
        // When & Then
        this.mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }
}
```

---

### Testing Pyramid

```
      /\
     /  \      E2E Tests (Few)
    /────\     - Full system tests
   /      \    - Slow, expensive
  /────────\   
 /          \  Integration Tests (Some)
/────────────\ - Service + Repository
              - Database interactions
──────────────
              Unit Tests (Many)
              - Domain services
              - Fast, isolated
              - Core business logic
```

**Recommended Distribution:**
- 70% Unit tests (Domain Services)
- 25% Integration tests (Application Services)
- 5% E2E tests (Full API)

---

## Decision Trees

### Decision Tree 1: Where Should This Logic Go?

```
Does this code need to query the database?
│
├─ YES
│  │
│  └─ Is it checking uniqueness/existence?
│     │
│     ├─ YES → Application Service (private method)
│     │        Example: validateUniqueEmail()
│     │
│     └─ NO → Application Service (load entities)
│              Example: findById(), findByOrganization()
│
└─ NO
   │
   └─ Does it manipulate object state/data?
      │
      ├─ YES
      │  │
      │  └─ Is it complex business logic?
      │     │
      │     ├─ YES → Domain Service
      │     │        Example: activateUser(), validatePromotion()
      │     │
      │     └─ NO → Entity (simple helper)
      │              Example: getFullName()
      │
      └─ NO → Utility/Helper Class
               Example: DateUtils, StringUtils
```

---

### Decision Tree 2: How Should I Test This?

```
What am I testing?
│
├─ Business Logic
│  │
│  └─ Domain Service → Unit Test
│                      - No Spring context
│                      - No database
│                      - Super fast
│
├─ Database Interactions
│  │
│  └─ Repository → Integration Test
│                  - Spring context
│                  - Test database
│                  - @DataJpaTest
│
├─ Use Case Workflow
│  │
│  └─ Application Service → Integration Test
│                            - Full Spring context
│                            - Test database
│                            - @SpringBootTest
│
└─ HTTP Endpoints
   │
   └─ Controller → Mock MVC Test
                   - @WebMvcTest
                   - Mock services
                   - Test HTTP layer only
```

---

### Decision Tree 3: How to Handle Validation?

```
What type of validation?
│
├─ Format/Pattern Validation
│  │
│  └─ Examples: email format, phone format, date format
│     │
│     └─ Domain Service
│        public void validateEmailFormat(String email)
│
├─ Uniqueness Validation
│  │
│  └─ Examples: unique email, unique username
│     │
│     └─ Application Service (private method)
│        private void validateUniqueEmail(String email)
│
├─ Business Rule Validation
│  │
│  └─ Examples: age requirements, eligibility checks
│     │
│     └─ Domain Service
│        public void validatePromotion Eligibility(User user)
│
├─ Input Validation (DTO)
   │
   └─ Examples: @NotNull, @Size, @Email
      │
      └─ DTO Annotations (JSR-303)
         @NotBlank String email
```

---

## Best Practices

### 1. Naming Conventions

**Domain Services:**
```java
// ✅ Good names
UserDomainService
OrderDomainService
PaymentDomainService

// ❌ Avoid
UserBusinessLogic
UserHelper
UserUtils
```

**Application Services:**
```java
// ✅ Good names
UserService
OrderService
PaymentService

// ❌ Avoid
UserApplicationService  // Redundant
UserManager
UserHandler
```

**Methods in Domain Services:**
```java
// ✅ Good - action verbs
activateUser(User user)
validatePromotion(User user)
calculateDiscount(Order order)

// ❌ Avoid
handleUserActivation(UUID id)  // Takes ID instead of object
processUser(User user)  // Vague
```

**Methods in Application Services:**
```java
// ✅ Good - use case oriented
createUser(CreateUserRequest request)
activateUser(UUID userId)
getUserById(UUID id)

// ❌ Avoid
doUserCreation(...)  // Awkward naming
handleActivation(...)  // Vague
```

---

### 2. Exception Handling

**Domain Services should throw:**
- `IllegalArgumentException` - Invalid input/data
- `IllegalStateException` - Invalid state transition
- Custom business exceptions

```java
@Component
public class UserDomainService {
    
    public void activateUser(User user) {
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException(
                "User is already active"
            );
        }
        user.setStatus(UserStatus.ACTIVE);
    }
}
```

**Application Services should throw:**
- `ResourceNotFoundException` - Entity not found
- Infrastructure exceptions
- Propagate domain exceptions

```java
@Service
public class UserService {
    
    @Transactional
    public UserResponse activateUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found: " + id
            ));
        
        // Domain exception propagates up
        userDomainService.activateUser(user);
        
        return mapper.toResponse(userRepository.save(user));
    }
}
```

**Global Exception Handler:**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            IllegalArgumentException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ex.getMessage()));
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            IllegalStateException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

---

### 3. Transaction Management

**Always on Application Service:**

```java
@Service
@Transactional(readOnly = true)  // ✅ Default for read operations
public class UserService {
    
    // ✅ Read operation - uses class-level @Transactional
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return mapper.toResponse(user);
    }
    
    // ✅ Write operation - override with readOnly = false
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // All database operations in one transaction
        validateUniqueEmail(request.email());
        
        Organization org = organizationRepository.findById(...).orElseThrow();
        
        User user = new User();
        // ... set properties
        
        userDomainService.validateUserData(user);
        
        User saved = userRepository.save(user);
        
        return mapper.toResponse(saved);
    }
}
```

**Never on Domain Services:**

```java
// ❌ WRONG
@Component
@Transactional  // NO! Domain service should not manage transactions
public class UserDomainService {
    // ...
}

// ✅ CORRECT
@Component  // No @Transactional
public class UserDomainService {
    // Pure business logic, no transaction management
}
```

---

### 4. Dependency Injection

**Domain Service Dependencies:**

```java
@Component
@RequiredArgsConstructor
public class UserDomainService {
    
    // ✅ OK - other domain services
    private final PasswordPolicyService passwordPolicyService;
    
    // ✅ OK - utility services (no infrastructure)
    private final PasswordEncoder passwordEncoder;
    
    // ❌ NOT OK - repositories
    // private final UserRepository repository;
    
    // ❌ NOT OK - application services
    // private final UserService userService;
}
```

**Application Service Dependencies:**

```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    // ✅ OK - repositories
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    
    // ✅ OK - domain services
    private final UserDomainService userDomainService;
    
    // ✅ OK - mappers
    private final UserMapper userMapper;
    
    // ⚠️ AVOID - other application services (can lead to circular dependencies)
    // private final OrganizationService organizationService;
}
```

---

### 5. Documentation

**Document Domain Services Clearly:**

```java
@Component
public class UserDomainService {
    
    /**
     * Activates a user account.
     * 
     * Business Rules:
     * - User must not already be active
     * - User account must not be suspended
     * 
     * @param user The user to activate (must be loaded in memory)
     * @throws IllegalStateException if user is already active
     * @throws IllegalStateException if user is suspended
     */
    public void activateUser(User user) {
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException(
                "User is already active"
            );
        }
        
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new IllegalStateException(
                "Cannot activate suspended user"
            );
        }
        
        user.setStatus(UserStatus.ACTIVE);
    }
}
```

**Document Complex Workflows:**

```java
@Service
public class UserService {
    
    /**
     * Creates a new user with credentials.
     * 
     * Workflow:
     * 1. Validates email uniqueness (infrastructure)
     * 2. Loads organization from database
     * 3. Creates user entity
     * 4. Validates user data (business rules)
     * 5. Persists user
     * 6. Creates credentials
     * 7. Hashes password
     * 8. Persists credentials
     * 
     * @param request User creation request
     * @return Created user with credentials
     * @throws IllegalArgumentException if email already exists
     * @throws ResourceNotFoundException if organization not found
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // Implementation
    }
}
```

---

## FAQ

### Q: Should I always create a domain service?

**A:** No, only when you have business logic that:
1. Operates on domain objects
2. Is complex enough to warrant extraction
3. Should be tested independently

For simple CRUD with no business rules, you can skip domain services:

```java
// Simple case - no domain service needed
@Service
public class CategoryService {
    
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        // Simple validation is done by input-validation on DTO (JSR-303)
        Category category = mapper.toEntity(request);
        Category saved = repository.save(category);
        return mapper.toResponse(saved);
    }
}
```

---

### Q: Can domain services call other domain services?

**A:** Yes! Domain services can and should call other domain services:

```java
@Component
@RequiredArgsConstructor
public class OrderDomainService {
    
    private final PricingDomainService pricingDomainService; // ✅ OK
    private final InventoryDomainService inventoryDomainService; // ✅ OK
    
    public void validateOrder(Order order) {
        // Use other domain services
        this.pricingDomainService.validatePricing(order);
        this.inventoryDomainService.checkAvailability(order.getItems());
    }
}
```

---

### Q: What about cross-entity business rules?

**A:** Cross-entity rules go in domain services and receive all relevant objects:

```java
@Component
public class OrderDomainService {
    
    /**
     * Validates if user can place order
     * Cross-entity business rule
     */
    public void validateUserCanPlaceOrder(User user, Order order) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("User not active");
        }
        
        if (order.getTotal().compareTo(user.getCreditLimit()) > 0) {
            throw new IllegalArgumentException("Exceeds credit limit");
        }
    }
}
```

Application service loads both entities and passes to domain service:

```java
@Service
public class OrderService {
    
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Load both entities
        User user = userRepository.findById(request.userId()).orElseThrow();
        Order order = buildOrder(request);
        
        // Pass both to domain service
        orderDomainService.validateUserCanPlaceOrder(user, order);
        
        return mapper.toResponse(orderRepository.save(order));
    }
}
```

---

### Q: Should I use value objects?

**A:** Optional. Value objects add complexity but provide benefits:

**Without value objects (simpler):**
```java
@Entity
public class User {
    private String email;
    private String phoneNumber;
}

@Component
public class UserDomainService {
    public void validateEmail(String email) {
        if (!email.matches("...")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
}
```

**With value objects (more complex but type-safe):**
```java
@Embeddable
public class Email {
    private final String value;
    
    public Email(String value) {
        if (!value.matches("...")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.value = value;
    }
}

@Entity
public class User {
    @Embedded
    private Email email;
}
```

Use value objects when:
- Type safety is important
- Value has complex validation
- Value is used across many entities

---

### Q: How do I handle auditing (createdAt, updatedAt)?

**A:** Use JPA lifecycle callbacks in entities:

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

Enable JPA auditing:

```java
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
```

---

### Q: What about pagination and sorting?

**A:** Handle in application service, use Spring Data:

```java
@Service
public class UserService {
    
    public Page<UserListResponse> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toListResponse);
    }
    
    public Page<UserListResponse> getUsersByOrganization(
            UUID orgId, 
            Pageable pageable) {
        Page<User> users = userRepository.findByOrganizationId(
            orgId, 
            pageable
        );
        return users.map(userMapper::toListResponse);
    }
}
```

Controller:

```java
@GetMapping
public ResponseEntity<Page<UserListResponse>> getUsers(
        @PageableDefault(size = 20, sort = "createdAt", direction = DESC) 
        Pageable pageable) {
    return ResponseEntity.ok(userService.getUsers(pageable));
}
```

---

### Q: Should DTOs be records or classes?

**A:** Use **records** for immutable DTOs (recommended):

```java
// ✅ Preferred - immutable, concise
public record CreateUserRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email
) {}

public record UserResponse(
    UUID id,
    String fullName,
    String email,
    UserStatus status
) {}
```

Use **classes** if you need mutability or inheritance:

```java
// Use when needed
@Data
public class UpdateUserRequest {
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    // Setters needed for frameworks
}
```

---

### Q: How to handle soft deletes?

**A:** Use Hibernate soft delete feature (org.hibernate.annotations.SoftDelete)

```java
@Entity
@SoftDelete
public class User {
    
// Other user properties    

}

@Component
public class UserDomainService {
    
    public void softDelete(User user) {
        if (user.isDeleted()) {
            throw new IllegalStateException("User already deleted");
        }
        
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException(
                "Cannot delete active user"
            );
        }
        
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setStatus(UserStatus.INACTIVE);
    }
    
    public void restore(User user) {
        if (!user.isDeleted()) {
            throw new IllegalStateException("User not deleted");
        }
        
        user.setDeleted(false);
        user.setDeletedAt(null);
        user.setStatus(UserStatus.ACTIVE);
    }
}
```

---

## Summary Checklist

When implementing a new feature, use this checklist:

### ✅ Entity
- [ ] Has JPA annotations
- [ ] Has simple helper methods only
- [ ] NO business logic
- [ ] NO repository dependencies

### ✅ Domain Service
- [ ] Has NO repository dependencies
- [ ] Methods receive domain objects, not IDs
- [ ] Contains pure business logic
- [ ] Validates business rules
- [ ] Handles state transitions
- [ ] Easily testable without infrastructure

### ✅ Application Service
- [ ] Has repository dependencies
- [ ] Annotated with `@Transactional`
- [ ] Orchestrates use cases
- [ ] Loads entities from database
- [ ] Validates uniqueness (infrastructure)
- [ ] Delegates business logic to domain services
- [ ] Maps DTOs ↔ Entities

### ✅ Repository
- [ ] Extends `JpaRepository`
- [ ] Has custom queries if needed
- [ ] NO business logic

### ✅ Controller
- [ ] Handles HTTP only
- [ ] Validates DTOs with `@Valid`
- [ ] Returns appropriate status codes
- [ ] Delegates to application services
- [ ] NO business logic
- [ ] NO repository access

### ✅ Tests
- [ ] Domain services have unit tests
- [ ] Application services have integration tests
- [ ] Controllers have MockMvc tests
- [ ] Core business logic is tested

---

## Conclusion

The Domain Service Pattern provides:

✅ **Clear Separation** - Each layer has well-defined responsibilities  
✅ **Testability** - Business logic can be tested without infrastructure  
✅ **Maintainability** - Logic is organized and easy to find  
✅ **Pragmatism** - Not over-engineered, suitable for real-world projects  
✅ **Scalability** - Easy to add new features and business rules

**Remember the golden rule:**

> **Domain Services = Pure Business Logic on Objects**  
> **Application Services = Orchestration + Infrastructure**

When in doubt, ask: "Does this need database access?"
- **Yes** → Application Service
- **No** → Domain Service

---

**Document Version:** 1.0  
**Maintained by:** Development Team  
**Questions?** Refer to code examples in this repository or ask the team lead.

---

## Quick Reference Card

```
┌─────────────────────────────────────────────────────────────┐
│                    QUICK REFERENCE                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ENTITY                                                     │
│  - JPA annotations                                          │
│  - Basic validation                                         │
│  - Simple helpers                                           │
│                                                             │
│  DOMAIN SERVICE                                             │
│  - NO repositories ⚠️                                       │
│  - Receives objects, not IDs                                │
│  - Pure business logic                                      │
│                                                             │
│  APPLICATION SERVICE                                        │
│  - @Transactional                                           │
│  - Has repositories                                         │
│  - Orchestrates workflows                                   │
│  - Validates uniqueness                                     │
│                                                             │
│  REPOSITORY                                                 │
│  - extends JpaRepository                                    │
│  - Custom queries                                           │
│                                                             │
│  CONTROLLER                                                 │
│  - HTTP only                                                │
│  - @Valid on DTOs                                           │
│  - Delegates to service                                     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```
