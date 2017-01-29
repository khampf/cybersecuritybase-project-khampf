package sec.project.domain;

import java.util.Set;
import javax.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "role")
public class Role extends AbstractPersistable<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> userRoles;
    
     // Constructors
    public Role() {
        super();
    }
    
    public Role(String name) {
        this.name = name;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Set<User> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<User> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role role = (Role) obj;
        if (!role.equals(role.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        // builder.append("Role [name=").append(name).append("]").append("[id=").append(id).append("]");
        // return builder.toString();
        return name;
    }
}