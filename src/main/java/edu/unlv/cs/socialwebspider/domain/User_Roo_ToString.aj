// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.socialwebspider.domain;

import java.lang.String;

privileged aspect User_Roo_ToString {
    
    public String User.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActivationDate: ").append(getActivationDate()).append(", ");
        sb.append("ActivationKey: ").append(getActivationKey()).append(", ");
        sb.append("Admin: ").append(getAdmin()).append(", ");
        sb.append("EmailAddress: ").append(getEmailAddress()).append(", ");
        sb.append("Enabled: ").append(getEnabled()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Password: ").append(getPassword()).append(", ");
        sb.append("Profile: ").append(getProfile()).append(", ");
        sb.append("Username: ").append(getUsername()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
    
}
