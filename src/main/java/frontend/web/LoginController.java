package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Controller
@Scope("request")
public class LoginController {

    private String username;
    private String password;

    private String registerUsername;
    private String registerPassword;
    private String registerEmail;
    private String registerFirstname;
    private String registerLastname;

    @Autowired
    RestUrlAccessor restUrlAccessor;

    @Autowired
    CurrentUserService currentUserService;

    public String getCurrentUserName() {
        return currentUserService.getName();
    }

    public String register() {    // todo: use spring ModelAndView?
        // todo: check for duplicate users
        Traveler newTraveler = new Traveler();
        PersonalData personalData = new PersonalData();
        personalData.setPassword(registerPassword);
        personalData.setUsername(registerUsername);
        personalData.setEmail(registerEmail);
        personalData.setFirstname(registerFirstname);
        personalData.setLastname(registerLastname);
        personalData.setHometown("dummy");  // todo: use geolocation
        newTraveler.setPersonaldata(personalData);
        restUrlAccessor.registerTraveler(newTraveler);
        // TODO: add error message if registration is not successful (error from backend)
        FacesMessage message = new FacesMessage("Successful registration!");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
        return "login";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterUsername() {
        return registerUsername;
    }

    public void setRegisterUsername(String registerUsername) {
        this.registerUsername = registerUsername;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }

    public String getRegisterEmail() {
        return registerEmail;
    }

    public void setRegisterEmail(String registerEmail) {
        this.registerEmail = registerEmail;
    }

    public String getRegisterFirstname() {
        return registerFirstname;
    }

    public void setRegisterFirstname(String registerFirstname) {
        this.registerFirstname = registerFirstname;
    }

    public String getRegisterLastname() {
        return registerLastname;
    }

    public void setRegisterLastname(String registerLastname) {
        this.registerLastname = registerLastname;
    }
}
