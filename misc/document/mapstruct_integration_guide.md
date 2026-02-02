# MapStruct Integration Guide - Domain Service Architecture

## Overview

MapStruct is a code generator that creates type-safe, performant bean mappings at compile time. This guide shows how to use MapStruct while adhering to the Domain Service Architecture principles.

---

## 🎯 Core Principles with MapStruct

### 1. Mappers Have NO Repository Dependencies

```java
// ❌ WRONG - Repository in mapper
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Autowired  // ❌ NO!
    OrganizationRepository organizationRepository;
    
    User toEntity(CreateUserRequest request);
}

// ✅ CORRECT - Pure mapping only
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    // No dependencies - just mappings
    User toEntity(CreateUserRequest request);
    
    UserResponse toResponse(User user);
}
```

### 2. Application Services Handle Relationships

MapStruct maps simple fields. Application services load and set relationships:

```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper mapper;
    private final OrganizationRepository organizationRepository;
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // 1. ✅ MapStruct maps simple fields
        User user = mapper.toEntity(request);
        
        // 2. ✅ Application service loads and sets relationships
        Organization org = organizationRepository
            .findById(request.organizationId())
            .orElseThrow();
        user.setOrganization(org);
        
        // Continue...
    }
}
```

---

## 🏗️ Creating a Base Mapper (Shared Configuration)

### Yes, Create a Shared Configuration!

**Purpose:** Avoid repeating common configurations across all mappers.

**Benefits:**
- ✅ DRY principle - define once, use everywhere
- ✅ Consistent behavior across all mappers
- ✅ Centralized configuration
- ✅ Easier maintenance

### Option 1: Shared MapperConfig (Recommended)

Create a shared configuration that all mappers can use:

```java
package com.example.usermanagement.mapper.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.MappingInheritanceStrategy;

/**
 * Shared MapStruct configuration
 * All mappers should use this config to ensure consistency
 */
@MapperConfig(
    componentModel = "spring",  // Spring dependency injection
    unmappedTargetPolicy = ReportingPolicy.WARN,  // Warn about unmapped fields
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,  // Ignore nulls
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,  // Always check for nulls
    mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG  // Inherit config
)
public interface MapperConfig {
    // This interface is just for configuration
    // No methods needed
}
```

**Usage in Mappers:**

```java
package com.example.usermanagement.mapper;

import com.example.usermanagement.mapper.config.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)  // ✅ Use shared config
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)  // Set by application service
    @Mapping(target = "credential", ignore = true)    // Set by application service
    @Mapping(target = "status", ignore = true)        // Set by application service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(CreateUserRequest request);
    
    UserResponse toResponse(User user);
    
    UserListResponse toListResponse(User user);
}
```

---

### Option 2: Abstract Base Mapper (For Common Methods)

If you have **common methods** across multiple mappers, create an abstract base:

```java
package com.example.usermanagement.mapper;

import org.mapstruct.MapperConfig;

/**
 * Base mapper with common utility methods
 * All domain mappers can extend this
 */
@MapperConfig(componentModel = "spring")
public interface BaseMapper {
    
    /**
     * Converts UUID to String
     * Available to all mappers
     */
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
    
    /**
     * Converts String to UUID
     * Available to all mappers
     */
    default UUID stringToUuid(String str) {
        return str != null ? UUID.fromString(str) : null;
    }
    
    /**
     * Formats LocalDateTime to ISO string
     * Available to all mappers
     */
    default String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
    
    /**
     * Formats money with currency
     * Available to all mappers
     */
    default String formatMoney(BigDecimal amount, String currency) {
        if (amount == null) return null;
        return String.format("%s %s", currency, amount.toString());
    }
}
```

**Usage:**

```java
@Mapper(config = BaseMapper.class)  // ✅ Inherit common methods
public interface UserMapper {
    
    // Automatically has access to uuidToString, stringToUuid, etc.
    
    @Mapping(target = "id", ignore = true)
    User toEntity(CreateUserRequest request);
    
    UserResponse toResponse(User user);
}
```

