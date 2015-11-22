package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.*;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
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
    private List<Picture> uploadedPictures = new ArrayList<>();
    private Place newPlace = new Place();
    private Place selectedPlace;

    public String loadTripsForTraveler() {
        trips = restUrlAccessor.loadAllTripsForTraveler(String.valueOf(currentUserService.getTraveler().getId()));
        return "trips";
    }

    public void loadAllTripsOfFriends() {
        tripsOfFriends = restUrlAccessor.loadAllTripsOfFriendsForTraveler(currentUserService.getName());
    }

    public String loadTrip(Trip trip, boolean editingMode) {
        setEditingMode(editingMode);
        selectedTrip = trip;
        return "tripprofile";
    }

    public String createNew() {
        Trip trip = restUrlAccessor.createTrip(currentUserService.getTraveler().getId());
        return loadTrip(trip, true);
    }

    public void updateSelectedTrip() {
        restUrlAccessor.updateTrip(selectedTrip);

        /*if(!uploadedFiles.isEmpty()) {
            List<Picture> pictures = new ArrayList<>();
            for (UploadedFile file : uploadedFiles) {
                Picture pic = new Picture();
                pic.setData(file.getContents());
                pic.setGallery_id(selectedTrip.getGallery().getId());
                pictures.add(pic);
            }
            restUrlAccessor.uploadPicturesForTrip(selectedTrip.getGallery().getId(), pictures);
            uploadedFiles = null;
        }*/
        if(!uploadedPictures.isEmpty()) {
            restUrlAccessor.uploadPicturesForTrip(selectedTrip.getGallery().getId(), uploadedPictures);
        }

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

    public String loadPlace(Place place) {
        selectedPlace = place;
        return "placeprofile";
    }

    public void upload(FileUploadEvent event) {
        UploadedFile f = event.getFile();
        Picture pic = new Picture();
        pic.setGallery_id(selectedTrip.getGallery().getId());   // todo: gallery is null
        pic.setData(f.getContents());
        uploadedPictures.add(pic);
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

    public List<Picture> getUploadedPictures() {
        return uploadedPictures;
    }

    public void setUploadedPictures(List<Picture> uploadedPictures) {
        this.uploadedPictures = uploadedPictures;
    }

    public Place getNewPlace() {
        return newPlace;
    }

    public void setNewPlace(Place newPlace) {
        this.newPlace = newPlace;
    }

    public Place getSelectedPlace() {
        return selectedPlace;
    }

    public void setSelectedPlace(Place selectedPlace) {
        this.selectedPlace = selectedPlace;
    }
}
