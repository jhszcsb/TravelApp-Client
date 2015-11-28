package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.Picture;
import frontend.domain.Place;
import frontend.domain.Trip;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@Controller
@ViewScoped
public class PlaceController {

    @Autowired
    RestUrlAccessor restUrlAccessor;

    @Autowired
    CurrentUserService currentUserService;

    private boolean editingMode = false;
    private Trip selectedTrip;
    private Place selectedPlace;
    private Place newPlace = new Place();
    private List<Picture> uploadedPictures = new ArrayList<>();

    public String loadPlace(Place place, Trip trip, boolean editingMode) {
        setEditingMode(editingMode);
        selectedTrip = trip;
        selectedPlace = place;
        return "placeprofile";
    }

    public String addPlace(Trip trip) {
        newPlace.setName("new place");
        newPlace.setDescription("new place");
        restUrlAccessor.addPlaceForTrip(newPlace, trip.getId());
        selectedPlace = newPlace;
        return loadPlace(selectedPlace, trip, true);
    }

    public void upload(FileUploadEvent event) {
        UploadedFile f = event.getFile();
        Picture pic = new Picture();
        pic.setGallery(selectedTrip.getGallery().getId());
        pic.setData(f.getContents());
        uploadedPictures.add(pic);
    }

    public void updateSelectedPlace() {
        //restUrlAccessor.updatePlace(selectedPlace); // TODO: implement

        if(!uploadedPictures.isEmpty()) {
            restUrlAccessor.uploadPicturesForTrip(selectedTrip.getGallery().getId(), selectedPlace.getId(), uploadedPictures);
        }

        setEditingMode(false);
    }

    public void deleteSelectedTrip() {

    }

    public boolean isEditingMode() {
        return editingMode;
    }

    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }

    public Trip getSelectedTrip() {
        return selectedTrip;
    }

    public void setSelectedTrip(Trip selectedTrip) {
        this.selectedTrip = selectedTrip;
    }

    public Place getSelectedPlace() {
        return selectedPlace;
    }

    public void setSelectedPlace(Place selectedPlace) {
        this.selectedPlace = selectedPlace;
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
}
