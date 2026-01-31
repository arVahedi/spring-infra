package examples.domain;

import examples.assets.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.envers.Audited;
import org.springinfra.model.domain.BaseDomain;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Audited
@Getter
@Setter
@SoftDelete
public class User extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "users_id_seq",
            allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private UserStatus status;
}
