package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.Friendship;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.faces.bean.ViewScoped;

@Controller
@ViewScoped
//@Scope("view")
public class TravelerController {

    //@Autowired    //todo: add dependency injection
    RestUrlAccessor restUrlAccessor = new RestUrlAccessor();

    private List<Traveler> travelers = new ArrayList<>();
    private List<Friendship> friendships = new ArrayList<>();
    private PersonalData personalData = new PersonalData();

    public void loadPersonalDataForTraveler() { // add to profilecontroller?
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   // todo: refactor to a variable
        String name = user.getUsername();
        personalData = restUrlAccessor.loadPersonalataForTraveler(name);
        //System.out.println(personalData.getUsername());
    }

    public void updatePersonalDataForTraveler() { // add to profilecontroller?
        restUrlAccessor.updatePersonalDataForTraveler(personalData);
        //System.out.println(personalData.getUsername());
    }

    public void loadAllTravelers() {
        travelers = restUrlAccessor.loadAllTravelers();
    }

    public void loadFriendsForTraveler() {  // todo: use traveler type instead of friendship -> fix backend!
        loadPersonalDataForTraveler();  // todo: optimize
        friendships = restUrlAccessor.loadAllFriendsForTraveler(String.valueOf(personalData.getId()));
    }

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<Traveler> travelers) {
        this.travelers = travelers;
    }

    public List<Friendship> getFriendships() {
        return friendships;
    }

    public void setFriendships(List<Friendship> friendships) {
        this.friendships = friendships;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }
}
