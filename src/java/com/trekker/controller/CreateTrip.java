package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.model.User;
import com.trekker.service.TripService;
import com.trekker.service.UserService;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class CreateTrip {
    private Trip trip;
    private User user;
    
    private int startYear;
    private int startMonth;
    private int startDay;
    
    private int endYear;
    private int endMonth;
    private int endDay;
    
    @EJB
    private TripService tripService;
    
    @EJB
    private UserService userService;
    
    @PostConstruct
    public void init() {
        trip = new Trip();
        user = userService.currentUser();
        
        startMonth = -1;
        endMonth = -1;
    }
    
    public void submit() throws IOException {
        Date startDate = new Date(startYear - 1900, startMonth, startDay);
        Date endDate = new Date(endYear - 1900, endMonth, endDay);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setOwner(user);
        Collection<Trip> trips = user.getTrips();
        trips.add(trip);
        user.setTrips(trips);
        userService.update(user);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Trip successfully created</div>");
        Faces.redirect("profile.xhtml");
    }
    
    public Trip getTrip() {
        return trip;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }
    
    
}
