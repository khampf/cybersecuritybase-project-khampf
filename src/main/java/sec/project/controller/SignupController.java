package sec.project.controller;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
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

    // KH: show a list of all registered signups
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String loadLists(Model model) {
        // initialize with some signups if the repository is empty
/*        if (signupRepository.count() == 0) {
            signupRepository.save(new Signup("Donald Duck", "donald.duck@disney.com"));
            signupRepository.save(new Signup("Donald Trump", "donald.trump@whitehouse.gov"));
        } */
        // the "normal" way
        model.addAttribute("signups", signupRepository.findAll());
        
        // but what about a native query?
        
        return "list";
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editLists(Model model) {
        model.addAttribute("signups", signupRepository.findAll());
        
        // but what about a native query?
        
        return "edit";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPost() {
        return "login";
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

