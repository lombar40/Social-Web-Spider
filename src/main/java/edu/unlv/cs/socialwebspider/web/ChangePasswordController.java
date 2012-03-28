package edu.unlv.cs.socialwebspider.web;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;

/**
 * Controller to control changing of a user's password
 * 
 * @author Ryan
 */
@RequestMapping("/changepassword/**")
@Controller
public class ChangePasswordController {

	@Autowired
	private ChangePasswordValidator validator;	// Sets up a data validator
	
	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;	// Sets up a password encryptor
	
	/**
	 * Models the change password form
	 * 
	 * @return Returns the URL to the jspx
	 */
	@ModelAttribute("changePasswordForm")
	public ChangePasswordForm fromBackingObject() {
		return new ChangePasswordForm();
	}
	
	/**
	 * Shows the change password form
	 * 
	 * @param model 
	 * @return Returns the URL to the jspx
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String createForm(Model model) {
		ChangePasswordForm form = new ChangePasswordForm();
		model.addAttribute("user", form);
		return "changepassword/index";
	}
	
	/**
	 * Attempts to change the user password
	 * 
	 * @param form The form with the info the user submitted
	 * @param result The results of the form (errors)
	 * @param model MVC model
	 * @param request HTTP Request
	 * @return Returns the URL to the jspx
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid ChangePasswordForm form, BindingResult result, Model model, HttpServletRequest request) {
		
		// Runs validation on the results and returns errors if validation fails
		validator.validate(form, result);
		if(result.hasErrors()) {
			return createForm(model);
		}
		
		// Updates the user password
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); // Stores the authenticated user
		TypedQuery<User> query = User.findUsersByUsername(authUser.getUsername());	// Stores the query for the user
		User targetUser = query.getSingleResult();	// Stores the user found with the query
		targetUser.setPassword(messageDigestPasswordEncoder.encodePassword(form.getNewPassword(), null));	// Encrypts and sets the user password
		targetUser.merge();		// Merges the local user with the database user
		return "changepassword/thanks";			
	}
}
