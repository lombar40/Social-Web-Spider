package edu.unlv.cs.socialwebspider.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findUsersByEmailAddress", "findUsersByActivationKeyAndEmailAddress", "findUsersByUsername" })
public class User {

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String username;

    @NotNull
    @Size(min = 1)
    private String password;

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String emailAddress;

    private Boolean enabled;

    private String activationKey;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date activationDate;

    @OneToOne
    private Profile profile;

    private Boolean admin;

    public long getUserIdByUsername() {
        TypedQuery<User> usernameQuery = User.findUsersByUsername(this.username);
        if (usernameQuery.getResultList().isEmpty()) return -1L;
        User user = usernameQuery.getSingleResult();
        return user.getId();
    }
}
