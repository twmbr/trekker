package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.model.User;
import com.trekker.service.UserService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@SessionScoped
public class CreateTrip implements Serializable {
    private Trip trip;
    private User user;

    private String temp = "";
    private String tempStore = "";
    private ArrayList<String> selectedItems = new ArrayList<String>();
    private ArrayList<String> waypoints1 = new ArrayList<String>();
    private boolean isPrivate;

    @EJB
    private UserService userService;

    @PostConstruct
    public void init() {
        trip = new Trip();
        user = userService.currentUser();
    }

    public void submit() throws IOException {
        this.setWaypoints();
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

    public void setWaypoints() {
        temp = "";
        for (int i = 0; i < waypoints1.size(); i++) {
            temp = temp + waypoints1.get(i) + ";";
        }
    }

    public void storeLocation() {
        waypoints1.add(waypoints1.size(), tempStore);
        setWaypoints();
    }

    public void removeWayPoints() {
        ArrayList<String> tempArray = new ArrayList<String>();
        for (int i = 0; i < selectedItems.size(); i++) {
            tempArray.add(selectedItems.get(i));
        }
        int tempSize = waypoints1.size();
        for (int j = tempSize - 1; j >= 0; j--) {
            for (int k = 0; k < tempArray.size(); k++) {
                if (waypoints1.get(j).equals(tempArray.get(k))) {
                    waypoints1.remove(k);
                }
            }
        }
    }
    
    public boolean getIsPrivate() {
        return isPrivate;
    }
    
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
