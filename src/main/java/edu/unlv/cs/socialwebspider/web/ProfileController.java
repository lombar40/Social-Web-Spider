package edu.unlv.cs.socialwebspider.web;

import java.util.Collection;

import edu.unlv.cs.socialwebspider.domain.Profile;
import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
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
	
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); // Stores the authenticated user
		String username = authUser.getUsername();	// Stores the authorized user's username
		User user = new User();	// Creates a temp user
		user.setUsername(username);	// Sets the temp user's username to the auth user
		Long authUserId = user.getUserIdByUsername();	// Gets the authorized user's user id
		Collection<GrantedAuthority> authorities = authUser.getAuthorities();
		
		// Check if the authorized user's id matches the one which they are trying to modify
		if(!authUserId.equals(id) && !authorities.contains(new GrantedAuthorityImpl("ROLE_ADMIN")))
			return "index";
		
        populateEditForm(uiModel, Profile.findProfile(id));
        return "profiles/update";
    }
}
