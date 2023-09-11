package examples.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import springinfra.assets.AuthorityType;
import springinfra.model.domain.BaseDomain;

import java.util.List;

@Entity
@Table(name = "role")
@NoArgsConstructor
@Audited
@Getter
@Setter
public class Role extends BaseDomain<Integer> {

    @Basic
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = AuthorityType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role_authority")
    @Column(name = "authority")
    private List<AuthorityType> authorities;
}
