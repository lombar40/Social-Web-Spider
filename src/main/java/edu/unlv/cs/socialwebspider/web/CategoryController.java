package edu.unlv.cs.socialwebspider.web;


import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.Document;
import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This controls the management of the categories. Users can view their own categories and view documents inside the category.
 * @author Ryan
 *
 */
@RequestMapping("/categorys")
@Controller
@RooWebScaffold(path = "categorys", formBackingObject = Category.class)
public class CategoryController {
	
	/**
	 * Shows the user a specific document and links them to the document.
	 * 
	 * @param id ID of the document
	 * @param uiModel MVC model
	 * @return URL to the jspx
	 */
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 						// Stores the authenticated user
		TypedQuery<Document> documentQuery = Document.findDocumentsByCategoryAndOwner(Category.findCategory(id), authUser.getUsername());	// Stores the document query
		List<Document> documentList = documentQuery.getResultList();	// Stores the document list
		
        uiModel.addAttribute("documents", documentList);	// Adds the document list to the model
        uiModel.addAttribute("itemId", id);					// Adds the documentid to the model
        return "categorys/show";
    }
	
	/**
	 * Shows the user a list of their own categories.
	 * 
	 * @param page What page they are on
	 * @param size What number of categories to view
	 * @param uiModel MVC model
	 * @return URL to the jspx
	 */
	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());									// Stores the query for the owner of the message
    	TypedQuery<Category> categoryQuery = Category.findCategorysByOwner(userQuery.getSingleResult());				// Stores the query for only the authenticated user's messages
    	List<Category> categoryList = categoryQuery.getResultList();													// Stores the list of the authenticated users's messages
        
    	// If specific results are specified set and display only those
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();							// Set the number of entries to show
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;	// Set the first result to start from
            int lastResult = firstResult + sizeNo;										// Set the last result to show
            
            // If the last result to show is out of range, set it to the max of the list
            if(lastResult > categoryList.size() - 1)
            	lastResult = categoryList.size();
            
            List<Category> categorySubList = categoryList.subList(firstResult, lastResult);			// Retrieve the requested sublist of messages
            uiModel.addAttribute("categorys", categorySubList);								// Send the list to the modeler
            float nrOfPages = (float) categoryList.size() / sizeNo;								// Set the number of pages
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));	// Model the number of pages
            return "categorys/list";														// Return the list of messages
        } 
        
        // If no specific size/pages were specified show all.
        uiModel.addAttribute("categorys", categoryList);						// Add the messages to the model
        return "categorys/list";										// Return the list of messages
    }
	
	/**
	 * Creation of a category
	 * 
	 * @param category Returned category information from the user
	 * @param bindingResult Results from the form (errors)
	 * @param uiModel The MVC model
	 * @param httpServletRequest HTTP servlet request
	 * @return The URL to the jspx
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Category category, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());									// Stores the query for the owner of the message
		
		// Checks if the form has errors
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, category);
            return "categorys/create";
        }
        
        category.setOwner(userQuery.getSingleResult());	// Sets the owner of the category to the logged in user
        uiModel.asMap().clear();
        category.persist();		// Stores the category
        return "redirect:/categorys/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
    }
	
	/**
	 * Updates the category
	 * 
	 * @param category Category the user is modifying
	 * @param bindingResult Results from the form (errors)
	 * @param uiModel The MVC model
	 * @param httpServletRequest HTTP servlet request
	 * @return The URl to the jspx
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Category category, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());									// Stores the query for the owner of the message
		
		// Check if the form had errors
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, category);
            return "categorys/update";
        }
        
        category.setOwner(userQuery.getSingleResult());	// Set the owner of the file to the logged in user
        uiModel.asMap().clear();
        category.merge();	// Save the category
        return "redirect:/categorys/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);	// Forward the user to the page they've modified
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		Category category = Category.findCategory(id);
		TypedQuery<Document> documentQuery = Document.findDocumentsByCategoryAndOwner(category, authUser.getUsername());
		List<Document> documentList = documentQuery.getResultList();
		if(documentList.size() > 0)
		{
			// Error that there are entries in this database
			uiModel.asMap().clear();
	        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	        return "redirect:/categorys";
		}
		
        category.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/categorys";
    }
}
