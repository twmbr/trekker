/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trekker.controller;

import com.trekker.model.Itinerary;
import com.trekker.model.Trip;
import com.trekker.service.TripService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author Lisa
 */
@ManagedBean
@RequestScoped
public class AddEntry {

    @ManagedProperty(value = "#{param.id}")
    private int id;
    private Trip trip;

    private Itinerary itinerary;
    @EJB
    private TripService tripService;

    @PostConstruct
    public void init() {
        itinerary = new Itinerary();
        trip = tripService.find(id);
    }
    
    public void submit() throws IOException {
        itinerary.setTripId(trip);
        trip.getItineraryCollection().add(itinerary);
        tripService.update(trip);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Itinerary successfully updated</div>");
        Faces.redirect("trip.xhtml?id=%s", Integer.toString(id));
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }
    
    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getId() {
        return id;
    }

    public void setId(int initId) {
        id = initId;
    }

    public ArrayList<Date> getEveryday() {
        ArrayList<Date> everyday = new ArrayList<Date>();
        everyday.add(trip.getStartDate());
        Date curDate = trip.getStartDate();
        Calendar cal = Calendar.getInstance();
        while (curDate.compareTo(trip.getEndDate()) == -1) {
            cal.setTime(curDate);
            cal.add(Calendar.DATE, 1);
            curDate = cal.getTime();
            everyday.add(curDate);
        }
        return everyday;
    }
}
