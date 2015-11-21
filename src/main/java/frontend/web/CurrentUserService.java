package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService /*implements LogoutSuccessHandler*/ {

    @Autowired
    RestUrlAccessor restUrlAccessor;

    private String name;
    private PersonalData personalData;
    private Traveler traveler;

    /*@Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        clearData();
    }*/

    // todo: use with logout filter
    public void clearData() {
        name = null;
        personalData = null;
        traveler = null;
    }

    public void loadCurrentUserName() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        name = user.getUsername();
    }

    public void loadCurrentUserPersonalData() {
        if(name == null) {
            loadCurrentUserName();
        }
        personalData = restUrlAccessor.loadPersonalDataForTraveler(name);
    }

    public void loadCurrentUserTravelerData() {
        if (personalData == null) {
            loadCurrentUserPersonalData();
        }
        traveler = restUrlAccessor.getTravelerByPersonalDataId(personalData.getId());
    }

    public void loadAllData() {
        loadCurrentUserName();
        if(name != null) {
            loadCurrentUserPersonalData();
        }
        if(personalData != null) {
            loadCurrentUserTravelerData();
        }
        // todo: error handling
    }

    public String getName() {
        if(name == null) {
            loadAllData();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonalData getPersonalData() {
        if(personalData == null) {
            loadAllData();
        }
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public Traveler getTraveler() {
        if(traveler == null) {
            loadAllData();
        }
        return traveler;
    }

    public void setTraveler(Traveler traveler) {
        this.traveler = traveler;
    }
}
