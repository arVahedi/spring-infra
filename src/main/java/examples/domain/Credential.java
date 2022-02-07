package examples.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.crypto.password.PasswordEncoder;
import springinfra.SpringContext;
import springinfra.model.domain.BaseDomain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "credential")
public class Credential extends BaseDomain<Long> {
    private String username;
    private String password;
    private User user;

    private List<CredentialRole> roles;

    public void changePassword(String password) {
        this.password = SpringContext.getApplicationContext().getBean(PasswordEncoder.class).encode(password);
    }

    //region Getter and Setter
    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<CredentialRole> getRoles() {
        return roles;
    }

    public void setRoles(List<CredentialRole> roles) {
        this.roles = roles;
    }
    //endregion
}
