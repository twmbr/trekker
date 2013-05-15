package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.service.TripService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@RequestScoped
public class SearchTrips {

    private List<Trip> trips;
    private String keyword;
    private String location;
    @EJB
    private TripService service;

    @PostConstruct
    public void init() {
        if (trips == null) {
            trips = service.list();
        }
        Collections.sort(trips, new Comparator<Trip>() {
            public int compare(Trip t1, Trip t2) {
                return t2.getStartDate().compareTo(t1.getStartDate());
            }
        });
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void search() {
        List<Trip> resultSet = new ArrayList<Trip>();
        
        if ((!keyword.isEmpty()) && (!location.isEmpty())) {
            List<Trip> tripsByLocation = service.findByLocation(location);
            List<Trip> tripsByKeyword = service.findByKeyword(keyword);
            
            for (Trip t : tripsByLocation) {
                for (Trip t2 : tripsByKeyword) {
                    if (t2.getId() == t.getId()) {
                        resultSet.add(t2);
                    }
                }
            }
            
            trips = resultSet;
        } else if (!keyword.isEmpty()) {
            trips = service.findByKeyword(keyword);
        } else if (!location.isEmpty()) {
            trips = service.findByLocation(location);
        }
        Collections.sort(trips, new Comparator<Trip>() {
            public int compare(Trip t1, Trip t2) {
                return t2.getStartDate().compareTo(t1.getStartDate());
            }
        });
    }
}
