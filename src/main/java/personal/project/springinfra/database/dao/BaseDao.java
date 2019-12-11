package personal.project.springinfra.database.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDao {

    @Autowired
    private SessionFactory sessionFactory;

    //region Getter and Setter
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    //endregion
}
