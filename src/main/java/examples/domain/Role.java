package examples.domain;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import springinfra.assets.AuthorityType;
import springinfra.model.domain.BaseDomain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@NoArgsConstructor
@Audited
public class Role extends BaseDomain<Integer> {
    private String name;
    private List<AuthorityType> authorities;

    //region Getter and Setter
    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass=AuthorityType.class, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name="role_authority")
    @Column(name="authority")
    public List<AuthorityType> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityType> authorities) {
        this.authorities = authorities;
    }

    //endregion
}
