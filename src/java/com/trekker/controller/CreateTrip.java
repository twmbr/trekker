package com.trekker.controller;

import com.trekker.model.Trip;
import com.trekker.model.User;
import com.trekker.service.UserService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    private int addIndex = 0;
    private String temp = "";
    private String tempStore = "";
    private ArrayList<String> selectedItems = new ArrayList<String>();
    private ArrayList<String> waypoints1 = new ArrayList<String>();

    @EJB
    private UserService userService;

    @PostConstruct
    public void init() {
        trip = new Trip();
        user = userService.currentUser();
    }

    public void submit() throws IOException {
        this.setWaypoints();
        trip.setWaypoints(temp);

        trip.setOwner(user);
        Collection<Trip> trips = user.getTrips();
        trips.add(trip);
        user.setTrips(trips);
        userService.update(user);
        trip = new Trip();
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
