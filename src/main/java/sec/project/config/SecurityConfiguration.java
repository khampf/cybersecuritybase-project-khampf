package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
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

// ISSUE 3: Disable javabean below and enable the next below it instead
//          Remember to also edit imports.sql
    @Bean
    public PlaintextPasswordEncoder passwordEncoder(){
        return new PlaintextPasswordEncoder();
    }
    
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public Md5PasswordEncoder passwordEncoder(){
//        return new Md5PasswordEncoder();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // no real security at first
        // http.anyRequest().permitAll();

// ISSUE #6: Comment out the 3 lines below to enable CSRF-cookies,
//           enable HTTP Strict Transport Security and to set
//           X-Frame-Options to DENY in the headers
        http
            .csrf().disable()
            .headers().frameOptions().disable();

        
        
        // decide who can see registrations
        http
            .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/", "/form", "/done").permitAll()
                .antMatchers("/list").permitAll()
                .antMatchers(HttpMethod.GET, "/view").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/view").hasAuthority("EDIT")
                .antMatchers("/users").hasAuthority("ADMIN")
                .antMatchers("/dumpusers").permitAll()
// ISSUE #7: comment out line above and uncomment line below                
//                .antMatchers("/dumpusers").hasAuthority("ADMIN")
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
