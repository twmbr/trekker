package com.trekker.controller;

import com.trekker.model.Accommodation;
import com.trekker.model.Trip;
import com.trekker.service.AccommodationService;
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
public class EditAccommodation {

    @EJB
    private TripService tripService;
    @EJB
    private AccommodationService accommodationService;
    private Accommodation accommodation;
    private Trip trip;
    @ManagedProperty(value = "#{param.id}")
    private int entryId;
    
    private Date checkinDate;
    private int checkinHour;
    private int checkinMinute;
    
    private Date checkoutDate;
    private int checkoutHour;
    private int checkoutMinute;

    @PostConstruct
    public void init() {
        accommodation = accommodationService.find(entryId);
        trip = accommodation.getTripId();
        checkinDate = (Date)accommodation.getCheckinTime().clone();
        checkinDate.setHours(0);
        checkinDate.setMinutes(0);
        checkoutDate = (Date)accommodation.getCheckoutTime().clone();
        checkoutDate.setHours(0);
        checkoutDate.setMinutes(0);
        checkinHour = accommodation.getCheckinTime().getHours();
        checkinMinute = accommodation.getCheckinTime().getMinutes();
        checkoutHour = accommodation.getCheckoutTime().getHours();
        checkoutMinute = accommodation.getCheckoutTime().getMinutes();
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
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

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public int getCheckinHour() {
        return checkinHour;
    }

    public void setCheckinHour(int checkinHour) {
        this.checkinHour = checkinHour;
    }

    public int getCheckinMinute() {
        return checkinMinute;
    }

    public void setCheckinMinute(int checkinMinute) {
        this.checkinMinute = checkinMinute;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public int getCheckoutHour() {
        return checkoutHour;
    }

    public void setCheckoutHour(int checkoutHour) {
        this.checkoutHour = checkoutHour;
    }

    public int getCheckoutMinute() {
        return checkoutMinute;
    }

    public void setCheckoutMinute(int checkoutMinute) {
        this.checkoutMinute = checkoutMinute;
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
        accommodation.setCheckinTime(checkinDate);
        accommodation.setCheckoutTime(checkoutDate);
        accommodation.getCheckinTime().setHours(checkinHour);
        accommodation.getCheckinTime().setMinutes(checkinMinute);
        accommodation.getCheckoutTime().setHours(checkoutHour);
        accommodation.getCheckoutTime().setMinutes(checkoutMinute);
        
        accommodationService.update(accommodation);
        tripService.update(trip);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Accommodation successfully updated</div>");
        Faces.redirect("trip.xhtml?id=%s", Integer.toString(trip.getId()));
    }
}