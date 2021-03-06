package edu.unlv.cs.socialwebspider.web;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.Document;
import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.provider.DatabaseAuthenticationProvider;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * Controller that controls the documents.
 * 
 * @author Ernesto
 *
 */
@RooWebScaffold(path = "documents", formBackingObject = Document.class, update=false)
@RequestMapping("/documents")
@Controller
public class DocumentController {

	@InitBinder
    protected void 	initBinder(HttpServletRequest request, 
    				ServletRequestDataBinder binder)
					throws ServletException {
		// Convert multipart object to byte[]
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value="savedoc",  method = RequestMethod.POST)
    public String createdoc(@Valid Document document,
    					 	BindingResult result, 
    					 	Model model,
    					 	@RequestParam("content") MultipartFile content,
    					 	HttpServletRequest request) {
    	
    	org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user

    	document.setContentType(content.getContentType());
    	document.setFilename(content.getOriginalFilename());
    	document.setSize(content.getSize());
    	document.setOwner(authUser.getUsername());

        if (result.hasErrors()) {
            model.addAttribute("document", document);
            return "documents/create";
        }
        document.persist();
        
        return "redirect:/documents?page=1&size=10" + encodeUrlPathSegment(document.getId().toString(), request);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
    	Document doc = Document.findDocument(id);
    	doc.setUrl("http://localhost:8080/socialwebspider/documents/showdoc/"+id);
        model.addAttribute("document", Document.findDocument(id));
        model.addAttribute("itemId", id);
        return "documents/show";
    }
    
  
    @RequestMapping(value = "/showdoc/{id}", method = RequestMethod.GET)
    public String showdoc(	@PathVariable("id") Long id,
    						HttpServletResponse response,
    						Model model) {
    	
    	Document doc = Document.findDocument(id);
    	

        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +doc.getFilename()+ "\"");

            OutputStream out = response.getOutputStream();
            response.setContentType(doc.getContentType());

            IOUtils.copy( new ByteArrayInputStream(doc.getContent()) , out);
            IOUtils.closeQuietly(out);
         
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
    	Document document = Document.findDocument(id);
    	document.setUrl("http://localhost:8080/doctemplus/documents/showdoc/"+id);
        model.addAttribute("document", document);
        return "documents/update";
    }
    
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
    	TypedQuery<Document> documentQuery = Document.findDocumentsByOwnerEquals(authUser.getUsername());			// Stores the query for only the authenticated user's messages
    	List<Document> documentList = documentQuery.getResultList();																		// Stores the list of the authenticated users's messages
        
    	// If specific results are specified set and display only those
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();							// Set the number of entries to show
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;	// Set the first result to start from
            int lastResult = firstResult + sizeNo;										// Set the last result to show
            
            // If the last result to show is out of range, set it to the max of the list
            if(lastResult > documentList.size() - 1)
            	lastResult = documentList.size();
            
            List<Document> documentSubList = documentList.subList(firstResult, lastResult);			// Retrieve the requested sublist of messages
            uiModel.addAttribute("documents", documentSubList);								// Send the list to the modeler
            float nrOfPages = (float) documentList.size() / sizeNo;								// Set the number of pages
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));	// Model the number of pages
            return "documents/list";														// Return the list of messages
        } 
        
        // If no specific size/pages were specified show all.
        uiModel.addAttribute("documents", documentList);						// Add the messages to the model
        return "documents/list";										// Return the list of messages
    }
    
    // Disable viweing of other's categories
    void populateEditForm(Model uiModel, Document document) {
    	org.springframework.security.core.userdetails.User authUser = DatabaseAuthenticationProvider.getPrincipal(); 	// Stores the authenticated user
    	TypedQuery<User> userQuery = User.findUsersByUsername(authUser.getUsername());
    	User owner = userQuery.getSingleResult();
    	TypedQuery<Category> categoryQuery = Category.findCategorysByOwner(owner);
    	List<Category> categories = categoryQuery.getResultList();
        uiModel.addAttribute("document", document);
        uiModel.addAttribute("categorys", categories);
    }
}