package springinfra.service;

import org.springframework.data.domain.Pageable;
import springinfra.database.repository.BaseRepository;
import springinfra.model.converter.BaseCrudConverter;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.request.BaseCrudRequest;

import java.util.List;

public interface CrudService<E extends BaseDomain, I extends Number> {

    E saveOrUpdate(BaseCrudRequest request);

    E delete(I id);

    E find(I id);

    List<E> findAll();

    List<E> findAll(Pageable pageable);

    BaseRepository getRepository();

    BaseCrudConverter getCrudConverter();

    Class<E> getGenericDomainClass();
}
