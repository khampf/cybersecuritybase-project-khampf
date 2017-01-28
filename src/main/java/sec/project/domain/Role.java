package sec.project.domain;

import java.util.Set;
import javax.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "role")
public class Role extends AbstractPersistable<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @ManyToOne
    @JoinColumn(name = "RoleName_ID")
    private RoleName roleName;

    @OneToMany(mappedBy = "User", cascade = CascadeType.ALL)
    private Set<User> users;

     // Constructors
    public Role() {
        super();
    }
    
    public Role(RoleName roleName) {
        this.roleName = roleName;
    }
    
    // Getters and setters
    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
    
/*    public String getName() {
        return roleName.getName();
    }
    
    public void setName(String rolename) {
        this.roleName.setName(rolename);
    }*/
/*    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
*/
}