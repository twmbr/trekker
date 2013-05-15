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
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import java.io.Serializable;
import java.util.Collections;

@Named
@SessionScoped
public class CreateTrip implements Serializable {
    private Trip trip;
    private User user;
    
    @EJB
    private TripService tripService;
    
    @EJB
    private UserService userService;
    
    private int startYear;
    private int startMonth;
    private int startDay;
    
    private int addIndex = 0;
    
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
        addIndex = 0;
        
        
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
       String[] tempString = new String[2];
       for (int i = 0; i < waypoints1.size(); i ++)
       {
           tempString = waypoints1.get(i).split("/");
           temp = temp + tempString[1] + ";";
       }
    }
     
     public void storeLocation() {   
      addIndex = addIndex+1;
      waypoints1.add(waypoints1.size(),addIndex +"/"+ tempStore);
      setWaypoints();
    }
     
     public void removeWayPoints(){
         ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for(int j = 0;  j < waypoints1.size();j++)
        {
            for (int i = 0; i < selectedItems.size();i++)
            {
                if(selectedItems.get(i).equalsIgnoreCase(waypoints1.get(j)))
                {
                    tempArray.add(j);
                }
            }
        }
            for (int k = tempArray.size()-1; k >= 0;k--)
            {
                    int j = tempArray.get(k);                  
                    waypoints1.remove(j);
            }
     }
     
      public void moveUpWayPoints(){
         ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for(int j = 0;  j < waypoints1.size();j++)
        {
            for (int i = 0; i < selectedItems.size();i++)
            {
                if(selectedItems.get(i).equalsIgnoreCase(waypoints1.get(j)))
                {
                    tempArray.add(j);
                }
            }
        }
        
        int startIndex = 0;
        boolean check = true;
        for (int i = 0;  i < tempArray.size(); i++)
        {
            if(tempArray.get(i) != i)
            {
                startIndex = i;
                check = false;
            }
            if(check == false)
            {
                i = tempArray.size();
            }
        }
       for(int i = startIndex; i < tempArray.size(); i ++)
       {
          if(tempArray.get(startIndex) != 0)  
          {
                Collections.swap(waypoints1, tempArray.get(i)-1, tempArray.get(i));
          }          
       }
     }
      
      public void moveDownWayPoints(){
         ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for(int j = 0;  j < waypoints1.size();j++)
        {
            for (int i = 0; i < selectedItems.size();i++)
            {
                if(selectedItems.get(i).equalsIgnoreCase(waypoints1.get(j)))
                {
                    tempArray.add(j);
                }
            }
        }
        
        int startIndex = 0;
        boolean check = true;
        int place = waypoints1.size()-1;
        for (int i = tempArray.size()-1;  i >= 0; i--)
        {
            if(tempArray.get(i) != place)
            {
                startIndex = i;
                check = false;
            }
            if(check == false)
            {
                i = -1;
            }
            place--;
        }
       for(int i = startIndex; i >= 0; i --)
       {
          if(tempArray.get(startIndex) != waypoints1.size()-1)  
          {
                Collections.swap(waypoints1, tempArray.get(i)+1, tempArray.get(i));
          }          
       }
     }
}