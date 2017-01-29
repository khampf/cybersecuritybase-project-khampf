package sec.project.controller;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sec.project.domain.Signup;
import sec.project.domain.User;
import sec.project.repository.RoleRepository;
import sec.project.repository.SignupRepository;
import sec.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.apache.catalina.Context;
import static org.h2.server.web.PageParser.escapeHtml;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

@Controller
@Transactional
public class DefaultController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SignupRepository signupRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @RequestMapping("*")
    public String defaultMapping(final Principal principal) {
        if (principal == null) return "redirect:/form";
        return "redirect:/view";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "403";
    }

    // signup form
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        
        try {
            // Open connection
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:mydb","sa","");
            

// ISSUE #1: Unsafe query comment out the 3 lines below and uncomment next block of code after
            String query = "INSERT INTO Signup (name, address) VALUES ('" + name + "', '" + address + "')";
            logger.info(query);
            connection.createStatement().execute(query);
          
//// ISSUE #1: Safer query by using parameterized queries
//            String query = "INSERT INTO Signup (name, address) VALUES (?, ?)";
//            PreparedStatement pstmt = connection.prepareStatement( query );
//            pstmt.setString( 1, name);
//            pstmt.setString( 2, address);
//            pstmt.execute();
//            logger.info(pstmt.toString());
//            pstmt.close();

            connection.close();
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
                
// ISSUE #1: even safer code by using built in repository code
//           comment out the whole try { } catch {} block above and uncomment
//           the following two lines
//        logger.info("signupRepository.save(" + name + ", " + address + ")");
//        signupRepository.save(new Signup(name, address));

        return "done";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public String loadLists(Model model) {
    
        StringBuilder content = new StringBuilder();
        try {
            // Open connection
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:mydb","sa","");
            String name, address;
            Set<Signup> signups;

            String query = "SELECT * FROM Signup";
            logger.info(query);
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            model.addAttribute("signups", resultSet);

            // Do something with the results
            while (resultSet.next()) {
                name = resultSet.getString("name");
                address = resultSet.getString("address");

                content.append(name + " &lt;" + address + "&gt;<br/>\n");
// ISSUE #3: comment out line above and uncomment line below for escaping HTML in String
//                content.append(escapeHtml(name) + " &lt;" + escapeHtml(address) + "&gt;<br/>\n");
            }
            connection.close();
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        content.append("\n<a href=\"/\">Return home</a>\n");
        return content.toString();
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewSignups(Model model) {
        model.addAttribute("signups", signupRepository.findAll());
        return "view";
    }

    @RequestMapping(value = "/view/{itemId}", method = RequestMethod.DELETE)
    public String remove(@PathVariable Long itemId, Model model) {
        logger.info("deleting signup with id=" + itemId);
        signupRepository.delete(itemId);
        return "redirect:/view?edit";
    }
    
    /* user login */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        // if there are no users add admin/admin with USER and ADMIN authorities
        if (userRepository.count() == 0) {
            User user = new User("admin", "$2a$10$VlQHfbbJdEH.Q2jcEfufn.e32mGCnKFGXooxS1s6xMEM7u6/3zHr.");
            user.setEnabled(true);
            user.addRole(roleRepository.findByName("USER"));
            user.addRole(roleRepository.findByName("ADMIN"));
            userRepository.save(user);
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPost() {
        return "login";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
    
    @RequestMapping(value = "/dumpusers", method = RequestMethod.GET)
    @ResponseBody
    public String dumpdb() {
        String query = "SELECT * FROM user";
        ResultSet resultSet;
        StringBuilder result = new StringBuilder();
        
        result.append("<pre>");
        try {
            // Open connection
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:mydb","sa","");

            // Query DB
            resultSet = connection.createStatement().executeQuery(query);
            
            // Do something with the results
            while (resultSet.next()) {
                result.append("id=" + resultSet.getString("id"));
                result.append("\t username=" + resultSet.getString("username"));
                result.append("\t password=" + resultSet.getString("password"));
                result.append("\n");
            }
            resultSet.close();
            connection.close();
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        result.append("</pre>");
// Used to quicly output encoded password        
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        result.append(passwordEncoder.encode("disabled"));
        return result.toString();
    }
    // Exception handling methods

    // Total control - setup a model and return the view name yourself. Or
    // consider subclassing ExceptionHandlerExceptionResolver (see below).
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        logger.error("Request: " + req.getRequestURL() + " raised " + ex);
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }    
// ISSUE #5 START: comment out from here to fix issue #5
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
// ISSUE #4: session cookie value JSESSION is limited to values 00-FF. Change 1 to at least 8
                context.getManager().getSessionIdGenerator().setSessionIdLength(1);
                context.setUseHttpOnly(false);
            }
        };
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setTomcatContextCustomizers(Arrays.asList(new TomcatContextCustomizer[]{tomcatContextCustomizer()}));
        return factory;
    }
// ISSUE #5 END
}

