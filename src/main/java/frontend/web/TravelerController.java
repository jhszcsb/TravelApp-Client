package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.Friendship;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@Controller
@ViewScoped
//@Scope("view")
public class TravelerController {

    @Autowired
    RestUrlAccessor restUrlAccessor;

    private List<Traveler> travelers = new ArrayList<>();
    private List<Friendship> friendships = new ArrayList<>(); // todo: optimize: load traveler personaldatas only instead of friendship data
    private PersonalData personalData = new PersonalData();
    private UploadedFile profilePic;
    private boolean editingMode = false;

    public String loadPersonalDataForTraveler() { // add to profilecontroller?
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   // todo: refactor to a variable
        String name = user.getUsername();
        personalData = restUrlAccessor.loadPersonalataForTraveler(name);
        byte[] pic = personalData.getProfilepic();
        if(pic != null) {
            InputStream stream = new ByteArrayInputStream(pic);
            personalData.setDiplayablePicture(new DefaultStreamedContent(stream));
        }
        return "profile";
    }

    public void updatePersonalDataForTraveler() { // add to profilecontroller?
        if(profilePic != null) {
            personalData.setProfilepic(profilePic.getContents());
        }
        restUrlAccessor.updatePersonalDataForTraveler(personalData);
        setEditingMode(false);
    }

    public void loadAllTravelers() {
        travelers = restUrlAccessor.loadAllTravelers();
    }

    public String loadFriendsForTraveler() {  // todo: use traveler type instead of friendship -> fix backend!
        loadPersonalDataForTraveler();      // todo: optimize
        friendships = restUrlAccessor.loadAllFriendsForTraveler(String.valueOf(personalData.getId()));

        for(int i = 0; i < friendships.size(); i++) {
            byte[] pic = friendships.get(i).getTraveler2().getPersonaldata().getProfilepic();
            if(pic != null) {
                InputStream stream = new ByteArrayInputStream(pic);
                PersonalData traveler2 = friendships.get(i).getTraveler2().getPersonaldata();
                traveler2.setDiplayablePicture(new DefaultStreamedContent(stream));
            }
        }
        return "friends";
    }

    public boolean isFriend(String name) {    // todo cache this or add to a viewscope constant
        if(friendships.isEmpty()) {
            loadFriendsForTraveler();
        }
        boolean isFriend = false;
        for(Friendship f : friendships) {   // todo: check java8 stream
            if(name.equals(f.getTraveler2().getPersonaldata().getUsername())) {
                isFriend = true;
            }
        }
        return isFriend;
    }

    public StreamedContent getDynamicTravelerImage() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("picture_id");
        if(id!=null && this.friendships!=null && !this.friendships.isEmpty()){
            Integer pictureId = Integer.parseInt(id);
            for(Friendship friendshipTemp : this.friendships){
                if(friendshipTemp.getTraveler2().getId() == pictureId){
                    return friendshipTemp.getTraveler2().getPersonaldata().getDiplayablePicture();
                }
            }
        }
        return new DefaultStreamedContent();
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

    public boolean isEditingMode() {
        return editingMode;
    }

    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }
}
