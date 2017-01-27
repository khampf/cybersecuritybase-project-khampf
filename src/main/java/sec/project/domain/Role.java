package sec.project.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

/*    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<User> users;
 */   
    // Constructors
    public Role() {
        super();
    }
    
    public Role(String name) {
        this.name = name;
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
