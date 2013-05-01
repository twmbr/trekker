package com.trekker.service;

import com.trekker.model.Trip;
import com.trekker.model.User;
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
}
