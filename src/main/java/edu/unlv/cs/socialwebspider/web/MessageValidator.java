package edu.unlv.cs.socialwebspider.web;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import edu.unlv.cs.socialwebspider.domain.Message;
import edu.unlv.cs.socialwebspider.domain.User;

/**
 * Validates a message to ensure the data is acceptable.
 * @author Ryan
 *
 */
@Service("messageValidator")
public class MessageValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Message.class.equals(clazz);
	}

	/**
	 * Validate if the user the message is being sent to exists and check and make sure they aren't sending to themselves.
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Message message = (Message) target;										// Store the message
		String messageTo = message.getMessageTo();								// Store who the message is to
		TypedQuery<User> usernameQuery = User.findUsersByUsername(messageTo);	// Store the query for the username
		List<User> results = usernameQuery.getResultList();						// Store the list of results from the username query
		
		// Check if the message is being sent to self
		if (message.getMessageTo().equals(message.getMessageFrom()))
			errors.rejectValue("messageTo", "sendmessage_senttoself");
		
		// Check if no user exists.
		if (results.size() < 1)
			errors.rejectValue("messageTo", "sendmessage_usernamenoexist");
	}
}

