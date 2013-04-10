package com.trekker.controller;

import com.trekker.model.User;
import com.trekker.service.UserService;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class Signup {
    private User user;
    
    @EJB
    private UserService service;
    
    @PostConstruct
    public void init() {
        user = new User();
    }
    
    public void submit() throws IOException {
        try {
            user.setPassword(new Sha256Hash(user.getPassword()).toHex());
            service.create(user);
            Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Account successfully created</div>");
            Faces.redirect("index.xhtml");
        } catch (RuntimeException e) {
            Messages.addGlobalError("Registration failed: {0}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    public User getUser() {
        return user;
    }
}
