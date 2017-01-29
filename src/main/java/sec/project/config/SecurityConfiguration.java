package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(userDetailsServiceBean()); 
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        
        // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
        
    }

    // Some different passwordEncoders to choose from

/*    
    @Bean
    public PlaintextPasswordEncoder passwordEncoder(){
        return new PlaintextPasswordEncoder();
    }
*/
    
/*
    @Bean
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
        // no real security at first
        // http.anyRequest().permitAll();

        // needed for /h2-console/
        http.csrf().disable();
        http.headers().frameOptions().disable();

        // http.headers().frameOptions().sameOrigin();

        // decide who can see registrations
        http
            .authorizeRequests()
                // .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/", "/form", "/done").permitAll()
                .antMatchers(HttpMethod.GET, "/view").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/view").hasAuthority("EDIT")
                .antMatchers("/users").hasAuthority("ADMIN")
                .antMatchers("/dumpdb").permitAll()
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
