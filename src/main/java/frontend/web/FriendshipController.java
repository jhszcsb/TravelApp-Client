package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import frontend.domain.Trip;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Controller
@ViewScoped
public class FriendshipController {

    //@Autowired    //todo: add dependency injection
    RestUrlAccessor restUrlAccessor = new RestUrlAccessor();

    private PersonalData selectedFriendProfile;
    private List<Trip> selectedFriendTrips;

    public String loadPersonalDataForFriend(String name) {
        selectedFriendProfile = restUrlAccessor.loadPersonalataForTraveler(name);
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   // todo: refactor to a spring bean
        String loggedInUserName = user.getUsername();
        restUrlAccessor.createFriendship(loggedInUserName, name);
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
