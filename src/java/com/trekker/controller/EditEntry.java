package com.trekker.controller;

import com.trekker.model.Itinerary;
import com.trekker.model.Trip;
import com.trekker.service.ItineraryService;
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

@ManagedBean
@RequestScoped
public class EditEntry {

    @EJB
    private TripService tripService;
    @EJB
    private ItineraryService itineraryService;
    private Itinerary itinerary;
    private Trip trip;
    @ManagedProperty(value = "#{param.id}")
    private int entryId;

    @PostConstruct
    public void init() {
        itinerary = itineraryService.find(entryId);
        trip = itinerary.getTripId();
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

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
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

    public void submit() throws IOException {
        itineraryService.update(itinerary);
        tripService.update(trip);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Itinerary successfully updated</div>");
        Faces.redirect("trip.xhtml?id=%s", Integer.toString(trip.getId()));
    }
}
