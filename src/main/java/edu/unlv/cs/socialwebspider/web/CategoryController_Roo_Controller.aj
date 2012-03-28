// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.socialwebspider.web;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.User;
import edu.unlv.cs.socialwebspider.web.CategoryController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect CategoryController_Roo_Controller {
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CategoryController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Category());
        return "categorys/create";
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CategoryController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Category.findCategory(id));
        return "categorys/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CategoryController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Category category = Category.findCategory(id);
        category.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/categorys";
    }
    
    void CategoryController.populateEditForm(Model uiModel, Category category) {
        uiModel.addAttribute("category", category);
        uiModel.addAttribute("users", User.findAllUsers());
    }
    
    String CategoryController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}