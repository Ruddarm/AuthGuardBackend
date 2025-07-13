package authgaurd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID);
    String id;

}
