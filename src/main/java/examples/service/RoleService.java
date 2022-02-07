package examples.service;

import examples.converter.RoleCrudConverter;
import examples.domain.Role;
import examples.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

@Service
@RequiredArgsConstructor
public class RoleService extends BaseService implements DefaultCrudService<Role, Integer> {

    private final RoleRepository roleRepository;
    private final RoleCrudConverter roleCrudConverter;

    @Override
    public RoleRepository getRepository() {
        return this.roleRepository;
    }

    @Override
    public RoleCrudConverter getCrudConverter() {
        return this.roleCrudConverter;
    }

    @Override
    public Class<Role> getGenericDomainClass() {
        return Role.class;
    }
}
