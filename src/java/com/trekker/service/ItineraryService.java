package com.trekker.service;

import com.trekker.model.Itinerary;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ItineraryService {
    @PersistenceContext
    private EntityManager em;
    
    public Itinerary find(Integer id) {
        return em.find(Itinerary.class, id);
    }
        
    public Integer create(Itinerary itinerary) {
        em.persist(itinerary);
        return itinerary.getId();
    }
    
    public void update(Itinerary itinerary) {
        em.merge(itinerary);
    }
    
    public void delete(Itinerary itinerary) {
        em.remove(em.contains(itinerary) ? itinerary : em.merge(itinerary));
    }
}