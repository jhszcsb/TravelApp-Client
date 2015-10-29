package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

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

    public String getCurrentUserName() {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //String name = user.getUsername();
        //return name;
        return "ASD";
    }

    public String register() {    // todo: use spring ModelAndView?
        RestUrlAccessor accessor = new RestUrlAccessor();
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
        accessor.registerTraveler(newTraveler);
        return "main";
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
