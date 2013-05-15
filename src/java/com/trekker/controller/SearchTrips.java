package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.service.TripService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private Date date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void search() {
        List<Trip> resultSet = new ArrayList<Trip>();
        List<Trip> tripsByLocation = new ArrayList<Trip>();
        List<Trip> tripsByKeyword = new ArrayList<Trip>();
        List<Trip> tripsByDate = new ArrayList<Trip>();
        
        if (!location.isEmpty()) tripsByLocation = service.findByLocation(location);
        if (!keyword.isEmpty()) tripsByKeyword = service.findByKeyword(keyword);
        if (date != null) tripsByDate = service.findByDate(date);
        
        if (!location.isEmpty()) {
            if (!keyword.isEmpty()) {
                if (date != null) {
                    for (Trip t : tripsByLocation)
                        for (Trip t2 : tripsByKeyword)
                            for (Trip t3 : tripsByDate) {
                                if ((t.getId() == t2.getId()) && (t.getId() == t3.getId()))
                                    resultSet.add(t);
                            }
                } else {
                    for (Trip t : tripsByLocation)
                        for (Trip t2: tripsByKeyword) {
                            if (t.getId() == t2.getId())
                                resultSet.add(t);
                        }
                }
            } else if (date != null) {
                for (Trip t: tripsByLocation)
                    for (Trip t2: tripsByDate) {
                        if (t.getId() == t2.getId())
                            resultSet.add(t);
                    }
            } else {
                resultSet = tripsByLocation;
            }
        } else if (!keyword.isEmpty()) {
            if (date != null) {
                tripsByDate = service.findByDate(date);
                for (Trip t : tripsByKeyword)
                    for (Trip t2 : tripsByDate) {
                        if (t.getId() == t2.getId())
                            resultSet.add(t);
                    }
            } else {
                resultSet = tripsByKeyword;
            }
        } else if (date != null) {
            resultSet = tripsByDate;
        }
        
        trips = resultSet;
        
        Collections.sort(trips, new Comparator<Trip>() {
            public int compare(Trip t1, Trip t2) {
                return t2.getStartDate().compareTo(t1.getStartDate());
            }
        });
    }
}
