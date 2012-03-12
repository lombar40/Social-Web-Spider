package edu.unlv.cs.socialwebspider.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import edu.unlv.cs.socialwebspider.domain.Profile;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findUsersByEmailAddress", "findUsersByActivationKeyAndEmailAddress", "findUsersByUsername" })
public class User {

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String username;	// Stores the user's username

    @NotNull
    @Size(min = 1)
    private String password;	// Stores the users's encrypted password

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String emailAddress;	// Stores the user's email address

    private Boolean enabled;		// Stores the enabled flag for the user

    private String activationKey;	// Stores the activation key for the user

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date activationDate;	// Stores the activation date of the user

    @ManyToOne
    private Profile profile;		// Stores the user's customizable profile

    private Boolean admin;			// Stores the admin flag for the user
}
