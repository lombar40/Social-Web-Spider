package edu.unlv.cs.socialwebspider.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findSpidersByOwner" })
public class Spider {

    @ManyToOne
    private User owner;

    private String status;

    @NotNull
    @Size(min = 1)
    private String url;
    
    @ManyToOne
    private Category category;
}
