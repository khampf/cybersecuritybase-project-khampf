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
    
    @Bean
    public PlaintextPasswordEncoder passwordEncoder(){
        return new PlaintextPasswordEncoder();
    }

/*    @Bean
    public Md5PasswordEncoder passwordEncoder(){
        return new Md5PasswordEncoder();
    }
*/

/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  */  

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // no real security at the moment
        // http
                // .anyRequest().permitAll();

        // decide who can see registrations
        // http.headers().frameOptions().sameOrigin();
 
//        http.csrf().disable();
//        http.headers().frameOptions().disable();
        http
            .authorizeRequests()
                .antMatchers("/", "/form", "/done", "/console/**", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/view").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/view").hasAuthority("ADMIN")
                .antMatchers("/users").hasAuthority("ADMIN")
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
