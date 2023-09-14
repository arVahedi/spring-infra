package examples.service;

import examples.domain.Role;
import examples.dto.crud.RoleDto;
import examples.mapper.RoleMapper;
import examples.repository.RoleRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springinfra.exception.UniqueConstraintAlreadyExistsException;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService extends BaseService implements DefaultCrudService<Integer, Role, RoleDto> {

    @Getter
    private final RoleRepository repository;
    @Getter
    private final RoleMapper mapper;

    @Override
    public Role save(RoleDto request) {
        checkUniqueRoleName(request.getName(), Optional.empty());
        return DefaultCrudService.super.save(request);
    }

    @Override
    public Role update(Integer id, RoleDto request) {
        checkUniqueRoleName(request.getName(), Optional.of(id));
        return DefaultCrudService.super.update(id, request);
    }

    private void checkUniqueRoleName(String name, Optional<Integer> excluded) {
        Optional<Role> exisingRole = this.repository.findFirstByName(name);
        if (exisingRole.isPresent() && (excluded.isEmpty() || excluded.get().intValue() != exisingRole.get().getId())) {
            throw new UniqueConstraintAlreadyExistsException(MessageFormat.format("The role with the same name {0} is already exists", name));
        }
    }
}
