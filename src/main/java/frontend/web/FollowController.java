package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import frontend.domain.Trip;
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
    RestUrlAccessor restUrlAccessor;

    @Autowired
    CurrentUserService currentUserService;

    private PersonalData selectedFollowedProfile;
    private List<Trip> selectedFollowedTrips;

    public String loadPersonalDataForFollowed(String name) {
        selectedFollowedProfile = restUrlAccessor.loadPersonalDataForTraveler(name);
        byte[] pic = selectedFollowedProfile.getProfilepic();
        if(pic != null) {
            InputStream stream = new ByteArrayInputStream(pic);
            selectedFollowedProfile.setDiplayablePicture(new DefaultStreamedContent(stream));
        }
        loadTripsForFollowed();
        return "followedprofile";
    }

    public void loadTripsForFollowed() {
        Traveler traveler = restUrlAccessor.getTravelerByPersonalDataId(selectedFollowedProfile.getId());
        selectedFollowedTrips = restUrlAccessor.loadAllTripsForTraveler(String.valueOf(traveler.getId()));
    }

    public void makeFollowed(String name) {
        String loggedInUserName = currentUserService.getName();
        restUrlAccessor.createFollowed(loggedInUserName, name);
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
