package edu.unlv.cs.socialwebspider.web;

import edu.unlv.cs.socialwebspider.domain.Spider;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/spiders")
@Controller
@RooWebScaffold(path = "spiders", formBackingObject = Spider.class)
public class SpiderController {
}