---

## 📋 Complete MapStruct Examples

### Example 1: User Mapper

**Entities:**

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    
    @OneToOne(mappedBy = "user")
    private Credential credential;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**DTOs:**

```java
public record CreateUserRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    String phoneNumber,
    @NotNull UUID organizationId
) {}

public record UpdateUserRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    String phoneNumber
) {}

public record UserResponse(
    UUID id,
    String firstName,
    String lastName,
    String fullName,
    String email,
    String phoneNumber,
    UserStatus status,
    OrganizationSummary organization,
    CredentialSummary credential,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

public record UserListResponse(
    UUID id,
    String fullName,
    String email,
    UserStatus status,
    String organizationName
) {}
```

**Mapper:**

```java
package com.example.usermanagement.mapper;

import com.example.usermanagement.dto.user.*;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.entity.Organization;
import com.example.usermanagement.mapper.config.MapperConfig;
import org.mapstruct.*;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    
    /**
     * Maps CreateUserRequest to User entity
     * Only maps simple fields - relationships handled by application service
     */
    @Mapping(target = "id", ignore = true)  // Generated by database
    @Mapping(target = "organization", ignore = true)  // Set by application service
    @Mapping(target = "credential", ignore = true)  // Set by application service
    @Mapping(target = "status", ignore = true)  // Set by application service
    @Mapping(target = "createdAt", ignore = true)  // Set by JPA
    @Mapping(target = "updatedAt", ignore = true)  // Set by JPA
    User toEntity(CreateUserRequest request);
    
    /**
     * Updates existing User entity from UpdateUserRequest
     * Only updates simple fields
     */
    @Mapping(target = "id", ignore = true)  // Never update ID
    @Mapping(target = "organization", ignore = true)  // Don't change org
    @Mapping(target = "credential", ignore = true)  // Don't change credential
    @Mapping(target = "status", ignore = true)  // Use separate method for status
    @Mapping(target = "createdAt", ignore = true)  // Immutable
    @Mapping(target = "updatedAt", ignore = true)  // Handled by JPA
    void updateEntity(@MappingTarget User user, UpdateUserRequest request);
    
    /**
     * Maps User entity to UserResponse
     * Includes all nested objects
     */
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "organization", source = "organization")
    @Mapping(target = "credential", source = "credential")
    UserResponse toResponse(User user);
    
    /**
     * Maps User entity to UserListResponse (summary view)
     */
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "organizationName", source = "organization.name")
    UserListResponse toListResponse(User user);
    
    /**
     * Maps Organization to OrganizationSummary
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    OrganizationSummary toOrganizationSummary(Organization organization);
    
    /**
     * Maps list of users
     */
    List<UserResponse> toResponseList(List<User> users);
    
    List<UserListResponse> toListResponseList(List<User> users);
}
```

---

### Example 2: Organization Mapper

```java
@Mapper(config = MapperConfig.class)
public interface OrganizationMapper {
    
    /**
     * Maps CreateOrganizationRequest to Organization entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)  // Set default in application service
    @Mapping(target = "users", ignore = true)  // Empty list initially
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Organization toEntity(CreateOrganizationRequest request);
    
    /**
     * Updates Organization entity from UpdateOrganizationRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Organization organization, 
                      UpdateOrganizationRequest request);
    
    /**
     * Maps Organization to OrganizationResponse
     */
    @Mapping(target = "userCount", expression = "java(organization.getUsers().size())")
    OrganizationResponse toResponse(Organization organization);
    
    /**
     * Maps Organization to OrganizationListResponse
     */
    @Mapping(target = "userCount", expression = "java(organization.getUsers().size())")
    OrganizationListResponse toListResponse(Organization organization);
    
    List<OrganizationResponse> toResponseList(List<Organization> organizations);
    
    List<OrganizationListResponse> toListResponseList(List<Organization> organizations);
}
```

---

