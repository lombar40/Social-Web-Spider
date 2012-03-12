package edu.unlv.cs.socialwebspider.web;

import java.util.Date;
import java.util.Random;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.unlv.cs.socialwebspider.domain.Profile;
import edu.unlv.cs.socialwebspider.domain.User;

@RequestMapping("/signup/**")
@Controller
public class SignUpController {

	@Autowired
	private SignUpValidator validator;	// Sets up a validator
	
	@Autowired
	private transient MailSender mailSender;	// Sets up a mail sender
	
	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;	// Sets up a password encoder
	
	// Sets up a signupform model
	@ModelAttribute("signUpForm")
	public SignUpForm fromBackingObject() {
		return new SignUpForm();
	}
	
	// Shows the user signup form
	@RequestMapping(method = RequestMethod.GET)
	public String createForm(Model model) {
		SignUpForm form = new SignUpForm();
		model.addAttribute("user", form);
		return "signup/index";
	}
	
	// Activates the user
	@RequestMapping(params = "activate", method = RequestMethod.GET)
	public String activateUser(@RequestParam(value = "activate", required = true) String activationKey,@RequestParam(value = "emailAddress", required = true) String emailAddress,Model model) {
		TypedQuery<User> query = User.findUsersByActivationKeyAndEmailAddress(activationKey, emailAddress);
		User User = query.getSingleResult();
		
		if(User != null){
			User.setActivationDate(new Date());
			User.setEnabled(true);
			User.merge();
			return "signup/activated";
		}
		else{
			return "signup/error";
		}
	}
	
	// Attempts creation of user with the supplied user details
	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid SignUpForm form, BindingResult result, Model model, HttpServletRequest request) {
		validator.validate(form, result);	// Validates provided information
		
		// Returns the form with errors found
		if(result.hasErrors()) {
			return createForm(model);
		}

		Random random = new Random(System.currentTimeMillis());	// Generates a random number for use with activation key
		String activationKey = "" + (random.nextInt() * -1);	// Sets the activation key
		
		Profile profile = new Profile();	// Stores profile information
		User user = new User();				// Stores user information

		profile.setVisibility(true);	// Set default visibility to true
		profile.setUsername(form.getUsername());
		profile.persist();				// Add the profile to the database
		
		user.setUsername(form.getUsername());	// Set the username from the form
		user.setActivationDate(null);			// Set the activation date to null until activated
		user.setEmailAddress(form.getEmailAddress());	// Sets the email address from the form
		user.setPassword(messageDigestPasswordEncoder.encodePassword(form.getPassword(), null));	// Encodes and sets the password from the form
		user.setActivationKey(activationKey);	// Sets the activation key
		user.setEnabled(false);		// Sets the user to disabled
		user.setAdmin(false);		// Sets default admin to false
		user.setProfile(profile);	// Binds the profile to the user
		user.persist();				// Add the user to the database
		
		
		// Send e-mail to user with the activation link
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmailAddress());
		mail.setSubject("Social Web Spider User Activation");
		mail.setText("Hi " + user.getUsername() + ",\nThank you for registering with us. Please click on this link to activate your account\nhttp://localhost:8080/socialwebspider/signup?emailAddress=" + user.getEmailAddress() + "&activate=" + activationKey + "\nThanks, Social Web Spider");
		mailSender.send(mail);
		
		return "signup/thanks";			
	}
}
