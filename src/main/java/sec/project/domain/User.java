package sec.project.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
import sec.project.repository.RoleRepository;

@Entity
@Table(name = "user")
public class User extends AbstractPersistable<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private boolean enabled;

//    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))

    //@ManyToMany
    //@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    
    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
    /*
        @ManyToMany
    @JoinTable(
        name = "roles_privileges",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    */
    
    // Constructors
    public User() {
        super();
    }
    
    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
    }
    
/*    public User(String name, String password, Role role) {
        this();
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
        this.roles.add(role);        
    }
    
    public User(String name, String password, Set<Role> roles) {
        this();
        this.username = username;
        this.password = password;
        this.roles = roles;
    } */
    
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public void addRole(Role role) {
        roles.add(role);
    }
    
    public void removeRole(Role role) {
        roles.remove(role);
    }
    
    @Override
    public String toString() {
        return "id=" + this.id + " username=" + this.username + " roles=" + this.roles.toString();
    }
}