package frontend.web;

import frontend.resthelper.RestHelper;
import frontend.domain.Picture;
import frontend.domain.Place;
import frontend.domain.Trip;
import frontend.resthelper.TripResourceHelper;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
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
public class PlaceController {

    @Autowired
    RestHelper restHelper;

    @Autowired
    CurrentUserService currentUserService;

    @Autowired
    TripResourceHelper tripResourceHelper;

    private boolean editingMode = false;
    private Trip selectedTrip;
    private Place selectedPlace;
    private Place newPlace = new Place();
    private List<Picture> uploadedPictures = new ArrayList<>();
    private List<Picture> selectedPlacePictures;

    public String loadPlace(Place place, Trip trip, boolean editingMode) {
        setEditingMode(editingMode);
        selectedTrip = trip;
        selectedPlace = place;
        loadPicturesForPlace(selectedPlace.getId());
        return "placeprofile";
    }

    private void loadPicturesForPlace(int placeId) {
        selectedPlacePictures = tripResourceHelper.loadPicturesForPlaceByPlaceId(placeId);
        for(int i = 0; i < selectedPlacePictures.size(); i++) {
            byte[] pic = selectedPlacePictures.get(i).getData();
            if(pic != null) {
                InputStream stream = new ByteArrayInputStream(pic);
                selectedPlacePictures.get(i).setDiplayablePicture(new DefaultStreamedContent(stream));
            }
        }
    }

    public StreamedContent getDynamicPlaceImage() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("picture_id");
        if(id != null && this.selectedPlacePictures != null && !this.selectedPlacePictures.isEmpty()){
            Integer pictureId = Integer.parseInt(id);
            for(Picture picture : this.selectedPlacePictures){
                if(picture.getId() == pictureId){
                    return picture.getDiplayablePicture();
                }
            }
        }
        return new DefaultStreamedContent();
    }

    public String addPlace(Trip trip) {
        newPlace.setName("new place");
        newPlace.setDescription("new place");
        tripResourceHelper.addPlaceForTrip(newPlace, trip.getId());
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
        //tripResourceHelper.updatePlace(selectedPlace); // TODO: implement
        if(!uploadedPictures.isEmpty()) {
            tripResourceHelper.uploadPicturesForTrip(selectedTrip.getGallery().getId(), selectedPlace.getId(), uploadedPictures);
        }
        setEditingMode(false);
    }

    public String loadPlaceForFollowed(Place place, Trip trip, boolean isEditingMode) {
        loadPlace(place, trip, isEditingMode);
        return "followedplaceprofile";
    }

    public void deleteSelectedTrip() {
        // TODO: implement
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

    public List<Picture> getSelectedPlacePictures() {
        return selectedPlacePictures;
    }

    public void setSelectedPlacePictures(List<Picture> selectedPlacePictures) {
        this.selectedPlacePictures = selectedPlacePictures;
    }
}
