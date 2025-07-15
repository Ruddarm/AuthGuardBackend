package authgaurd.authgaurd.model.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String appId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "clientId")
    private ClientEntity client;

    @OneToOne
    @JoinColumn(name = "apiKeyId")
    private ApiKeyEntity apiKeyEntity;

    @OneToMany(mappedBy = "app")
    private List<UserEntity> user;
}
