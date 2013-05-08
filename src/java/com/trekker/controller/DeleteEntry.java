package com.trekker.controller;

import com.trekker.model.Itinerary;
import com.trekker.model.Trip;
import com.trekker.service.ItineraryService;
import com.trekker.service.TripService;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class DeleteEntry {    
    @EJB
    private TripService tripService;
    
    @EJB
    private ItineraryService itineraryService;
    
    public void submit(int entryId) throws IOException {
        Itinerary entryToDelete = itineraryService.find(entryId);
        Trip trip = entryToDelete.getTripId();
        trip.getItineraryCollection().remove(entryToDelete);
        tripService.update(trip);
        itineraryService.delete(entryToDelete);
        Messages.addFlashGlobalInfo("<div class=\"alert alert-success\">Itinerary successfully updated</div>");
        Faces.redirect("trip.xhtml?id=%s", Integer.toString(trip.getId()));
    }
}
