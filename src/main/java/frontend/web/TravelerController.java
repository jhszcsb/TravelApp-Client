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

@Controller
@Scope("request")
public class TravelerController {

    //@Autowired    //todo: add dependency injection
    RestUrlAccessor restUrlAccessor = new RestUrlAccessor();

    private List<Traveler> travelers = new ArrayList<>();
    private List<Friendship> friendships = new ArrayList<>();
    private PersonalData personalData = new PersonalData();

    public void loadPersonalDataForTraveler() { // add to profilecontroller?
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        System.out.println("User name: " + name);   // todo debug
        personalData = restUrlAccessor.loadPersonalataForTraveler(name);
    }

    public void loadAllTravelers() {
        travelers = restUrlAccessor.loadAllTravelers();
    }

    public void loadFriendsForTraveler() {  // todo: use traveler type instead of friendship -> fix backend!
        friendships = restUrlAccessor.loadAllFriendsForTraveler("1");    // todo: id is hardcoded for testing. use the id of the authenticated user
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
