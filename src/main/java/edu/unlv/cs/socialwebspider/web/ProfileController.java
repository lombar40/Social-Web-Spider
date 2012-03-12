package edu.unlv.cs.socialwebspider.web;

import edu.unlv.cs.socialwebspider.domain.Profile;
import edu.unlv.cs.socialwebspider.domain.User;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;


@RooWebScaffold(path = "profiles", formBackingObject = Profile.class)
@RequestMapping("/profiles")
@Controller
public class ProfileController {
	
	// Customized user profile viewer which allows retrieval of user details
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("profile", Profile.findProfile(id));	// Enables access to the user profile details
        uiModel.addAttribute("itemId", id);		// Enables access of profile ID
        uiModel.addAttribute("user", User.findUser(id));	// Enables access to user details
        return "profiles/show";		// Return page
    }
}