### Example 3: Credential Mapper (With Domain Service)

**Important:** Even with MapStruct, domain services are still used for business logic.

```java
@Mapper(config = MapperConfig.class)
public interface CredentialMapper {
    
    /**
     * Maps CreateCredentialRequest to Credential entity
     * Note: Password is NOT mapped - handled by domain service
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)  // ⚠️ Set by domain service
    @Mapping(target = "user", ignore = true)  // Set by application service
    @Mapping(target = "lastPasswordChange", ignore = true)  // Set by domain service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Credential toEntity(CreateCredentialRequest request);
    
    /**
     * Maps Credential to CredentialResponse
     * Uses domain service to check if password is expired
     */
    CredentialResponse toResponse(Credential credential);
    
    CredentialListResponse toListResponse(Credential credential);
    
    List<CredentialResponse> toResponseList(List<Credential> credentials);
}
```

**Application Service using the mapper:**

```java
@Service
@RequiredArgsConstructor
public class CredentialService {
    
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final CredentialDomainService credentialDomainService;
    private final CredentialMapper credentialMapper;
    
    @Transactional
    public CredentialResponse createCredential(CreateCredentialRequest request) {
        // 1. Validate uniqueness (infrastructure)
        if (credentialRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username exists");
        }
        
        // 2. Load user (infrastructure)
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // 3. ✅ MapStruct maps simple fields
        Credential credential = credentialMapper.toEntity(request);
        
        // 4. ✅ Application service sets relationships
        credential.setUser(user);
        
        // 5. ✅ Domain service hashes password (business logic)
        String passwordHash = credentialDomainService.hashPassword(request.password());
        credential.setPasswordHash(passwordHash);
        
        // 6. ✅ Domain service validates
        credentialDomainService.validateCredentialData(credential);
        
        // 7. Persist
        Credential saved = credentialRepository.save(credential);
        
        // 8. ✅ MapStruct maps to response
        return credentialMapper.toResponse(saved);
    }
}
```

---

## 🔧 Advanced MapStruct Techniques

### 1. Custom Mapping Methods

When you need logic in mapping, use `@Named` methods:

```java
@Mapper(config = MapperConfig.class)
public interface UserMapper {
    
    @Mapping(target = "fullName", qualifiedByName = "buildFullName")
    UserResponse toResponse(User user);
    
    /**
     * Custom method for building full name
     */
    @Named("buildFullName")
    default String buildFullName(User user) {
        if (user.getFirstName() == null && user.getLastName() == null) {
            return "Unknown";
        }
        return String.format("%s %s", 
            user.getFirstName() != null ? user.getFirstName() : "",
            user.getLastName() != null ? user.getLastName() : ""
        ).trim();
    }
}
```

### 2. Conditional Mapping

```java
@Mapper(config = MapperConfig.class)
public interface UserMapper {
    
    @Mapping(target = "organization", 
             source = "organization", 
             conditionExpression = "java(user.getOrganization() != null)")
    UserResponse toResponse(User user);
}
```

### 3. Using Domain Service in Mapper (For Read-Only Logic)

**Only for calculation/derivation, NOT for validation or state changes:**

```java
@Mapper(config = MapperConfig.class, uses = {CredentialDomainService.class})
public abstract class CredentialMapper {
    
    @Autowired
    protected CredentialDomainService credentialDomainService;
    
    /**
     * Maps to response, using domain service to check expiration
     */
    @Mapping(target = "passwordExpired", 
             expression = "java(credentialDomainService.isPasswordExpired(credential))")
    public abstract CredentialResponse toResponse(Credential credential);
}
```

**⚠️ Important Constraints:**
- ✅ OK: Read-only calculations (isPasswordExpired, calculateAge, etc.)
- ❌ NOT OK: State modifications
- ❌ NOT OK: Validation that throws exceptions
- ❌ NOT OK: Database queries

---

