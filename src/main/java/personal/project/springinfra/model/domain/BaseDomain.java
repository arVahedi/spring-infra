package personal.project.springinfra.model.domain;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseDomain<I extends Serializable> {

    private I id;
    private Long version;

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

    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    private void setVersion(Long version) {
        this.version = version;
    }
    //endregion

}
