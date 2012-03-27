package edu.unlv.cs.socialwebspider.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@RooJavaBean
@RooToString
@RooJpaEntity
@RooJpaActiveRecord
public class Document {
	final int MAX_SIZE = 4000000; // 4MB limit
	
	// The id of the document in the database
	private int documentId;

	// Log errors in the controller
	private static final Log log = LogFactory.getLog(Document.class);

	@Size(max = 200)
	private String owner;
	
	@Size(max = 200)
	private String category; 
	
    @NotNull
    @Size(max = 30)
    private java.lang.String name;

    @NotNull
    @Size(max = 500)
    private java.lang.String description;

    private java.lang.String filename;

    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    private java.lang.String contentType;

    private java.lang.Long size;
    
    @Transient
    @Size(max = 100)
    private String url ;
    
    
    public void checkSize()  throws SizeException
    {
    	if(size > MAX_SIZE)
    		throw new SizeException();
    }

}
