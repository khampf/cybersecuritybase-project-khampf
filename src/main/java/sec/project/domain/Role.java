package sec.project.domain;

import java.util.Set;
import javax.persistence.*;

@Entity
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String role;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<User> users;
    
    // Getters and setters
}
