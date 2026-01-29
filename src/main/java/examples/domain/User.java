package examples.domain;

import examples.assets.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;
import org.springinfra.model.domain.LogicalDeletableDomain;

@Entity
@Table(name = "users")
@SQLRestriction("deleted = false")
@NoArgsConstructor
@Audited
@Getter
@Setter
public class User extends LogicalDeletableDomain<Long> {
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
    @Convert(converter = UserStatus.Converter.class)
    private UserStatus status;
}
