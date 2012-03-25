package edu.unlv.cs.socialwebspider.web;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import edu.unlv.cs.socialwebspider.domain.Message;
import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/messages")
@Controller
@RooWebScaffold(path = "messages", formBackingObject = Message.class)
public class MessageController {
	
	@Autowired
	private MessageValidator validator;	// Sets up a validator
	
	// Modify the list
	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); // Stores the authenticated user
    	TypedQuery<Message> query = Message.findMessagesByMessageToEquals(authUser.getUsername());	// Stores the query for only the authenticated user's messages
    	List<Message> list = query.getResultList();	// Stores the list of the authenticated users's messages
    	
    	// If specific results are specified set and display only those
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();	// Set the number of entries to show
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;	// Set the first result to start from
            int lastResult = firstResult + sizeNo;	// Set the last result to show
            
            // If the last result to show is out of range, set it to the max of the list
            if(lastResult > list.size() - 1)
            	lastResult = list.size();
            
            List<Message> messageList = list.subList(firstResult, lastResult);	// Retrieve the requested sublist of messages
            uiModel.addAttribute("messages", messageList);	// Send the list to the modeler
            float nrOfPages = (float) list.size() / sizeNo;		// Set the number of pages
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));	// Model the number of pages
        } 
        // If no specific size/pages were specified show all. (THIS ELSE WAS PRESUPPLIED BY SPRING! :P)
        else 
            uiModel.addAttribute("messages", query.getResultList());
        
        addDateTimeFormatPatterns(uiModel);	// Format the date and time for the message dates
        return "messages/list";	// Return the list of messages
    }
	
	// Modify creation
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(Message message, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); // Stores the authenticated user
		String messageFrom = authUser.getUsername();	// Stores the authenticated user's username
		
		

		
		TypedQuery<User> query = User.findUsersByUsername(messageFrom);	// Stores the query of messages for the authenticated user
		
		User messageOwner = null;
		
		// If the sending user is the root admin, create a User entry for them
		if(messageFrom.equals("admin"))
		{
			messageFrom = "Root Admin";
			messageOwner = new User();
			messageOwner.setActivationDate(new Date());
			messageOwner.setActivationKey("");
			messageOwner.setAdmin(true);
			messageOwner.setEmailAddress("");
			messageOwner.setEnabled(true);
			messageOwner.setId(1L);
			messageOwner.setPassword("");
			messageOwner.setProfile(null);
			messageOwner.setUsername("Root Admin");
			messageOwner.setVersion(0);
		}
		
		// If the user wasn't a root admin, set the user entry to their user entry
		if(messageOwner == null)
			messageOwner = query.getSingleResult();	// Gets the authenticated user's user object
		
		message.setMessageFrom(messageFrom);			// Sets the message from the authenticated user
		message.setOwner(messageOwner);					// Sets the owner of the message to the authenticated user's user object
		message.setMessageDate(new Date());				// Sets the message date to now
		
		validator.validate(message, bindingResult);		// Validates the entered information
		
		// If errors found, return the errors with the form
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, message);
            return "messages/create";
        }
        
        // If successful clear the model, save the data, and forward the user to a success page
        uiModel.asMap().clear();
        message.persist();
        return "messages/thanks";
        //return "redirect:/messages/" + encodeUrlPathSegment(message.getId().toString(), httpServletRequest);
    }
	
	// Modify the edit form
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Message());
        return "messages/create";
    }
	
	// Disable viewing of messages by other users
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); // Stores the authenticated user
		Message message = Message.findMessage(id);	// Stores the message found the user is trying to view
		
		// Check if the authorizes user's username matches the 'messageto' field in the message, if not deny user access to the message
		if(!authUser.getUsername().equals(message.getMessageTo()))
			return "messages/list";
		
		// Model the message for the autorized user
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("message", message);
        uiModel.addAttribute("itemId", id);
        return "messages/show";
    }
	
	// Disable modification of existing messages
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        return "messages/list";
    }
}
