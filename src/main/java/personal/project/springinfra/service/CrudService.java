package personal.project.springinfra.service;

import personal.project.springinfra.database.repository.BaseRepository;
import personal.project.springinfra.dto.crud.BaseCrudRequest;
import personal.project.springinfra.model.domain.BaseDomain;

import java.util.List;

public interface CrudService<E extends BaseDomain> {

    E saveOrUpdate(BaseCrudRequest request);

    E delete(long id);

    E find(long id);

    List<E> findAll();

    BaseRepository getRepository();

    Class<E> getGenericDomainClass();
}
