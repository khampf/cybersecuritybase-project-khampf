package sec.project.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String role;

/*    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<User> users;
 */   
    // Constructors
    public Role() {
        super();
    }
    
    public Role(String role) {
        this.role = role;
    }
    
    // Getters and setters
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
