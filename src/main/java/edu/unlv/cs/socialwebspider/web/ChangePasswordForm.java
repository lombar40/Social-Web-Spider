package edu.unlv.cs.socialwebspider.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangePasswordForm {

	@NotNull
	@Size(min = 1)
	private String oldPassword;		// Stores the form's old password
	
	@NotNull
	@Size(min = 1)
	private String newPassword;		// Stores the form's new password
	
	@NotNull
	@Size(min = 1)
	private String newPasswordRepeat;	// Stores the form's new password repeated

	// Needed getters/setters
	public String getOldPassword()
	{
		return this.oldPassword;
	}
	
	public void setOldPassword(String oldPassword)
	{
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword()
	{
		return this.newPassword;
	}
	
	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}
	
	public String getNewPasswordRepeat()
	{
		return this.newPasswordRepeat;
	}
	
	public void setNewPasswordRepeat(String newPasswordRepeat)
	{
		this.newPasswordRepeat = newPasswordRepeat;
	}	
}
