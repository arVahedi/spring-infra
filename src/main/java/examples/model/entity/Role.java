package examples.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springinfra.assets.AuthorityType;
import org.springinfra.model.entity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "role")
@NoArgsConstructor
@Audited
@Getter
@Setter
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(
            name = "role_seq",
            sequenceName = "role_id_seq",
            allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "name",  nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = AuthorityType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role_authority")
    @Column(name = "authority")
    private List<AuthorityType> authorities;
}
