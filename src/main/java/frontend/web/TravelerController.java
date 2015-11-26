package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.FollowerData;
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

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@Controller
@ViewScoped
//@Scope("view")
public class TravelerController {

    @Autowired
    RestUrlAccessor restUrlAccessor;

    @Autowired
    CurrentUserService currentUserService;

    private List<Traveler> travelers = new ArrayList<>();
    private List<FollowerData> followerDatas = new ArrayList<>(); // todo: optimize: load traveler personaldatas only instead of followed data
    private PersonalData personalData = new PersonalData();
    private UploadedFile profilePic;
    private boolean editingMode = false;

    public String loadPersonalDataForTraveler() { // add to profilecontroller?
        personalData = currentUserService.getPersonalData();
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
        loadFollowedsForTraveler(); // todo: optimize this
    }

    public String loadFollowedsForTraveler() {  // todo: use traveler type instead of follows -> fix backend!
        loadPersonalDataForTraveler();      // todo: optimize
        followerDatas = restUrlAccessor.loadAllFollowsForTraveler(String.valueOf(personalData.getId()));

        for(int i = 0; i < followerDatas.size(); i++) {
            byte[] pic = followerDatas.get(i).getTraveler2().getPersonaldata().getProfilepic();
            if(pic != null) {
                InputStream stream = new ByteArrayInputStream(pic);
                PersonalData traveler2 = followerDatas.get(i).getTraveler2().getPersonaldata();
                traveler2.setDiplayablePicture(new DefaultStreamedContent(stream));
            }
        }
        return "follows";
    }

    public boolean isFollowed(String name) {    // todo cache this or add to a viewscope constant
        if(followerDatas.isEmpty()) {
            loadFollowedsForTraveler();
        }
        boolean isFollowed = false;
        for(FollowerData f : followerDatas) {   // todo: check java8 stream
            if(name.equals(f.getTraveler2().getPersonaldata().getUsername())) {
                isFollowed = true;
            }
        }
        return isFollowed;
    }

    public StreamedContent getDynamicTravelerImage() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("picture_id");
        if(id!=null && this.followerDatas !=null && !this.followerDatas.isEmpty()){
            Integer pictureId = Integer.parseInt(id);
            for(FollowerData followerDataTemp : this.followerDatas){
                if(followerDataTemp.getTraveler2().getId() == pictureId){
                    return followerDataTemp.getTraveler2().getPersonaldata().getDiplayablePicture();
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

    public List<FollowerData> getFollowerDatas() {
        return followerDatas;
    }

    public void setFollowerDatas(List<FollowerData> followerDatas) {
        this.followerDatas = followerDatas;
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