## 🎯 Complete Example: User Service with MapStruct

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final CredentialRepository credentialRepository;
    private final UserDomainService userDomainService;
    private final CredentialDomainService credentialDomainService;
    private final UserMapper userMapper;  // ✅ MapStruct generated
    
    /**
     * Get all users
     */
    public List<UserListResponse> getAllUsers() {
        List<User> users = userRepository.findAllWithDetails();
        return userMapper.toListResponseList(users);  // ✅ MapStruct
    }
    
    /**
     * Get user by ID
     */
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return userMapper.toResponse(user);  // ✅ MapStruct
    }
    
    /**
     * Create user
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // 1. Infrastructure: Validate uniqueness
        validateUniqueEmail(request.email());
        validateUniqueUsername(request.username());
        
        // 2. Infrastructure: Load organization
        Organization organization = organizationRepository
            .findById(request.organizationId())
            .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        
        // 3. ✅ MapStruct: Map simple fields
        User user = userMapper.toEntity(request);
        
        // 4. Application Service: Set relationships and defaults
        user.setOrganization(organization);
        user.setStatus(UserStatus.PENDING);
        
        // 5. Domain Service: Validate business rules
        userDomainService.validateUserData(user);
        
        // 6. Infrastructure: Persist user
        User savedUser = userRepository.save(user);
        
        // 7. Create credential
        Credential credential = new Credential();
        credential.setUsername(request.username());
        credential.setUser(savedUser);
        
        // 8. Domain Service: Hash password
        String passwordHash = credentialDomainService.hashPassword(request.password());
        credential.setPasswordHash(passwordHash);
        
        // 9. Domain Service: Validate credential
        credentialDomainService.validateCredentialData(credential);
        
        // 10. Infrastructure: Persist credential
        Credential savedCredential = credentialRepository.save(credential);
        savedUser.setCredential(savedCredential);
        
        // 11. ✅ MapStruct: Map to response
        return userMapper.toResponse(savedUser);
    }
    
    /**
     * Update user
     */
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        // 1. Infrastructure: Load user
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // 2. Infrastructure: Validate unique email
        validateUniqueEmail(request.email(), id);
        
        // 3. ✅ MapStruct: Update entity from request
        userMapper.updateEntity(user, request);
        
        // 4. Domain Service: Validate updated data
        userDomainService.validateUserData(user);
        
        // 5. Infrastructure: Persist
        User updated = userRepository.save(user);
        
        // 6. ✅ MapStruct: Map to response
        return userMapper.toResponse(updated);
    }
    
    /**
     * Change user status
     */
    @Transactional
    public UserResponse changeUserStatus(UUID id, ChangeUserStatusRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // ✅ Domain Service: Handle state transition
        switch (request.status()) {
            case ACTIVE -> userDomainService.activateUser(user);
            case INACTIVE -> userDomainService.deactivateUser(user);
            case SUSPENDED -> userDomainService.suspendUser(user);
        }
        
        User updated = userRepository.save(user);
        
        // ✅ MapStruct: Map to response
        return userMapper.toResponse(updated);
    }
    
    // Private helper methods for infrastructure concerns
    
    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email exists: " + email);
        }
    }
    
    private void validateUniqueEmail(String email, UUID excludeUserId) {
        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(excludeUserId)) {
                throw new IllegalArgumentException("Email exists: " + email);
            }
        });
    }
    
    private void validateUniqueUsername(String username) {
        if (credentialRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username exists: " + username);
        }
    }
}
```

---

## 📊 Responsibility Matrix with MapStruct

| Concern | MapStruct Mapper | Application Service | Domain Service |
|---------|------------------|---------------------|----------------|
| Simple field mapping (request → entity) | ✅ | ❌ | ❌ |
| Simple field mapping (entity → response) | ✅ | ❌ | ❌ |
| Load related entities from DB | ❌ | ✅ | ❌ |
| Set relationships | ❌ | ✅ | ❌ |
| Set default values | ❌ | ✅ | ❌ |
| Hash passwords | ❌ | ❌ | ✅ |
| Validate business rules | ❌ | ❌ | ✅ |
| State transitions | ❌ | ❌ | ✅ |
| Read-only calculations* | ✅ | ✅ | ✅ |

*Read-only calculations can be in mappers if they don't modify state

---

## ✅ Best Practices Summary

### 1. Use Shared Configuration

```java
@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MapperConfig {}
```

All mappers reference this:
```java
@Mapper(config = MapperConfig.class)
public interface UserMapper {}
```

### 2. Create Abstract Base Mapper for Common Utilities

```java
@MapperConfig(componentModel = "spring")
public interface BaseMapper {
    default String formatDateTime(LocalDateTime dt) { ... }
    default UUID stringToUuid(String str) { ... }
}
```

### 3. Always Ignore What Application Service Handles

```java
@Mapping(target = "id", ignore = true)  // Database generates
@Mapping(target = "organization", ignore = true)  // App service sets
@Mapping(target = "createdAt", ignore = true)  // JPA handles
User toEntity(CreateUserRequest request);
```

### 4. Use @MappingTarget for Updates

```java
void updateEntity(@MappingTarget User user, UpdateUserRequest request);
```

### 5. Keep Mappers Pure

```java
// ❌ NO repositories
// ❌ NO database queries
// ❌ NO business validation
// ✅ Just field mapping
```

---

## 🧪 Testing MapStruct Mappers

```java
@SpringBootTest  // Need Spring context for MapStruct component
class UserMapperTest {
    
