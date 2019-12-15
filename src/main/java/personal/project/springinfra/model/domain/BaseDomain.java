package personal.project.springinfra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseDomain<I extends Serializable> implements Persistable<I> {

    private I id;
    private Long version;

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return null == this.id;
    }

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

    public void setVersion(Long version) {
        this.version = version;
    }
    //endregion

}
