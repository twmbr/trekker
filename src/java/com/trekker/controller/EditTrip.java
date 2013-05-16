package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.model.User;
import com.trekker.service.TripService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@ManagedBean
@SessionScoped
public class EditTrip implements Serializable {
    @ManagedProperty(value = "#{param.id}")
    private int id;
    private String name = "";
    private String startLoc = "";
    private Trip trip;
    private User user;
    @EJB
    private TripService tripService;
    private String endLoc = "";
    private Date startDate;
    private Date endDate;
    private boolean isPublic;
    private int addIndex = 0;
    private String temp = "";
    private String tempStore = "";
    private ArrayList<String> selectedItems = new ArrayList<String>();

    public Date getStartDate() {
        return startDate;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Date getEndDate() {
        return endDate;
    }
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

        Trip aTrip = new Trip();
        aTrip = tripService.find(id);
        trip = new Trip();
        if (aTrip != null) {
            name = aTrip.getName();
            startLoc = aTrip.getStartLocation();
            endLoc = aTrip.getEndLocation();
            startDate = aTrip.getStartDate();
            endDate = aTrip.getEndDate();
            isPublic = aTrip.getIsPublic();
        }
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        this.setWaypoints();
        trip.setWaypoints(name);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setIsPublic(isPublic);

        Trip aTrip = new Trip();
        aTrip = tripService.find(id);
        trip.setOwner(aTrip.getOwner());
        trip.setId(aTrip.getId());
        tripService.update(trip);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Trip successfully created</div>");
        Faces.redirect("profile.xhtml");
    }

    public void setWaypoints() {
        temp = "";
        String[] tempString = new String[2];
        for (int i = 0; i < waypoints1.size(); i++) {
            tempString = waypoints1.get(i).split("/");
            temp = temp + tempString[1] + ";";
        }
    }

    public void storeLocation() {
        addIndex = addIndex + 1;
        waypoints1.add(waypoints1.size(), addIndex + "/" + tempStore);
        setWaypoints();
    }

    public void removeWayPoints() {
        ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for (int j = 0; j < waypoints1.size(); j++) {
            for (int i = 0; i < selectedItems.size(); i++) {
                if (selectedItems.get(i).equalsIgnoreCase(waypoints1.get(j))) {
                    tempArray.add(j);
                }
            }
        }
        for (int k = tempArray.size() - 1; k >= 0; k--) {
            int j = tempArray.get(k);
            waypoints1.remove(j);
        }
    }

    public void moveUpWayPoints() {
        ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for (int j = 0; j < waypoints1.size(); j++) {
            for (int i = 0; i < selectedItems.size(); i++) {
                if (selectedItems.get(i).equalsIgnoreCase(waypoints1.get(j))) {
                    tempArray.add(j);
                }
            }
        }

        int startIndex = 0;
        boolean check = true;
        for (int i = 0; i < tempArray.size(); i++) {
            if (tempArray.get(i) != i) {
                startIndex = i;
                check = false;
            }
            if (check == false) {
                i = tempArray.size();
            }
        }
        for (int i = startIndex; i < tempArray.size(); i++) {
            if (tempArray.get(startIndex) != 0) {
                Collections.swap(waypoints1, tempArray.get(i) - 1, tempArray.get(i));
            }
        }
    }

    public void moveDownWayPoints() {
        ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for (int j = 0; j < waypoints1.size(); j++) {
            for (int i = 0; i < selectedItems.size(); i++) {
                if (selectedItems.get(i).equalsIgnoreCase(waypoints1.get(j))) {
                    tempArray.add(j);
                }
            }
        }

        int startIndex = 0;
        boolean check = true;
        int place = waypoints1.size() - 1;
        for (int i = tempArray.size() - 1; i >= 0; i--) {
            if (tempArray.get(i) != place) {
                startIndex = i;
                check = false;
            }
            if (check == false) {
                i = -1;
            }
            place--;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (tempArray.get(startIndex) != waypoints1.size() - 1) {
                Collections.swap(waypoints1, tempArray.get(i) + 1, tempArray.get(i));
            }
        }
    }
}
