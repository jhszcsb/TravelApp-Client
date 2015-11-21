package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.*;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
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
    private List<UploadedFile> uploadedFiles = new ArrayList<>();
    private Places newPlace = new Places();

    public String loadTripsForTraveler() {
        trips = restUrlAccessor.loadAllTripsForTraveler(String.valueOf(currentUserService.getTraveler().getId()));
        return "trips";
    }

    public void loadAllTripsOfFriends() {
        tripsOfFriends = restUrlAccessor.loadAllTripsOfFriendsForTraveler(currentUserService.getName());
    }

    public String loadTrip(Trip trip) {
        setEditingMode(false);
        selectedTrip = trip;
        return "tripprofile";
    }

    public String createNew() {
        Trip trip = restUrlAccessor.createTrip(currentUserService.getTraveler().getId());
        setEditingMode(true);
        return loadTrip(trip);
    }

    public void updateSelectedTrip() {
        restUrlAccessor.updateTrip(selectedTrip);
        setEditingMode(false);
    }

    public String deleteSelectedTrip() {
        restUrlAccessor.deleteTrip(selectedTrip.getId());
        // todo: facesmessage after successful delete
        selectedTrip = null;
        return "trips";
    }

    public void addPlace() {
        restUrlAccessor.addPlaceForTrip(newPlace, selectedTrip.getId());
    }

    public void upload(FileUploadEvent event) {
        uploadedFiles.add(event.getFile());
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

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public Places getNewPlace() {
        return newPlace;
    }

    public void setNewPlace(Places newPlace) {
        this.newPlace = newPlace;
    }
}
