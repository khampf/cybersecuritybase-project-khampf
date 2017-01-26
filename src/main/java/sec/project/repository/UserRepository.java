package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sec.project.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    public User findByName(String name);
}
