package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.service.TripService;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@RequestScoped
public class TripDetails {
    @ManagedProperty(value="#{param.id}")
    private int id;
    
    private Trip trip;
    
    @EJB
    private TripService service;
    
    @PostConstruct
    public void init() {
        trip = service.find(id);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Trip getTrip() {
        return trip;
    }
}
