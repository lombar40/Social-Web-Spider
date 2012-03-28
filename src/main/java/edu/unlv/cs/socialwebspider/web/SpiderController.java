package edu.unlv.cs.socialwebspider.web;

import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.Document;
import edu.unlv.cs.socialwebspider.domain.Spider;
import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;
import edu.unlv.cs673.socialwebspider.spider.controller.BinarySizes;
import edu.unlv.cs673.socialwebspider.spider.controller.CrawlerImagesOnlyControllerImpl;
import edu.unlv.cs673.socialwebspider.uuid.UUIDFactoryImpl;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/spiders")
@Controller
@RooWebScaffold(path = "spiders", formBackingObject = Spider.class)
public class SpiderController {
	
	// Set owner to current user
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Spider spider, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());			// Stores the query for the owner of the message
		
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, spider);
            return "spiders/create";
        }
        spider.setOwner(userQuery.getSingleResult());
        spider.setStatus("Running");
//        UUIDFactoryImpl UUIDFactory = new UUIDFactoryImpl();
//		String myUUID = UUIDFactory.generateUUID();
//		String configFolder = "spider/configs/" + myUUID;
//		String storageFolder = "spider/images/" + myUUID;
//
//		System.out.println("configFolder: " + configFolder);
//		System.out.println("storageFolder: " + storageFolder);
//
//		CrawlerImagesOnlyControllerImpl crawlController = new CrawlerImagesOnlyControllerImpl();
//		try {
//			crawlController.startSpider(authUser.getUsername(), spider.getCategory().getId().intValue(), configFolder, 1, storageFolder, 1, 20, spider.getUrl(), BinarySizes.TWENTY_KB);
//			spider.setStatus("Completed");
//      } catch (Exception e) {
//			fail("Exception occurred during crawling.");
//			assert(false);
//		}
		
        uiModel.asMap().clear();
        spider.persist();
        return "redirect:/spiders/" + encodeUrlPathSegment(spider.getId().toString(), httpServletRequest);
    }
	
	// List only user's spider
	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
		TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());
		User targetUser = userQuery.getSingleResult();
    	TypedQuery<Spider> spiderQuery = Spider.findSpidersByOwner(targetUser);			// Stores the query for only the authenticated user's messages
    	List<Spider> spiderList = spiderQuery.getResultList();																		// Stores the list of the authenticated users's messages
        
    	// If specific results are specified set and display only those
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();							// Set the number of entries to show
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;	// Set the first result to start from
            int lastResult = firstResult + sizeNo;										// Set the last result to show
            
            // If the last result to show is out of range, set it to the max of the list
            if(lastResult > spiderList.size() - 1)
            	lastResult = spiderList.size();
            
            List<Spider> spiderSubList = spiderList.subList(firstResult, lastResult);			// Retrieve the requested sublist of messages
            uiModel.addAttribute("spiders", spiderSubList);								// Send the list to the modeler
            float nrOfPages = (float) spiderList.size() / sizeNo;								// Set the number of pages
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));	// Model the number of pages
            return "spiders/list";														// Return the list of messages
        } 
        
        // If no specific size/pages were specified show all.
        uiModel.addAttribute("spiders", spiderList);						// Add the messages to the model
        return "spiders/list";		
    }
	
	// Disable editing of spider
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        return "index";
    }
	
	// Disabled viewing of other's categoires when picking the category to spider to 
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
