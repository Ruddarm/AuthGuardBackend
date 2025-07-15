package authgaurd.authgaurd.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String userId;
    @Column(nullable=false)
    private String username;

    @ManyToOne
    @JoinColumn(nullable=false,name="appId")
    private AppEntity app;
}
