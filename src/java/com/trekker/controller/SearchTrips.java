package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.service.TripService;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@RequestScoped
public class SearchTrips {
    private List<Trip> trips;
    private String keyword;
    
    @EJB
    private TripService service;
    
    @PostConstruct
    public void init() {
        if (trips == null) {
            trips = service.list();
        }
    }
    
    public List<Trip> getTrips() {
        return trips;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public void search() {
        trips = service.findByKeyword(keyword);
    }
}
