/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.domain;

import java.util.Set;
import javax.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "rolename")
public class RoleName extends AbstractPersistable<Long> {
    
    @Id
    @GeneratedValue
    private Long Id;

    private String rolename;
    
    @OneToMany(mappedBy = "roleName", cascade = CascadeType.ALL)
    private Set<Role> roles;
    
    // Constructors
    public RoleName() {
        super();
    }
    
    public RoleName(String rolename) {
        this.rolename = rolename;
    }
    
    // Getters and setters
    public String getName() {
        return rolename;
    }
    
    public void setName(String rolename) {
        this.rolename = rolename;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
}
