package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@Controller
@ViewScoped
//@Scope("request")
public class TripController {

    @Autowired
    RestUrlAccessor restUrlAccessor;

    @Autowired
    CurrentUserService currentUserService;

    private List<Trip> trips = new ArrayList<>();
    private List<Trip> tripsOfFriends = new ArrayList<>();
    private Trip selectedTrip;
    private boolean editingMode = false;

    public String loadTripsForTraveler() {
        Traveler traveler = currentUserService.getTraveler();
        trips = restUrlAccessor.loadAllTripsForTraveler(String.valueOf(traveler.getId()));
        return "trips";
    }

    public void loadAllTripsOfFriends() {
        String name = currentUserService.getName();
        tripsOfFriends = restUrlAccessor.loadAllTripsOfFriendsForTraveler(name);
    }

    public String loadTrip(Trip trip) {
        selectedTrip = trip;
        return "tripprofile";
    }

    public void createNew() {
        Traveler traveler = currentUserService.getTraveler();
        Trip trip = restUrlAccessor.createTrip(traveler.getId());
        loadTrip(trip);
    }

    public void updateSelectedTrip() {
        restUrlAccessor.updateTrip(selectedTrip);
        setEditingMode(false);
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Trip> getTripsOfFriends() {
        return tripsOfFriends;
    }

    public void setTripsOfFriends(List<Trip> tripsOfFriends) {
        this.tripsOfFriends = tripsOfFriends;
    }

    public Trip getSelectedTrip() {
        return selectedTrip;
    }

    public void setSelectedTrip(Trip selectedTrip) {
        this.selectedTrip = selectedTrip;
    }

    public boolean isEditingMode() {
        return editingMode;
    }

    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }
}
