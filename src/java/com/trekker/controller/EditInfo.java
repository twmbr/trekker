package com.trekker.controller;

import com.trekker.model.User;
import com.trekker.service.UserService;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class EditInfo {
    private User user;
    
    private String oldPassword;
    private String newPassword;

    @EJB
    private UserService service;
    
    @PostConstruct
    public void init() {
        user = service.currentUser();
    }
    
    public void submit() throws IOException {
        if (!newPassword.isEmpty()) {
            String oldHashedPassword = new Sha256Hash(oldPassword).toHex();
            String newHashedPassword = new Sha256Hash(newPassword).toHex();
            if (user.getPassword().equals(oldHashedPassword)) {
                user.setPassword(newHashedPassword);
            } else {
                Messages.addGlobalError("<div class=\"alert alert-error\">Old password does not match</div>");
                return;
            }
        }
        service.update(user);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Account successfully updated</div>");
        Faces.redirect("profile.xhtml");
    }
    
    public User getUser() {
        return user;
    }
    
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
