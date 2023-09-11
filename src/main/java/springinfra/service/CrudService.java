package springinfra.service;

import org.springframework.data.domain.Pageable;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.request.BaseCrudDto;

import java.util.List;

public interface CrudService<I extends Number, E extends BaseDomain<I>, D extends BaseCrudDto<E, I>> {

    E save(D request);

    E update(I id, D request);

    E delete(I id);

    E find(I id);

    List<E> list();

    List<E> list(Pageable pageable);

}
