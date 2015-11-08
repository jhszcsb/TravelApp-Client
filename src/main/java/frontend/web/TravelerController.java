package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.Friendship;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
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
    private List<Friendship> friendships = new ArrayList<>(); // todo: optimize: load traveler personaldatas only instead of friendship data
    private PersonalData personalData = new PersonalData();
    private UploadedFile profilePic;

    public void loadPersonalDataForTraveler() { // add to profilecontroller?
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   // todo: refactor to a variable
        String name = user.getUsername();
        personalData = restUrlAccessor.loadPersonalataForTraveler(name);
        InputStream stream = new ByteArrayInputStream(personalData.getProfilepic());
        personalData.setDiplayablePicture(new DefaultStreamedContent(stream));
        //System.out.println(personalData.getUsername());
    }

    public void updatePersonalDataForTraveler() { // add to profilecontroller?
        if(profilePic != null) {
            personalData.setProfilepic(profilePic.getContents());
        }
        restUrlAccessor.updatePersonalDataForTraveler(personalData);
        //System.out.println(personalData.getUsername());
    }

    public void loadAllTravelers() {
        travelers = restUrlAccessor.loadAllTravelers();
    }

    public void loadFriendsForTraveler() {  // todo: use traveler type instead of friendship -> fix backend!
        loadPersonalDataForTraveler();      // todo: optimize
        friendships = restUrlAccessor.loadAllFriendsForTraveler(String.valueOf(personalData.getId()));

        for(int i = 0; i < friendships.size(); i++) {
            InputStream stream = new ByteArrayInputStream(friendships.get(i).getTraveler2().getPersonaldata().getProfilepic());
            friendships.get(i).getTraveler2().getPersonaldata().setDiplayablePicture(new DefaultStreamedContent(stream));
            //System.out.println("pic: " + friendships.get(i).getTraveler2().getPersonaldata().getProfilepic().length);
            //System.out.println("displayable pic: " + friendships.get(i).getTraveler2().getPersonaldata().getDiplayablePicture().toString());
        }
    }

    public void upload(FileUploadEvent event) {
    //public void upload() {
        //FacesMessage message = new FacesMessage("INVOKED", "INVOKED");
        //FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        //FacesContext.getCurrentInstance().addMessage(null, message);
        profilePic = event.getFile();
        personalData.setProfilepic(profilePic.getContents());
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
}
