package sec.project.config;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sec.project.domain.Role;
import sec.project.domain.User;
import sec.project.repository.UserRepository;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UserRepository userRepository;
    
    public CustomUserDetailsService() {
        super();
    }
    
    @PostConstruct
    public void init() {
        logger.info("Started with " + userRepository.count() + " pre-existing users");
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        assert (userRepository != null);
        logger.info("I have " + userRepository.count() + " user accounts");

        try {
            User user = userRepository.findByUsername(username);
            if (!user.getEnabled()) {
                logger.info("Disabled user=" + user.getUsername() + " tried to log in!");
                throw new UsernameNotFoundException("User not found");
            }
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (Role role : user.getRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
            
    // no users in userdb - add default administrator
    // php -r "echo password_hash('admin', PASSWORD_BCRYPT, ['cost' => 13]) . PHP_EOL;"

    // PasswordEncoder pe = new BCryptPasswordEncoder();
    // System.out.println("TEST: admin = " + pe.encode("admin"));
              
   // ted/president
//          User user = new User("ted", "president");
//          User user = new User("ted", "c8d56be998c94089ea6e1147dc9253c1");
//            User user = new User("ted", "$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS");
//            user.addRole(new Role("USER"));
//            userRepository.save(user);
//        }
}
