package examples.service;

import examples.assets.UserStatus;
import examples.model.dto.CreateUserRequest;
import examples.model.mapper.UserMapper;
import examples.model.view.UserView;
import examples.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.exception.EmailAlreadyExistException;
import org.springinfra.model.dto.PropertyBagDto;
import org.springinfra.service.BaseService;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserView save(CreateUserRequest request) {
        checkEmailUniqueness(request.email());
        var user = this.userMapper.toEntity(request);
        user.setStatus(UserStatus.ACTIVE);
        user = this.userRepository.save(user);
        return this.userMapper.toView(user);
    }

    public UserView patch(UUID pId, PropertyBagDto propertyBagDto) {
        var user = this.userRepository.findByPublicId(pId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The ID [{0}] does not exist", pId)));

        this.userMapper.patchEntity(user, propertyBagDto.getProperties());
        user = this.userRepository.save(user);
        return this.userMapper.toView(user);
    }

    public void delete(UUID pId) {
        this.userRepository.deleteByPublicId(pId);
    }

    public UserView find(UUID pId) {
        var user = this.userRepository.findByPublicId(pId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The ID [{0}] does not exist", pId)));
        return this.userMapper.toView(user);
    }

    public List<UserView> list(Pageable pageable) {
        var users = this.userRepository.findAll(pageable);
        return this.userMapper.toViews(users.getContent());
    }

    private void checkEmailUniqueness(String email) {
        if (this.userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new EmailAlreadyExistException();
        }
    }
}
