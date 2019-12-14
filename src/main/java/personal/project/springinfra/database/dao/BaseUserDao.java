package personal.project.springinfra.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.project.springinfra.model.domain.User;

public interface BaseUserDao extends JpaRepository<User, Long> {
}
