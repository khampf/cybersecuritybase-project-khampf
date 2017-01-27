package sec.project.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import sec.project.domain.Role;

@Entity
@Table(name = "user")
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
        this.roles = new HashSet<>();
    }
    
    public User(String name, String password, Role role) {
        this();
        this.name = name;
        this.password = password;
        this.roles = new HashSet<>();
        this.roles.add(role);        
    }
    
    public User(String name, String password, Set<Role> roles) {
        this();
        this.name = name;
        this.password = password;
        this.roles = roles;
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

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public void addRole(Role role) {
        this.roles.add(role);
    }
    
    @Override
    public String toString() {
        return "id=" + this.id + " name=" + this.name + " roles=" + this.roles.toString();
    }
}