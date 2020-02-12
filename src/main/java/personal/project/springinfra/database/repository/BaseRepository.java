package personal.project.springinfra.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.project.springinfra.model.domain.BaseDomain;

public interface BaseRepository<T extends BaseDomain, ID> extends JpaRepository<T, ID> {
}
