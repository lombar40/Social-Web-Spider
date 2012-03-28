package edu.unlv.cs.socialwebspider.web;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.unlv.cs.socialwebspider.domain.User;

/**
 * Validates that the info entered by the user for their new account is valid.
 * 
 * @author Ryan
 *
 */
@Service("signUpValidator")
public class SignUpValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return SignUpForm.class.equals(clazz);
	}

	/**
	 * Validates the information the user provided. Checks if the username/email elready exists and checks if the passwords match
	 */
	@Override
	public void validate(Object target, Errors errors) {
		SignUpForm form = (SignUpForm) target;													// Signup form
		String password = form.getPassword();													// Stores password from signup form
		String repeatPassword = form.getPasswordRepeat();										// Stores repeated password from signup form
		TypedQuery<User> usernameQuery = User.findUsersByUsername(form.getUsername());			// Sets up a query to check for existing username
		TypedQuery<User> emailQuery = User.findUsersByEmailAddress(form.getEmailAddress());		// Sets up a query to check for existing email
		List<User> results;
		
		// Queries the database for the an existing user with provided username, returns an error if so
		results = usernameQuery.getResultList();
		if (!results.isEmpty()) {
			errors.rejectValue("username", "signup_usernameexists");
		}
		
		// Queries the database for the an existing user with provided email, returns an error if so
		results = emailQuery.getResultList();
		if (!results.isEmpty()) {
			errors.rejectValue("emailAddress", "signup_emailaddressexists");
		}

		// Checks if the passwords provided match, returns an error if not
		if (!password.equals(repeatPassword)) {
			errors.rejectValue("passwordRepeat", "signup_repeatpasswordnomatch");
		}
	}
}
