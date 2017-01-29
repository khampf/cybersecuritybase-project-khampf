package sec.project.controller;

import java.security.Principal;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import sec.project.domain.Role;
import sec.project.domain.Signup;
import sec.project.domain.User;
import sec.project.repository.RoleRepository;
import sec.project.repository.SignupRepository;
import sec.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@Transactional
public class SignupController {
    
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

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        signupRepository.save(new Signup(name, address));
        return "done";
    }
    
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String loadLists(Model model) {
        // initialize with some signups if the repository is empty
/*        if (signupRepository.count() == 0) {
            signupRepository.save(new Signup("Donald Duck", "donald.duck@disney.com"));
            signupRepository.save(new Signup("Donald Trump", "donald.trump@whitehouse.gov"));
        } */
        model.addAttribute("signups", signupRepository.findAll());
        return "view";
    }

    /* edit signups list */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editLists(Model model) {
        model.addAttribute("signups", signupRepository.findAll());
        // but what about a native query?
        return "edit";
    }
    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.POST)
    public String remove(@PathVariable Long itemId, Model model) {
        logger.info("deleting user with id=" + itemId);
        signupRepository.delete(itemId);
        model.addAttribute("signups", signupRepository.findAll());
        return "edit";
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

    /*
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req) {
        req.setAttribute("logout","1");
        return "login";
    }
*/
    // Exception handling methods

    // Total control - setup a model and return the view name yourself. Or
    // consider subclassing ExceptionHandlerExceptionResolver (see below).
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
      // logger.error("Request: " + req.getRequestURL() + " raised " + ex);

      ModelAndView mav = new ModelAndView();
      mav.addObject("exception", ex);
      mav.addObject("url", req.getRequestURL());
      mav.setViewName("error");
      return mav;
    }    
}

