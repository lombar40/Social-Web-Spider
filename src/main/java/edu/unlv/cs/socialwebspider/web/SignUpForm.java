package edu.unlv.cs.socialwebspider.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The form the user fills out to sign up
 * 
 * @author Ryan
 *
 */
public class SignUpForm {

	@NotNull
	@Size(min = 1)
	private String username;	// Stores the username
	
	@NotNull
	@Size(min = 1)
	private String password;	// Stores the password
	
	@NotNull
	@Size(min = 1)
	private String passwordRepeat;	// Stores the repeated password
	
	@NotNull
	@Size(min = 1)
	private String emailAddress;	// Stores the email address
	
	// Needed getters/setters
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordRepeat() {
		return passwordRepeat;
	}
	
	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
