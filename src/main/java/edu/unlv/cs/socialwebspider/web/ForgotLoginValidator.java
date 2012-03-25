package edu.unlv.cs.socialwebspider.web;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.unlv.cs.socialwebspider.domain.User;

@Service("forgotLoginValidator")
public class ForgotLoginValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ForgotLoginForm form = (ForgotLoginForm) target;	// Forgotlogin form
		String emailAddress = form.getEmailAddress();	// Email address from the form
		TypedQuery<User> query = User.findUsersByEmailAddress(emailAddress);	// Sets up a query to check for the user
		List<User> results;
		
		results = query.getResultList();
		if(results.isEmpty())
		{
			errors.rejectValue("emailAddress", "forgot_login_emailnotfound");
		}
	}
}
