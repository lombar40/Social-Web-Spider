package edu.unlv.cs.socialwebspider.web;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;

/**
 * Change password validation. Checks if the entered data is acceptable.
 * 
 * @author Ryan
 *
 */
@Service("changePasswordValidator")
public class ChangePasswordValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePasswordForm.class.equals(clazz);
	}
	
	/**
	 * Validates the entered passwords to ensure that the current password is valid, and that the new passwords entered match
	 */
	@Override
	public void validate(Object target, Errors errors) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal();	// Stores the authenticated user's details
		ChangePasswordForm form = (ChangePasswordForm) target;	// Stores the changepasswordform
		String oldPassword = form.getOldPassword();				// Stores the old password
		String newPassword = form.getNewPassword();				// Stores the new password
		String newPasswordRepeat = form.getNewPasswordRepeat();	// Stores the new password repeated
		
		// Checks if the old password provided matches the authenticated user existing password, throws an error if not
		if (!oldPassword.equals(authUser.getPassword())) {
			errors.rejectValue("oldPassword", "changepassword_currentpasswordnomatch");
		}
		
		// Checks if the provided new passwords match, throws an error if not
		if (!newPassword.equals(newPasswordRepeat)) {
			errors.rejectValue("newPasswordRepeat", "changepassword_repeatpasswordnomatch");
		}
	}
}