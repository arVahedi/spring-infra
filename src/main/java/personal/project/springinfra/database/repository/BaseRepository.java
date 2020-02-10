package personal.project.springinfra.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.project.springinfra.model.domain.BaseDomain;

import java.io.Serializable;

public interface BaseRepository<T extends BaseDomain, I extends Serializable> extends JpaRepository<T, I> {
}
