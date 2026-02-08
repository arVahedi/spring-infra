package examples.service;

import examples.model.dto.request.CreateRoleRequest;
import examples.model.dto.request.UpdateRoleRequest;
import examples.model.entity.Role;
import examples.model.mapper.RoleMapper;
import examples.model.dto.view.RoleView;
import examples.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.exception.UniqueConstraintAlreadyExistsException;
import org.springinfra.service.BaseService;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService extends BaseService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleView save(CreateRoleRequest request) {
        checkRoleNameUniqueness(request.name(), null);
        var role = this.roleMapper.toEntity(request);
        role = this.roleRepository.save(role);
        return this.roleMapper.toView(role);
    }

    public RoleView update(UUID pId, UpdateRoleRequest request) {
        checkRoleNameUniqueness(request.name(), pId);
        var role = this.roleRepository.findByPublicId(pId).orElseThrow(EntityNotFoundException::new);
        this.roleMapper.updateEntity(role, request);
        role = this.roleRepository.save(role);
        return this.roleMapper.toView(role);
    }

    private void checkRoleNameUniqueness(String name, UUID excluded) {
        Optional<Role> existingRole = this.roleRepository.findFirstByName(name);
        if (existingRole.isPresent() && (excluded == null || !excluded.equals(existingRole.get().getPublicId()))) {
            throw new UniqueConstraintAlreadyExistsException(MessageFormat.format("The role with the same name {0} is already exists", name));
        }
    }

    public void delete(UUID pId) {
        this.roleRepository.deleteByPublicId(pId);
    }

    public RoleView find(UUID pId) {
        var role = this.roleRepository.findByPublicId(pId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The role [{0}] does not exist", pId)));
        return this.roleMapper.toView(role);
    }

    public List<RoleView> list(Pageable pageable) {
        var roles = this.roleRepository.findAll(pageable);
        return this.roleMapper.toViews(roles.getContent());
    }
}
