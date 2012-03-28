// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.socialwebspider.domain;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Category_Roo_Finder {
    
    public static TypedQuery<Category> Category.findCategorysByOwner(User owner) {
        if (owner == null) throw new IllegalArgumentException("The owner argument is required");
        EntityManager em = Category.entityManager();
        TypedQuery<Category> q = em.createQuery("SELECT o FROM Category AS o WHERE o.owner = :owner", Category.class);
        q.setParameter("owner", owner);
        return q;
    }
    
}