    @Autowired
    private UserMapper mapper;
    
    @Test
    void toEntity_shouldMapAllSimpleFields() {
        // Given
        CreateUserRequest request = new CreateUserRequest(
            "John",
            "Doe",
            "john@example.com",
            "+1234567890",
            UUID.randomUUID()
        );
        
        // When
        User user = mapper.toEntity(request);
        
        // Then
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("+1234567890", user.getPhoneNumber());
        
        // Organization not set by mapper
        assertNull(user.getOrganization());
        assertNull(user.getId());
    }
    
    @Test
    void toResponse_shouldMapAllFields() {
        // Given
        Organization org = new Organization();
        org.setId(UUID.randomUUID());
        org.setName("Test Corp");
        
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setStatus(UserStatus.ACTIVE);
        user.setOrganization(org);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // When
        UserResponse response = mapper.toResponse(user);
        
        // Then
        assertEquals(user.getId(), response.id());
        assertEquals("John", response.firstName());
        assertEquals("John Doe", response.fullName());
        assertEquals("Test Corp", response.organization().name());
    }
    
    @Test
    void updateEntity_shouldOnlyUpdateMappedFields() {
        // Given
        User existing = new User();
        existing.setId(UUID.randomUUID());
        existing.setFirstName("John");
        existing.setLastName("Doe");
        existing.setStatus(UserStatus.ACTIVE);
        
        UpdateUserRequest request = new UpdateUserRequest(
            "Jane",
            "Smith",
            "jane@example.com",
            "+9876543210"
        );
        
        // When
        mapper.updateEntity(existing, request);
        
        // Then
        assertEquals("Jane", existing.getFirstName());
        assertEquals("Smith", existing.getLastName());
        assertEquals("jane@example.com", existing.getEmail());
        
        // ID and status should not change
        assertNotNull(existing.getId());
        assertEquals(UserStatus.ACTIVE, existing.getStatus());
    }
}
```

---

## 🎯 Decision Tree: Should I Use MapStruct or Manual Mapping?

```
Is the mapping simple (direct field to field)?
│
├─ YES
│  │
│  └─ Are there 5+ fields to map?
│     │
│     ├─ YES → Use MapStruct ✅
│     │        (Reduces boilerplate)
│     │
│     └─ NO → Either is fine
│              (MapStruct still preferred for consistency)
│
└─ NO (Complex mapping logic required)
   │
   └─ Use manual mapping or MapStruct with custom methods
      (Use @Named methods in MapStruct)
