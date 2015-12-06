package frontend.web;

import frontend.domain.SocialData;
import frontend.resthelper.FollowerResourceHelper;
import frontend.resthelper.RestHelper;
import frontend.domain.FollowerData;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import frontend.resthelper.TravelerResourceHelper;
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

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

@Controller
@ViewScoped
//@Scope("view")
public class TravelerController {

    @Autowired
    RestHelper restHelper;

    @Autowired
    CurrentUserService currentUserService;

    @Autowired
    TravelerResourceHelper travelerResourceHelper;

    @Autowired
    FollowerResourceHelper followerResourceHelper;

    private List<Traveler> travelers = new ArrayList<>();
    private List<FollowerData> followerDatas = new ArrayList<>(); // optimize: load traveler personaldatas only instead of followed data
    private PersonalData personalData = new PersonalData();
    private SocialData socialData = new SocialData();
    private UploadedFile profilePic;
    private boolean editingMode = false;

    public String loadOrReloadPersonalDataForTraveler(boolean reload) {
        if(personalData.getDiplayablePicture() == null || reload == true) {
            return loadPersonalDataForTraveler();
        }
        return "profile";
    }

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
        travelerResourceHelper.updatePersonalDataForTraveler(personalData);
        // reload here?
        setEditingMode(false);
    }

    public String loadAllTravelers() {
        travelers = travelerResourceHelper.loadAllTravelers();
        loadFollowedsForTraveler(); // improvement: optimize this
        return "browse";
    }

    public String loadFollowedsForTraveler() {  // improvement: use traveler type instead of follows -> fix backend!
        followerDatas = followerResourceHelper.loadAllFollowsForTraveler(String.valueOf(currentUserService.getTraveler().getId()));

        for(int i = 0; i < followerDatas.size(); i++) {
            byte[] pic = followerDatas.get(i).getFollowed().getPersonaldata().getProfilepic();
            if(pic != null) {
                InputStream stream = new ByteArrayInputStream(pic);
                PersonalData followed = followerDatas.get(i).getFollowed().getPersonaldata();
                followed.setDiplayablePicture(new DefaultStreamedContent(stream));
            }
        }
        return "follows";
    }

    public boolean isFollowed(String name) { // cache this?
        if(followerDatas.isEmpty()) {
            loadFollowedsForTraveler();
        }
        boolean isFollowed = false;
        for(FollowerData f : followerDatas) {   // improvement: check java8 stream
            if(name.equals(f.getFollowed().getPersonaldata().getUsername())) {
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
                if(followerDataTemp.getFollowed().getId() == pictureId){
                    return followerDataTemp.getFollowed().getPersonaldata().getDiplayablePicture();
                }
            }
        }
        return new DefaultStreamedContent();
    }

    // http://stackoverflow.com/questions/8304967/how-to-use-pgraphicimage-with-streamedcontent-within-pdatatable

    public StreamedContent getDynamicTravelerProfileImage() {
        //return personalData.getDiplayablePicture();

        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Get ID value from actual request param.
            return personalData.getDiplayablePicture();
        }

/*
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("profile_id");
        Integer pictureId = Integer.parseInt(id);
        if(personalData.getDiplayablePicture() != null) {
            return personalData.getDiplayablePicture();
        }
        return new DefaultStreamedContent();*/
    }

    public void upload(FileUploadEvent event) {
    //public void upload() {
        //FacesMessage message = new FacesMessage("INVOKED", "INVOKED");
        //FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        //FacesContext.getCurrentInstance().addMessage(null, message);
        profilePic = event.getFile();
        personalData.setProfilepic(profilePic.getContents());
    }

    public void cancelUpdate() {
        setEditingMode(false);
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
        loadPersonalDataForTraveler();
    }
}
