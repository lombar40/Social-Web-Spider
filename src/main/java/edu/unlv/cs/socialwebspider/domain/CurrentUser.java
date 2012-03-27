package edu.unlv.cs.socialwebspider.domain;

import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;


import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class CurrentUser extends User {
	enum UserType { Noob, Elite };	// Apply constraints depending on user type
	
	java.lang.Long Id;
	
	@Size(max = 200)
	String name;
	
	UserType type;
	
	@OneToOne
	AccessRecord accessRecord;
	
	// Add a file to records - adds a pointer to a database object
	void addRecord(String fileId)
	{
		accessRecord.addFileId(fileId);
	}
	
	// Share a specific file (identified by its Id) with another user
	void shareFile(String fileId, String otherUser)
	{
		
		CurrentUser other;
		
		/* Find user by his user Id, once found, add fileId to other user accessRecord. */
		other = (CurrentUser) User.findUsersByUsername(otherUser);
		
		other.addRecord(fileId);
	}

}

