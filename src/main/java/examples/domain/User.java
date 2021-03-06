package examples.domain;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import examples.assets.UserStatus;
import personal.project.springinfra.model.domain.LogicalDeletableDomain;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Where(clause = "deleted=false")
@NoArgsConstructor
@Audited
public class User extends LogicalDeletableDomain<Long> {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private UserStatus status;

    //region Getter and Setter
    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "status")
    @Convert(converter = UserStatus.Converter.class)
    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
    //endregion
}
