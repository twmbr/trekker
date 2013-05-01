package com.trekker.service;

import com.trekker.model.Media;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MediaService {
    @PersistenceContext
    private EntityManager em;
    
    public Media find(Integer id) {
        return em.find(Media.class, id);
    }
        
    public Integer create(Media media) {
        em.persist(media);
        return media.getId();
    }
    
    public void update(Media media) {
        em.merge(media);
    }
    
    public void delete(Media media) {
        em.remove(em.contains(media) ? media : em.merge(media));
    }
}
