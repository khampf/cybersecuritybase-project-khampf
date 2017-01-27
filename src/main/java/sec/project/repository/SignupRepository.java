package sec.project.repository;

import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sec.project.domain.Signup;

@Repository
@Table(name = "signup")
public interface SignupRepository extends JpaRepository<Signup, Long> {
    @Autowired
    public Signup findByName(String name);
}
