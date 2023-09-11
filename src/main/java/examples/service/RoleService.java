package examples.service;

import examples.domain.Role;
import examples.dto.crud.RoleDto;
import examples.mapper.RoleMapper;
import examples.repository.RoleRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springinfra.service.BaseService;
import springinfra.service.DefaultCrudService;

@Service
@RequiredArgsConstructor
public class RoleService extends BaseService implements DefaultCrudService<Integer, Role, RoleDto> {

    @Getter
    private final RoleRepository repository;
    @Getter
    private final RoleMapper mapper;

}
