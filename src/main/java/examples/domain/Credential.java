package examples.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.crypto.password.PasswordEncoder;
import springinfra.SpringContext;
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

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CredentialRole> roles = new ArrayList<>();

    public void changePassword(String password) {
        this.password = SpringContext.getApplicationContext().getBean(PasswordEncoder.class).encode(password);
    }
}
