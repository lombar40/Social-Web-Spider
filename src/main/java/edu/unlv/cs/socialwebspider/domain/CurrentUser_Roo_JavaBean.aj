// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.socialwebspider.domain;

import edu.unlv.cs.socialwebspider.domain.AccessRecord;
import edu.unlv.cs.socialwebspider.domain.CurrentUser;

privileged aspect CurrentUser_Roo_JavaBean {
    
    public Long CurrentUser.getId() {
        return this.Id;
    }
    
    public void CurrentUser.setId(Long Id) {
        this.Id = Id;
    }
    
    public String CurrentUser.getName() {
        return this.name;
    }
    
    public void CurrentUser.setName(String name) {
        this.name = name;
    }
    
    public UserType CurrentUser.getType() {
        return this.type;
    }
    
    public void CurrentUser.setType(UserType type) {
        this.type = type;
    }
    
    public AccessRecord CurrentUser.getAccessRecord() {
        return this.accessRecord;
    }
    
    public void CurrentUser.setAccessRecord(AccessRecord accessRecord) {
        this.accessRecord = accessRecord;
    }
    
}
