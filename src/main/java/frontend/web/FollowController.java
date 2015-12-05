package frontend.web;

import frontend.resthelper.FollowerResourceHelper;
import frontend.resthelper.RestHelper;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import frontend.domain.Trip;
import frontend.resthelper.TravelerResourceHelper;
import frontend.resthelper.TripResourceHelper;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Controller
@ViewScoped
public class FollowController {

    @Autowired
    RestHelper restHelper;

    @Autowired
    CurrentUserService currentUserService;

    @Autowired
    TravelerResourceHelper travelerResourceHelper;

    @Autowired
    TripResourceHelper tripResourceHelper;

    @Autowired
    FollowerResourceHelper followerResourceHelper;

    private PersonalData selectedFollowedProfile;
    private List<Trip> selectedFollowedTrips;

    public String loadPersonalDataForFollowed(String name) {
        selectedFollowedProfile = travelerResourceHelper.loadPersonalDataForTraveler(name);
        byte[] pic = selectedFollowedProfile.getProfilepic();
        if(pic != null) {
            InputStream stream = new ByteArrayInputStream(pic);
            selectedFollowedProfile.setDiplayablePicture(new DefaultStreamedContent(stream));
        }
        loadTripsForFollowed();
        return "followedprofile";
    }

    public void loadTripsForFollowed() {
        Traveler traveler = travelerResourceHelper.getTravelerByPersonalDataId(selectedFollowedProfile.getId());
        selectedFollowedTrips = tripResourceHelper.loadAllTripsForTraveler(String.valueOf(traveler.getId()));
    }

    public void makeFollowed(String name) {
        String loggedInUserName = currentUserService.getName();
        followerResourceHelper.createFollowed(loggedInUserName, name);
        FacesMessage message = new FacesMessage("Followed!");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
    }

    public PersonalData getSelectedFollowedProfile() {
        return selectedFollowedProfile;
    }

    public void setSelectedFollowedProfile(PersonalData selectedFollowedProfile) {
        this.selectedFollowedProfile = selectedFollowedProfile;
    }

    public List<Trip> getSelectedFollowedTrips() {
        return selectedFollowedTrips;
    }

    public void setSelectedFollowedTrips(List<Trip> selectedFollowedTrips) {
        this.selectedFollowedTrips = selectedFollowedTrips;
    }
}
