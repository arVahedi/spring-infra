package examples.service;

import annotation.IntegrationTest;
import examples.assets.UserStatus;
import examples.model.dto.request.CreateUserRequest;
import examples.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenSavingUser_thenEmailIsNormalizedToLowerCase() {
        CreateUserRequest request = new CreateUserRequest(
                "John",
                "Doe",
                "John.Doe@Example.com",
                "123-456-7890",
                UserStatus.ACTIVE
        );

        var view = this.userService.save(request);

        assertThat(view.getEmail()).isEqualTo("john.doe@example.com");
        var stored = this.userRepository.findByPublicId(view.getPublicId()).orElseThrow();
        assertThat(stored.getEmail()).isEqualTo("john.doe@example.com");
    }
}
