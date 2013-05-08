package com.trekker.service;

import com.trekker.model.Accommodation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AccommodationService {
    @PersistenceContext
    private EntityManager em;
    
    public Accommodation find(Integer id) {
        return em.find(Accommodation.class, id);
    }
        
    public Integer create(Accommodation accommodation) {
        em.persist(accommodation);
        return accommodation.getId();
    }
    
    public void update(Accommodation accommodation) {
        em.merge(accommodation);
    }
    
    public void delete(Accommodation accommodation) {
        em.remove(em.contains(accommodation) ? accommodation : em.merge(accommodation));
    }
}
