package com.trekker.service;

import com.trekker.model.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.shiro.SecurityUtils;

@Stateless
public class UserService {
    @PersistenceContext
    private EntityManager em;
    
    public User find(Integer id) {
        return em.find(User.class, id);
    }
    
    public User find(String email, String password) {
        List<User> found = em.createNamedQuery("User.find", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();
        return found.isEmpty() ? null : found.get(0);
    }
    
    @Produces
    @Named("users")
    public List<User> list() {
        return em.createNamedQuery("User.list", User.class).getResultList();
    }
    
    @Produces
    @Named("currentUser")
    public User currentUser() {
        Object email = SecurityUtils.getSubject().getPrincipal();
        List<User> found = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getResultList();
        return found.isEmpty() ? null : found.get(0);
    }
    
    public Integer create(User user) {
        em.persist(user);
        return user.getId();
    }
    
    public void update(User user) {
        em.merge(user);
    }
    
    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }
}
