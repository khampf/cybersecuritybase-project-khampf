package sec.project.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import sec.project.domain.Role;

@Entity
@Table(name = "users")
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // Constructors
    public User() {
        super();
    }
    
    public User(String name, String password) {
        this();
        this.name = name;
        this.password = password;
    }
    
    public User(String name, String password, String role) {
        this();
        this.name = name;
        this.password = password;
        // this.roles.add(new Role(role)); // This fails. Look at docs.
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void addRole(Role role) {
        roles.add(role);
    }
}