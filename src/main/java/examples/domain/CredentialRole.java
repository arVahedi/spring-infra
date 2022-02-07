package examples.domain;

import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import springinfra.model.domain.BaseDomain;

import javax.persistence.*;

@Entity
@Table(name = "credential_role")
@NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CredentialRole extends BaseDomain<Integer> {
    private Credential credential;
    private Role role;

    //region Getter and Setter
    @ManyToOne(targetEntity = Credential.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //endregion
}
