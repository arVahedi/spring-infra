package examples.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springinfra.model.domain.BaseDomain;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credential")
@Getter
@Setter
public class Credential extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credential_seq")
    @SequenceGenerator(
            name = "credential_seq",
            sequenceName = "credential_id_seq",
            allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    //We've set this because User has a soft delete (@SoftDelete), but Credential doesn't
    private User user;

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CredentialRole> roles = new ArrayList<>();
}
