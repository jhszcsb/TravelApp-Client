package frontend.web;

import frontend.rest.RestHelper;
import frontend.domain.Picture;
import frontend.domain.Trip;
import frontend.rest.TripResourceHelper;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@ViewScoped
//@Scope("request")
public class TripController {

    @Autowired
    RestHelper restHelper;

    @Autowired
    CurrentUserService currentUserService;

    @Autowired
    TripResourceHelper tripResourceHelper;

    private List<Trip> trips = new ArrayList<>();
    private List<Trip> tripsOfFolloweds = new ArrayList<>();
    private Trip selectedTrip;
    private List<Picture> selectedTripPictures = new ArrayList<>();
    private boolean editingMode = false;

    public String loadTripsForTraveler() {
        trips = tripResourceHelper.loadAllTripsForTraveler(String.valueOf(currentUserService.getTraveler().getId()));
        return "trips";
    }

    public void loadAllTripsOfFolloweds() {
        tripsOfFolloweds = tripResourceHelper.loadAllTripsOfFollowedsForTraveler(currentUserService.getName());
    }

    public String loadTrip(Trip trip, boolean editingMode) {
        setEditingMode(editingMode);
        selectedTrip = trip;
        loadPicturesForTrip(trip.getGallery().getId());
        return "tripprofile";
    }

    private void loadPicturesForTrip(int galleryId) {
        selectedTripPictures = tripResourceHelper.loadPicturesForTripByGalleryId(galleryId);
        for(int i = 0; i < selectedTripPictures.size(); i++) {
            byte[] pic = selectedTripPictures.get(i).getData();
            if(pic != null) {
                InputStream stream = new ByteArrayInputStream(pic);
                selectedTripPictures.get(i).setDiplayablePicture(new DefaultStreamedContent(stream));
            }
        }
    }

    public String createNew() {
        Trip trip = tripResourceHelper.createTrip(currentUserService.getTraveler().getId());
        return loadTrip(trip, true);
    }

    public void updateSelectedTrip() {
        tripResourceHelper.updateTrip(selectedTrip);
        setEditingMode(false);
    }

    public String deleteSelectedTrip() {
        tripResourceHelper.deleteTrip(selectedTrip.getId());
        // todo: add facesmessage after successful delete
        selectedTrip = null;
        return "trips";
    }

    public StreamedContent getDynamicTripImage() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("picture_id");
        if(id != null && this.selectedTripPictures != null && !this.selectedTripPictures.isEmpty()){
            Integer pictureId = Integer.parseInt(id);
            for(Picture picture : this.selectedTripPictures){
                if(picture.getId() == pictureId){
                    return picture.getDiplayablePicture();
                }
            }
        }
        return new DefaultStreamedContent();
    }

    public String loadTripForFollowed(Trip trip) {
        loadTrip(trip, false);
        return "followedtripprofile";
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Trip> getTripsOfFolloweds() {
        return tripsOfFolloweds;
    }

    public void setTripsOfFolloweds(List<Trip> tripsOfFolloweds) {
        this.tripsOfFolloweds = tripsOfFolloweds;
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

    public List<Picture> getSelectedTripPictures() {
        return selectedTripPictures;
    }

    public void setSelectedTripPictures(List<Picture> selectedTripPictures) {
        this.selectedTripPictures = selectedTripPictures;
    }
}
