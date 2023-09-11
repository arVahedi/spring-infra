package springinfra.configuration;


import org.mapstruct.*;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.request.BaseCrudDto;

@MapperConfig(
        componentModel = "spring",
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG
)
public interface GlobalMapperConfig extends BaseConfig {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "insertDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    BaseDomain<?> anyCrudDtoToDomainObjectConfiguration(BaseCrudDto<?, ?> dto);
}
