/**
 * 
 */
package edu.unlv.cs.socialwebspider.provider;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import edu.unlv.cs.socialwebspider.domain.User;

@Service("databaseAuthenticationProvider")
public class DatabaseAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {

	private String adminUser;		// Stores the root admin username
	private String adminPassword;	// Stores the root admin password

	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;	// Sets up a password encryptor

	// Sets the root admin's username
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	
	// Sets the root admin's password
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	// Returns the currently authenticated user details
	public static org.springframework.security.core.userdetails.User getPrincipal() {
		  return (org.springframework.security.core.userdetails.User)((SecurityContext)SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
	}
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0,
			UsernamePasswordAuthenticationToken arg1)
			throws AuthenticationException {
		return;
	}

	
	// Retrives and sets a user login
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		String password = (String) authentication.getCredentials();		// Stores the attempted authentication password
		
		// If no password entered, throw error
	    if (! StringUtils.hasText(password)) {
	    	throw new BadCredentialsException("Please enter password");
	    }
	    
	    String encryptedPassword = messageDigestPasswordEncoder.encodePassword(password, null); 	// Encrypts the entered password
	    String expectedPassword = null;		// Sets up the expected password
	    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();	// Sets up the authority list
	    
	    //	If entered username is the root admin username, check against the root admin password
	    if (adminUser.equals(username)) {
	    	expectedPassword = adminPassword; 	// Expected root admin password
	      
	    	// If entered root admin password doesn't match, throw error
	    	if (! encryptedPassword.equals(expectedPassword)) {
	    		throw new BadCredentialsException("Invalid password");
	    	}
	      
	    	authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));	// Set root admin privileges
	    } 
	    // Else attempt non root admin user
	    else {
	    	// Try to find provided user
	    	try {
		    	TypedQuery<User> query= User.findUsersByUsername(username);	// Set up query by username
		        User targetUser = (User) query.getSingleResult();	// Query database for provided username
		        expectedPassword = targetUser.getPassword();	// Get expected password from found user
		        
		        // Check and make sure the user's account has been activated
		        if (!targetUser.getEnabled())
		        {
		        	throw new BadCredentialsException("Account not activated");
		        }
		        
		        // If no password is set, throw error
		        if (! StringUtils.hasText(expectedPassword)) {
		        	throw new BadCredentialsException("No password for " + username + " set in database, contact administrator");
		        }
		        
		        // If passwords don't match, throw error
		        if (! encryptedPassword.equals(expectedPassword)) {
		        	throw new BadCredentialsException("Invalid Password");
		        }
		        
		        // If user has the admin flag set, add admin privileges
		        if (targetUser.getAdmin())
		        {
		        	authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
		        }
		        else
		        {
		        	authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
		        }
	    	} 
	    	// Catch errors if user not found
	    	catch (EmptyResultDataAccessException e) {
	    		throw new BadCredentialsException("Invalid user");
	    	} 
	    	catch (EntityNotFoundException e) {
	    		throw new BadCredentialsException("Invalid user");
	    	} 
	    	
	    	// Catch database error (multiple users exist with username)
	    	catch (NonUniqueResultException e) {
	    		throw new BadCredentialsException("Non-unique user, contact administrator");
	    	}
	    }
	    
	    // Return authenticated user details
	    return new org.springframework.security.core.userdetails.User(
	      username,
	      password,
	      true, // enabled 
	      true, // account not expired
	      true, // credentials not expired 
	      true, // account not locked
	      authorities
	    );
	}
}
