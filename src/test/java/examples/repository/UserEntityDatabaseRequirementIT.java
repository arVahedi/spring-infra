package examples.repository;

import annotation.IntegrationTest;
import examples.assets.UserStatus;
import examples.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

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

        // Assert 1: row still exists physically
        Integer rows = this.jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE id = ?",
                Integer.class,
                id
        );
        assertThat(rows).isEqualTo(1);

        // Assert 2: deleted flag was updated
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

        // Assert 1: row still exists physically
        Long id = user.getId();

        // Assert 2: deleted flag was updated
        Integer statusId = this.jdbc.queryForObject(
                "SELECT status FROM users WHERE id = ?",
                Integer.class,
                id
        );
        assertThat(statusId).isEqualTo(UserStatus.ACTIVE.getCode());
    }
}
