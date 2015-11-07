package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.Friendship;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@Controller
@ViewScoped
//@Scope("view")
public class TravelerController {

    //@Autowired    //todo: add dependency injection
    RestUrlAccessor restUrlAccessor = new RestUrlAccessor();

    private List<Traveler> travelers = new ArrayList<>();
    private List<Friendship> friendships = new ArrayList<>();
    private PersonalData personalData = new PersonalData();
    private UploadedFile profilePic;
    private DefaultStreamedContent picture;

    public void loadPersonalDataForTraveler() { // add to profilecontroller?
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   // todo: refactor to a variable
        String name = user.getUsername();
        personalData = restUrlAccessor.loadPersonalataForTraveler(name);
        InputStream stream = new ByteArrayInputStream(personalData.getProfilepic());
        picture = new DefaultStreamedContent(stream);
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
        loadPersonalDataForTraveler();      // todo: optimize
        friendships = restUrlAccessor.loadAllFriendsForTraveler(String.valueOf(personalData.getId()));
    }

    public void uploadProfilePic() {
        if(profilePic != null) {
            FacesMessage message = new FacesMessage("Succesful", profilePic.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
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

    public UploadedFile getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(UploadedFile profilePic) {
        this.profilePic = profilePic;
    }

    public DefaultStreamedContent getPicture() {
        return picture;
    }

    public void setPicture(DefaultStreamedContent picture) {
        this.picture = picture;
    }
}
