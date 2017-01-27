package sec.project.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sec.project.domain.Role;
import sec.project.domain.User;
import sec.project.repository.UserRepository;

//@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    // private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private UserRepository userRepository;
    // private RoleRepository roleRepository;
    
    public CustomUserDetailsService(UserRepository userRepository) { // , RoleRepository roleRepository){
        this.userRepository = userRepository;
        // this.roleRepository = roleRepository;
    }
    
    private Md5PasswordEncoder passwordEncoder;
    
    // this will be removed
    // private Map<String, String> accountDetails;

    /*
    @PostConstruct
    public void init() {
        // this data would typically be retrieved from a database
        //this.accountDetails = new TreeMap<>();
        // this.accountDetails.put("ted", "$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm");
        //this.accountDetails.put("ted", "$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS"); // president
        
    } */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
/*        if (!this.accountDetails.containsKey(username)) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                username,
                this.accountDetails.get(username),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }*/
        if (userRepository.count() == 0) {
            // no users in userdb - add default administrator
            // php -r "echo password_hash('admin', PASSWORD_BCRYPT, ['cost' => 13]) . PHP_EOL;"
            
            // PasswordEncoder pe = new BCryptPasswordEncoder();
            // System.out.println("TEST: admin = " + pe.encode("admin"));
              
            // ted/president
//          User user = new User("ted", "president");
//          User user = new User("ted", "c8d56be998c94089ea6e1147dc9253c1");
            User user = new User("ted", "$2a$10$nKOFU.4/iK9CqDIlBkmMm.WZxy2XKdUSlImsG8iKsAP57GMcXwLTS");
            user.addRole(new Role("USER"));
            userRepository.save(user);
            
            // admin/admin
//          user = new User("admin", "admin");  // PlaintextPasswordEncoder
//          user = new User("admin", "21232f297a57a5a743894a0e4a801fc3");   // Md5PasswordEncoder
            user = new User("admin", "$2a$10$VlQHfbbJdEH.Q2jcEfufn.e32mGCnKFGXooxS1s6xMEM7u6/3zHr."); // BCryptPasswordEncoder
            
            user.addRole(new Role("USER"));
            user.addRole(new Role("ADMIN"));
            userRepository.save(user);
            
            // User user = new User("admin","admin","USER");
            // userRepository.save(user);
        }

        System.out.println("DEBUG: users = " + userRepository.count());
        
        try {
            User user = userRepository.findByName(username);
            if (user == null) {
                // LOGGER.debug("no user found by that username");
                System.out.println("DEBUG: no user found by username " + username);
                // return null;
                throw new UsernameNotFoundException("User null not found");
            }
            // LOGGER.debug(" user from username " + user.toString());
            System.out.println("DEBUG: user from username " + user.toString() + " with password " + user.getPassword());
            return new org.springframework.security.core.userdetails.User(
                    user.getName(),
                    user.getPassword(),
                    getAuthorities(user)
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
    
    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for(Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            authorities.add(grantedAuthority);
        }
        // LOGGER.debug("user authorities are " + authorities.toString());
        System.out.println("DEBUG: user authorities are " + authorities.toString());
        return authorities;
    }
}
