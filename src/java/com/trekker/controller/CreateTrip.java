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
    
    @EJB
    private UserService userService;
    
    @PostConstruct
    public void init() {
        trip = new Trip();
        user = userService.currentUser();
    }
    
    public void submit() throws IOException {
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
}
