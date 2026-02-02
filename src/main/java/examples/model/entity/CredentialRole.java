package examples.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springinfra.model.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "credential_role")
@NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CredentialRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credential_role_seq")
    @SequenceGenerator(
            name = "credential_role_seq",
            sequenceName = "credential_role_id_seq",
            allocationSize = 50)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = Credential.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private Credential credential;

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
