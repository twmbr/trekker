package com.trekker.service;

import com.trekker.model.Trip;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TripService {

    @PersistenceContext
    private EntityManager em;

    public Trip find(Integer id) {
        return em.find(Trip.class, id);
    }

    public Integer create(Trip trip) {
        em.persist(trip);
        return trip.getId();
    }

    public void update(Trip trip) {
        em.merge(trip);
    }

    public void delete(Trip trip) {
        em.remove(em.contains(trip) ? trip : em.merge(trip));
    }

    @Produces
    @Named("trips")
    public List<Trip> list() {
        return em.createNamedQuery("Trip.list", Trip.class).getResultList();
    }
    
    public List<Trip> findByKeyword(String keyword) {
        return em.createNamedQuery("Trip.findByKeyword", Trip.class)
                .setParameter("name", "%"+keyword+"%")
                .getResultList();
    }
    
    public List<Trip> findByLocation(String location) {
        return em.createNamedQuery("Trip.findByLocation", Trip.class)
                .setParameter("location", "%" + location + "%")
                .getResultList();
    }
    
    public List<Trip> findByDate(Date date) {
        return em.createNamedQuery("Trip.findByDate", Trip.class)
                .setParameter("date", date)
                .getResultList();
    }
}
