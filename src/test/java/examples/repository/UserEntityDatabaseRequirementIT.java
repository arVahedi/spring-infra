package examples.repository;

import annotation.IntegrationTest;
import examples.assets.UserStatus;
import examples.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@IntegrationTest
class UserEntityDatabaseRequirementIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbc;


    @Test
    void whenDeleteUser_thenPerformSoftDelete() {
        // Arrange
        User user = new User();
        user.setFirstName("Joe");
        user.setLastName("Doe");
        user.setEmail("a@a.com");
        user.setPhone("123-456-7890");
        user.setStatus(UserStatus.ACTIVE);
        //todo: fix this
//        this.userRepository.saveAndFlush(user);
        user = this.userRepository.save(user);
        this.userRepository.flush();

        Long id = user.getId();

        // Act
        this.userRepository.delete(user);
        this.userRepository.flush();      // important: force SQL execution

        // Assert
        Integer rows = this.jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE id = ?",
                Integer.class,
                id
        );
        assertThat(rows).isEqualTo(1);

        Boolean deleted = this.jdbc.queryForObject(
                "SELECT deleted FROM users WHERE id = ?",
                Boolean.class,
                id
        );
        assertThat(deleted).isTrue();

        assertThat(this.userRepository.findById(id)).isEmpty();
        assertThat(this.userRepository.findAll()).extracting(User::getId).doesNotContain(id);
    }

    @Test
    void whenInsertUser_thenCreatedDateMustBeFilled() {
        // Arrange
        User user = new User();
        user.setFirstName("Joe");
        user.setLastName("Doe");
        user.setEmail("a@a.com");
        user.setPhone("123-456-7890");
        user.setStatus(UserStatus.ACTIVE);
        Instant beforeInsertTime = Instant.now();

        // Act
        user = this.userRepository.save(user);
        this.userRepository.flush();

        // Assert
        Long id = user.getId();

        Instant createdDate = this.jdbc.queryForObject(
                "SELECT created_date FROM users WHERE id = ?",
                Instant.class,
                id
        );
        assertThat(createdDate)
                .isNotNull()
                .isAfter(beforeInsertTime);

        Instant lastModifiedDate = this.jdbc.queryForObject(
                "SELECT last_modified_date FROM users WHERE id = ?",
                Instant.class,
                id
        );
        assertThat(lastModifiedDate)
                .isNotNull()
                .isEqualTo(createdDate);
    }

    @Test
    void whenUpdateUser_thenLastModifiedDateMustBeFilled() {
        // Arrange
        User user = new User();
        user.setFirstName("Joe");
        user.setLastName("Doe");
        user.setEmail("a@a.com");
        user.setPhone("123-456-7890");
        user.setStatus(UserStatus.ACTIVE);

        // Act
        user = this.userRepository.save(user);
        this.userRepository.flush();
        user.setFirstName("Updated");
        Instant beforeUpdateTime = Instant.now();
        this.userRepository.save(user);
        this.userRepository.flush();

        // Assert
        Long id = user.getId();
        Instant createdDate = this.jdbc.queryForObject(
                "SELECT created_date FROM users WHERE id = ?",
                Instant.class,
                id
        );

        Instant lastModifiedDate = this.jdbc.queryForObject(
                "SELECT last_modified_date FROM users WHERE id = ?",
                Instant.class,
                id
        );
        assertThat(lastModifiedDate)
                .isNotNull()
                .isAfter(createdDate)
                .isAfter(beforeUpdateTime);
    }

    @Test
    void whenUpsertUser_thenUserStatusMustBeEnumPersistableCode() {
        // Arrange
        User user = new User();
        user.setFirstName("Joe");
        user.setLastName("Doe");
        user.setEmail("a@a.com");
        user.setPhone("123-456-7890");
        user.setStatus(UserStatus.ACTIVE);

        // Act
        user = this.userRepository.save(user);
        this.userRepository.flush();

        // Assert
        Long id = user.getId();

        Integer statusId = this.jdbc.queryForObject(
                "SELECT status FROM users WHERE id = ?",
                Integer.class,
                id
        );
        assertThat(statusId).isEqualTo(UserStatus.ACTIVE.getCode());
    }
}
