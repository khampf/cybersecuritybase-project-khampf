package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sec.project.domain.User;
import sec.project.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    /* @Autowired
    private UserRepository userRepository; */
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // just enabled this again without really knowing why...
        // auth.userDetailsService(userDetailsServiceBean()); 
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
    }

//    @Override
    //@Autowired
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(userDetailsServiceBean());
        
        // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
 //       auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
 //   }
    
    // @Bean
/*    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new CustomUserDetailsService();
    } */
    
/*    @Bean
    public PlaintextPasswordEncoder passwordEncoder(){
        return new PlaintextPasswordEncoder();
    }
*/

/*    @Bean
    public Md5PasswordEncoder passwordEncoder(){
        return new Md5PasswordEncoder();
    }
*/
  @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // no real security at the moment
        // http
                // .anyRequest().permitAll();

        // decide who can see registrations
        http.csrf().disable();
        // http.headers().frameOptions().sameOrigin();
        http.headers().frameOptions().disable();
 
        http
            .authorizeRequests()
                .antMatchers("/", "/form", "/done", "/console/**", "/h2-console/**").permitAll()
                .antMatchers("/list").hasAuthority("USER")  // possible exploit if single page
                .antMatchers("/edit", "/users").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .and()
            .exceptionHandling().accessDeniedPage("/403");
    }
}
