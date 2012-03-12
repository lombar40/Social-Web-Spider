package edu.unlv.cs.socialwebspider.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ForgotLoginForm {

	@NotNull
	@Size(min = 1)
	private String emailAddress;	// Stores the email address
	
	private Boolean resetPassword;	// Should we reset the password

	// Needed getters/setters
	public String getEmailAddress()
	{
		return this.emailAddress;
	}
	
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public Boolean getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(Boolean resetPassword) {
		this.resetPassword = resetPassword;
	}
}
