package examples.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import springinfra.model.domain.BaseDomain;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credential")
@Getter
@Setter
public class Credential extends BaseDomain<Long> {
    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CredentialRole> roles = new ArrayList<>();
}
