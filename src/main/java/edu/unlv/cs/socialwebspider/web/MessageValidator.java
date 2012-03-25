package edu.unlv.cs.socialwebspider.web;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import edu.unlv.cs.socialwebspider.domain.Message;
import edu.unlv.cs.socialwebspider.domain.User;

@Service("messageValidator")
public class MessageValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Message.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Message message = (Message) target;
		String messageTo = message.getMessageTo();
		TypedQuery<User> usernameQuery = User.findUsersByUsername(messageTo);
		List<User> results = usernameQuery.getResultList();
		
		if (message.getMessageTo().equals(message.getMessageFrom()))
			errors.rejectValue("messageTo", "sendmessage_senttoself");
			
		if (results.size() < 1)
			errors.rejectValue("messageTo", "sendmessage_usernamenoexist");
	}
}

