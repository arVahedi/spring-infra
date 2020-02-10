package personal.project.springinfra.service;

import org.springframework.stereotype.Service;
import personal.project.springinfra.database.repository.BaseRepository;
import personal.project.springinfra.model.domain.BaseDomain;

@Service
public interface CrudService<E extends BaseDomain, R extends BaseRepository> {

    R getRepository();


}
