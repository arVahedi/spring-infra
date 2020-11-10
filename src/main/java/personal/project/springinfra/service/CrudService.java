package personal.project.springinfra.service;

import personal.project.springinfra.database.repository.BaseRepository;
import personal.project.springinfra.model.converter.BaseCrudConverter;
import personal.project.springinfra.model.domain.BaseDomain;
import personal.project.springinfra.model.dto.crud.request.BaseCrudRequest;

import java.util.List;

public interface CrudService<E extends BaseDomain> {

    E saveOrUpdate(BaseCrudRequest request);

    E delete(long id);

    E find(long id);

    List<E> findAll();

    BaseRepository getRepository();

    BaseCrudConverter getCrudConverter();

    Class<E> getGenericDomainClass();
}
