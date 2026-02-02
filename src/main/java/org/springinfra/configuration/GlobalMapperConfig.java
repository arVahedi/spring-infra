package org.springinfra.configuration;


import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;

/**
 * This is global configuration of mapstruct mappers.
 * <p>
 * For further information take a look at <a href="https://mapstruct.org/documentation/stable/reference/html/#shared-configurations">its official documentation</a>
 */

@MapperConfig(
        componentModel = "spring",
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG
)
public interface GlobalMapperConfig extends BaseConfig {

}
