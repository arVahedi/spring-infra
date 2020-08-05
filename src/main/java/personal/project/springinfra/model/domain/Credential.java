package personal.project.springinfra.model.domain;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;

@Entity
@Table(name = "credential")
public class Credential extends BaseDomain<Long> {
    private String username;
    private String password;
    private String salt;
    private User user;

    public void changePassword(String password) {
        this.salt = RandomStringUtils.randomAlphanumeric(10);
        String clearPassword = makeSaltDirtyPassword(password);
        this.password = DigestUtils.sha256Hex(clearPassword);
    }

    private String makeSaltDirtyPassword(String clearPassword) {
        return this.salt + clearPassword;
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

    @Basic
    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion
}
