package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sec.project.domain.Signup;

@Repository
public interface SignupRepository extends JpaRepository<Signup, Long> {

}
