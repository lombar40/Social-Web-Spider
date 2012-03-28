package edu.unlv.cs.socialwebspider.web;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.Spider;
import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;
import edu.unlv.cs673.socialwebspider.spider.controller.CrawlerThread;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The controller that manages the creation of spiders
 * 
 * @author Ryan
 */
@RequestMapping("/spiders")
@Controller
@RooWebScaffold(path = "spiders", formBackingObject = Spider.class)
public class SpiderController {
	
	/**
	 * Create a new spider
	 * @param spider Spider information
	 * @param bindingResult Results from the form (errors)
	 * @param uiModel The MVC model
	 * @param httpServletRequest The HTTP servlet request
	 * @return The URL to the jspx
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Spider spider, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());			// Stores the query for the owner of the message
		
		// Check if the form had any errors
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, spider);
            return "spiders/create";
        }
        
        spider.setOwner(userQuery.getSingleResult());	// Set the owner of the spider to the logged in user
        spider.setStatus("Running");					// Set the status of the spider to running
        uiModel.asMap().clear();
        spider.persist();								// Save the spider

        // Start the spider
        new Thread(new CrawlerThread("spider/configs/", "spider/images/", spider.getUrl(), authUser.getUsername(), spider.getCategory(), spider.getId())).start();
        
        return "redirect:/spiders/" + encodeUrlPathSegment(spider.getId().toString(), httpServletRequest);
    }
	
	/**
	 * Lists the user's spiders
	 * @param page Page number
	 * @param size Number of items on the page
	 * @param uiModel The MVC model
	 * @return The URL to the jspx
	 */
	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());	// Stores the user query
		User targetUser = userQuery.getSingleResult();									// Stores the logged in user
    	TypedQuery<Spider> spiderQuery = Spider.findSpidersByOwner(targetUser);			// Stores the query for only the authenticated user's messages
    	List<Spider> spiderList = spiderQuery.getResultList();							// Stores the list of the authenticated users's messages
        
    	// If specific results are specified set and display only those
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();							// Set the number of entries to show
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;	// Set the first result to start from
            int lastResult = firstResult + sizeNo;										// Set the last result to show
            
            // If the last result to show is out of range, set it to the max of the list
            if(lastResult > spiderList.size() - 1)
            	lastResult = spiderList.size();
            
            List<Spider> spiderSubList = spiderList.subList(firstResult, lastResult);			// Retrieve the requested sublist of messages
            uiModel.addAttribute("spiders", spiderSubList);										// Send the list to the modeler
            float nrOfPages = (float) spiderList.size() / sizeNo;								// Set the number of pages
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));	// Model the number of pages
            return "spiders/list";																// Return the list of messages
        } 
        
        // If no specific size/pages were specified show all.
        uiModel.addAttribute("spiders", spiderList);	// Add the messages to the model
        return "spiders/list";							// Return the list of messages
    }
	
	/**
	 * Disables editing of a spider
	 * @param id ID of the spider
	 * @param uiModel The MVC model
	 * @return The URL to the jspx
	 */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        return "index";
    }
	
	/**
	 * Displays the edit form to the user to create a spider.
	 * @param uiModel The MVC model
	 * @param spider The spider to model
	 */
	void populateEditForm(Model uiModel, Spider spider) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
    	TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());
    	User owner = userQuery.getSingleResult();
    	TypedQuery<Category> categoryQuery = Category.findCategorysByOwner(owner);
    	List<Category> categories = categoryQuery.getResultList();
    	
        uiModel.addAttribute("spider", spider);
        uiModel.addAttribute("categorys", categories);
        uiModel.addAttribute("users", User.findAllUsers());
    }
}
