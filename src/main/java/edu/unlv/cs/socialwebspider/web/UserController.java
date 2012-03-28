package edu.unlv.cs.socialwebspider.web;

import edu.unlv.cs.socialwebspider.domain.User;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Manages the users.
 * 
 * @author Ryan
 *
 */
@RooWebScaffold(path = "users", formBackingObject = User.class)
@RequestMapping("/users")
@Controller
public class UserController {
	
}
