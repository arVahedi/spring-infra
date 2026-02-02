package examples.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springinfra.model.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credential")
@Getter
@Setter
public class Credential extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credential_seq")
    @SequenceGenerator(
            name = "credential_seq",
            sequenceName = "credential_id_seq",
            allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "username", nullable = false, updatable = false)
    private String username;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    //We've set this because User has a soft delete (@SoftDelete), but Credential doesn't
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CredentialRole> roles = new ArrayList<>();
}
