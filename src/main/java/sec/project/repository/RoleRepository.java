package sec.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sec.project.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Autowired
    public Role findByRolename(String rolename);
}
