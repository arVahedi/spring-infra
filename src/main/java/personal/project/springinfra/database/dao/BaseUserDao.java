package personal.project.springinfra.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.project.springinfra.model.domain.User;

@Repository
public interface BaseUserDao extends JpaRepository<User, Long> {
}
