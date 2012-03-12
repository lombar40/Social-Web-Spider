package edu.unlv.cs.socialwebspider.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Profile {
	
	private String username;	// Stores the user's username
	
    private String displayName;	// Stores the user's display name

    private Boolean visibility;	// Stores the privacy of the user. True = Public; False = Private; Default = True

    private String avatarURL;	// Stores the user's avatar URL

    private String about;		// Stores the about me section of the user
}
