package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.model.User;
import com.trekker.service.TripService;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import com.trekker.service.TripService;
import com.trekker.service.UserService;
import java.util.Collections;
import javax.inject.Named;

@Named
@SessionScoped
public class EditTrip implements Serializable{
    @ManagedProperty(value="#{param.id}")
    private int id;
    private String name ="";
    private String startLoc= "";
        
    private Trip trip;
    private User user;

    @EJB
    private TripService tripService;
    
    @EJB
    private UserService userService;
    
    private String endLoc = "";
    private int addIndex = 0;
    private int priv = 1;
    private int startYear;
    private int startMonth = -1;
    private int startDay;
    private int endYear;
    private int endMonth = -1;
    private int endDay;
    
    private String temp = "";
    private String tempStore = "";
    private ArrayList<String> selectedItems = new ArrayList<String>();
    private ArrayList<String> waypoints1 = new ArrayList<String>();
    
    public String getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    public String getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(String endLoc) {
        this.endLoc = endLoc;
    }
    
     public int getPriv() {
        return priv;
    }

    public void setPriv(int priv) {
        this.priv = priv;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @PostConstruct
    public void init() {
        trip = tripService.find(id);
       // setup();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Trip getTrip() {
        return trip;
    }
     
     public void submit() throws IOException {
         trip.setName(name);
         trip.setStartLocation(startLoc);
         trip.setEndLocation(endLoc);
        Date startDate = new Date(startYear - 1900, startMonth, startDay);
        Date endDate = new Date(endYear - 1900, endMonth, endDay);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        this.setWaypoints();
        tripService.update(trip);
        
        
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Trip successfully created</div>");
        Faces.redirect("profile.xhtml");
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
      
      public void setup(){
          name = trip.getName();
          startLoc = trip.getStartLocation();
          endLoc = trip.getEndLocation();       
        startDay = trip.getStartDate().getDate();      
        startMonth = trip.getStartDate().getMonth();
        startYear = trip.getStartDate().getYear()+1900;
        endDay = trip.getEndDate().getDate();
        endMonth = trip.getEndDate().getMonth();
        endYear = trip.getEndDate().getYear()+1900;
    
          
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
