package frontend.web;

import frontend.domain.Place;
import frontend.resthelper.RestHelper;
import frontend.domain.Picture;
import frontend.domain.Trip;
import frontend.resthelper.TripResourceHelper;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
        testHateoas();
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
        // improvement: add facesmessage after successful delete
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

    public List<Resource<Place>> loadPlacesForTrip(int trip_id) {
        Place place = new Place();
        Resource<Place> placeResource = new Resource<>(place);
        placeResource.add(linkTo(PlaceController.class).slash("trips").slash(place.getId()).slash("place").withRel("DELETE"));
        //System.out.println("href: " + placeResource.getLinks().get(0).getHref());
        //System.out.println("rel: " + placeResource.getLinks().get(0).getRel());
        Place place2 = new Place();
        Resource<Place> placeResource2 = new Resource<>(place2);
        List<Resource<Place>> placeResources = new ArrayList<>();
        placeResources.add(placeResource);
        placeResources.add(placeResource2);
        return placeResources;
    }

    public boolean isDeleteEnabled(int place_id, List<Resource<Place>> places) {
        for(Resource<Place> place : places) {
            if(place.getContent().getId() == place_id &&
                    !place.getLink("DELETE").getHref().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void testHateoas() {
        //System.out.println("Loading places for trip 6");
        List<Resource<Place>> places = loadPlacesForTrip(6);
        //System.out.println("Is delete enabled for place 1");
        isDeleteEnabled(1, places);
        //System.out.println("Is delete enabled for place 2");
        isDeleteEnabled(2, places);
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
