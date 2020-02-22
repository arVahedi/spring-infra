package personal.project.springinfra.service;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.project.springinfra.dto.crud.BaseCrudRequest;
import personal.project.springinfra.model.domain.BaseDomain;

import java.util.List;

public interface CrudService<E extends BaseDomain> {

    E saveOrUpdate(BaseCrudRequest request);

    void delete(long id);

    E find(long id);

    List<E> findAll();

    JpaRepository getRepository();

    Class<E> getGenericDomainClass();
}
