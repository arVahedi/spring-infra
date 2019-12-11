package personal.project.springinfra.model.domain;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseDomain<I extends Serializable> {

    private I id;

    //region Getter and Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }
    //endregion

}
