package examples.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springinfra.model.domain.BaseDomain;

@Getter
@Setter
@Entity
@Table(name = "credential_role")
@NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CredentialRole extends BaseDomain<Integer> {

    @ManyToOne(targetEntity = Credential.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private Credential credential;

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
