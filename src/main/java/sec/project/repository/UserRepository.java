package sec.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sec.project.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Autowired
    public User findByUsername(String username);
}
