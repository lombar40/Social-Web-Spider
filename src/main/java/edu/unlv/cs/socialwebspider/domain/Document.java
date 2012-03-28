package edu.unlv.cs.socialwebspider.domain;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJpaActiveRecord(finders = { "findDocumentsByCategoryAndOwner", "findDocumentsByOwnerEquals" })
public class Document {

    final int MAX_SIZE = 4000000;

    @Size(min = 1)
    private String owner;

    @ManyToOne
    private Category category;

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 500)
    private String description;

    private String filename;

    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    private String contentType;

    private Long size;

    @Transient
    @Size(max = 100)
    private String url;

    public void checkSize() throws SizeException {
        if (size > MAX_SIZE) throw new SizeException();
    }
}
