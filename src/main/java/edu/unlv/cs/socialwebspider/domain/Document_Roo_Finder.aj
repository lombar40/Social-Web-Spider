// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.socialwebspider.domain;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.Document;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Document_Roo_Finder {
    
    public static TypedQuery<Document> Document.findDocumentsByCategoryAndOwner(Category category, String owner) {
        if (category == null) throw new IllegalArgumentException("The category argument is required");
        if (owner == null || owner.length() == 0) throw new IllegalArgumentException("The owner argument is required");
        EntityManager em = Document.entityManager();
        TypedQuery<Document> q = em.createQuery("SELECT o FROM Document AS o WHERE o.category = :category AND o.owner = :owner", Document.class);
        q.setParameter("category", category);
        q.setParameter("owner", owner);
        return q;
    }
    
    public static TypedQuery<Document> Document.findDocumentsByOwnerEquals(String owner) {
        if (owner == null || owner.length() == 0) throw new IllegalArgumentException("The owner argument is required");
        EntityManager em = Document.entityManager();
        TypedQuery<Document> q = em.createQuery("SELECT o FROM Document AS o WHERE o.owner = :owner", Document.class);
        q.setParameter("owner", owner);
        return q;
    }
    
}
