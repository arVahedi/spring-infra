package springinfra.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import springinfra.model.domain.BaseDomain;

@NoRepositoryBean
public interface BaseRepository<E extends BaseDomain, ID> extends JpaRepository<E, ID> {

    void detach(E entity);
}
