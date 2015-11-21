package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import frontend.domain.Trip;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Controller
@ViewScoped
public class FriendshipController {

    @Autowired
    RestUrlAccessor restUrlAccessor;

    @Autowired
    CurrentUserService currentUserService;

    private PersonalData selectedFriendProfile;
    private List<Trip> selectedFriendTrips;

    public String loadPersonalDataForFriend(String name) {
        selectedFriendProfile = restUrlAccessor.loadPersonalDataForTraveler(name);
        byte[] pic = selectedFriendProfile.getProfilepic();
        if(pic != null) {
            InputStream stream = new ByteArrayInputStream(pic);
            selectedFriendProfile.setDiplayablePicture(new DefaultStreamedContent(stream));
        }
        loadTripsForFriend();
        return "friendprofile";
    }

    public void loadTripsForFriend() {
        Traveler traveler = restUrlAccessor.getTravelerByPersonalDataId(selectedFriendProfile.getId());
        selectedFriendTrips = restUrlAccessor.loadAllTripsForTraveler(String.valueOf(traveler.getId()));
    }

    public void makeFriend(String name) {
        String loggedInUserName = currentUserService.getName();
        restUrlAccessor.createFriendship(loggedInUserName, name);
        FacesMessage message = new FacesMessage("Friend added!");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
    }

    public PersonalData getSelectedFriendProfile() {
        return selectedFriendProfile;
    }

    public void setSelectedFriendProfile(PersonalData selectedFriendProfile) {
        this.selectedFriendProfile = selectedFriendProfile;
    }

    public List<Trip> getSelectedFriendTrips() {
        return selectedFriendTrips;
    }

    public void setSelectedFriendTrips(List<Trip> selectedFriendTrips) {
        this.selectedFriendTrips = selectedFriendTrips;
    }
}