```

---

## 📋 Checklist: Adding MapStruct to Existing Project

- [ ] Add MapStruct dependencies to pom.xml
- [ ] Configure annotation processors (Lombok first, then MapStruct)
- [ ] Create `MapperConfig` interface with shared configuration
- [ ] Create `BaseMapper` interface with common utility methods (optional)
- [ ] Convert existing manual mappers to MapStruct interfaces
- [ ] Add `@Mapping` annotations to ignore application service responsibilities
- [ ] Test all mappers
- [ ] Update documentation

---


## 🎯 The Golden Rules for Mappers

### Rule 1: Mappers are Stateless Translators

Mappers only translate between data structures:
- Request DTO → Entity
- Entity → Response DTO
- Entity → Summary DTO

### Rule 2: Mappers Have NO Dependencies (Except Other Mappers)

```java
// ✅ CORRECT
@Component
public class UserMapper {
    // No dependencies - pure mapping
}

// ✅ CORRECT
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final OrganizationMapper organizationMapper; // ✅ OK - another mapper
}

// ❌ WRONG
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final UserRepository userRepository; // ❌ NO!
    private final OrganizationRepository orgRepository; // ❌ NO!
    private final UserDomainService userDomainService; // ❌ NO!
}
```

### Rule 3: Mappers Do NOT Query Databases

```java
// ❌ WRONG
public User toEntity(CreateUserRequest request) {
    User user = new User();
    // ... set fields
    
    // ❌ Database query in mapper
    Organization org = organizationRepository.findById(request.organizationId())
        .orElseThrow();
    user.setOrganization(org);
    
    return user;
}

// ✅ CORRECT - Application service loads entities
public User toEntity(CreateUserRequest request) {
    User user = new User();
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEmail(request.email());
    // Don't set organization - application service will do it
    return user;
}
```

### Rule 4: Mappers Do NOT Contain Business Logic

```java
// ❌ WRONG
public User toEntity(CreateUserRequest request) {
    User user = new User();
    user.setFirstName(request.firstName());
    
    // ❌ Business logic doesn't belong here
    if (request.firstName().length() < 2) {
        throw new IllegalArgumentException("Name too short");
    }
    
    return user;
}

// ✅ CORRECT - Keep it pure mapping
public User toEntity(CreateUserRequest request) {
    User user = new User();
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEmail(request.email());
    return user;
}
```

---

## Summary

### ✅ MapStruct With Domain Service Architecture

**MapStruct Mappers:**
- ✅ Simple field mapping (request → entity, entity → response)
- ✅ Use shared configuration via `@MapperConfig`
- ✅ Use base mapper for common utility methods
- ✅ Ignore fields that application service handles
- ❌ NO repository dependencies
- ❌ NO database queries
- ❌ NO business validation

**Application Services:**
- ✅ Use MapStruct for field mapping
- ✅ Load and set relationships
- ✅ Set default values
- ✅ Validate uniqueness (infrastructure)
- ✅ Delegate business logic to domain services

**Domain Services:**
- ✅ Pure business logic
- ✅ Can be used in MapStruct for read-only calculations
- ❌ Should not be used for state modifications in mappers

**The Perfect Flow:**

```java
@Transactional
public ResponseDTO createEntity(RequestDTO request) {
    // 1. Infrastructure: Validate & load
    validateUniqueness(request);
    RelatedEntity related = loadRelated(request);
    
    // 2. ✅ MapStruct: Simple mapping
    Entity entity = mapper.toEntity(request);
    
    // 3. Application: Set relationships
    entity.setRelated(related);
    entity.setStatus(INITIAL);
    
    // 4. Domain: Validate business rules
    domainService.validate(entity);
    
    // 5. Infrastructure: Persist
    Entity saved = repository.save(entity);
    
    // 6. ✅ MapStruct: Map to response
    return mapper.toResponse(saved);
}
```

This keeps your architecture clean, leverages MapStruct for what it does best (field mapping), and maintains proper separation of concerns!
