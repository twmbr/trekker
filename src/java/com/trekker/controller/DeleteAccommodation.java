package com.trekker.controller;

import com.trekker.model.Accommodation;
import com.trekker.model.Trip;
import com.trekker.service.AccommodationService;
import com.trekker.service.TripService;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class DeleteAccommodation {    
    @EJB
    private TripService tripService;
    
    @EJB
    private AccommodationService accommodationService;
    
    public void submit(int accommodationId) throws IOException {
        Accommodation accommodationToDelete = accommodationService.find(accommodationId);
        Trip trip = accommodationToDelete.getTripId();
        trip.getAccommodationCollection().remove(accommodationToDelete);
        tripService.update(trip);
        accommodationService.delete(accommodationToDelete);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Accommodation successfully updated</div>");
        Faces.redirect("trip.xhtml?id=%s", Integer.toString(trip.getId()));
    }
}
