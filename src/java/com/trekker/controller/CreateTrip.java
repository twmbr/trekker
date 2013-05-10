package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.model.User;
import com.trekker.service.TripService;
import com.trekker.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import java.io.Serializable;

@Named
@SessionScoped
public class CreateTrip implements Serializable {
    private Trip trip;
    private User user;
    
    private int startYear;
    private int startMonth;
    private int startDay;
    
    private int endYear;
    private int endMonth;
    private int endDay;
    private String temp = "";
    private String tempStore = "";
    private ArrayList<String> selectedItems = new ArrayList<String>();
    private ArrayList<String> waypoints1 = new ArrayList<String>();

    public ArrayList<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(ArrayList<String> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getTempStore() {
        return tempStore;
    }

    public void setTempStore(String tempStore) {
        this.tempStore = tempStore;
    }

    public ArrayList<String> getWaypoints1() {
        return waypoints1;
    }

    public void setWaypoints1(ArrayList<String> waypoints1) {
        this.waypoints1 = waypoints1;
    }
    
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
        this.setWaypoints();
        trip.setOwner(user);
        Collection<Trip> trips = user.getTrips();
        trips.add(trip);
        user.setTrips(trips);
        userService.update(user);
        trip = new Trip();
        startMonth = -1;
        endMonth = -1;
        startDay =0;
        endDay=0;
        startYear=0;
        endYear=0;
        temp = "";
        tempStore = "";
        waypoints1 = new ArrayList<String>();
        
        
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
    
     
     
     public void setWaypoints() {  
       temp = "";
       for (int i = 0; i < waypoints1.size(); i ++)
       {
           temp = temp + waypoints1.get(i) + ";";
       }
    }
     
     public void storeLocation() {   
      waypoints1.add(waypoints1.size(),tempStore);
      setWaypoints();
    }
     
     public void removeWayPoints(){
         ArrayList<String> tempArray = new ArrayList<String>();
        for (int i = 0; i < selectedItems.size();i++)
        {
            tempArray.add(selectedItems.get(i));
        }
        int tempSize = waypoints1.size();
        for (int j = tempSize-1;j >= 0; j--)
        {
            for (int k = 0; k < tempArray.size();k++)
            {
                if(waypoints1.get(j).equals(tempArray.get(k)))
                {
                    waypoints1.remove(k);
                }
            }
        }
     }
}