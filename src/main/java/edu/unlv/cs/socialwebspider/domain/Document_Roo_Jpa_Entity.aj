// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.socialwebspider.domain;

import edu.unlv.cs.socialwebspider.domain.Document;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Document_Roo_Jpa_Entity {
    
    declare @type: Document: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Document.id;
    
    @Version
    @Column(name = "version")
    private Integer Document.version;
    
    public Long Document.getId() {
        return this.id;
    }
    
    public void Document.setId(Long id) {
        this.id = id;
    }
    
    public Integer Document.getVersion() {
        return this.version;
    }
    
    public void Document.setVersion(Integer version) {
        this.version = version;
    }
    
}