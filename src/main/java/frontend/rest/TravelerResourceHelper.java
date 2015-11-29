package frontend.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class TravelerResourceHelper {
    public static final String HOST = "localhost";
    public static final String PORT = "8099";
    public static final String BASE_URL = "http://" + HOST + ":" + PORT + "/";
    public static final String URL_TRAVELER = BASE_URL + "travelers/";

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    RestHelper restHelper;

    @Autowired
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public List<Traveler> loadAllTravelers() {
        ResponseEntity<Traveler[]> responseData = restTemplate.exchange(URL_TRAVELER, HttpMethod.GET, restHelper.createAuthenticatedRequest(), Traveler[].class);
        Traveler[] travelerArray = responseData.getBody();
        List<Traveler> travelers = Arrays.asList(travelerArray);
        return travelers;
    }

    public void getTravelerById(String id) {
        ResponseEntity<Object> responseData = restTemplate.getForEntity(URL_TRAVELER + id, Object.class);
    }

    public void registerTraveler(Traveler newTraveler) {    // todo add a return value (string message or else)
        String json = null;
        String encoded = passwordEncoder.encode(newTraveler.getPersonaldata().getPassword());
        newTraveler.getPersonaldata().setPassword(encoded);
        if (encoded != null) json = restHelper.writeValue(newTraveler);
        if(json != null){
            JsonNode jsonNode = restHelper.prepareJsonObject(json);
            restTemplate.exchange(URL_TRAVELER, HttpMethod.POST, restHelper.createAuthenticatedRequestWithData(jsonNode), Object.class, jsonNode);
        }
    }

    public PersonalData loadPersonalDataForTraveler(String name) {
        ResponseEntity<PersonalData> responseData = restTemplate.exchange(URL_TRAVELER + name + "/personaldata", HttpMethod.GET, restHelper.createAuthenticatedRequest(), PersonalData.class);
        PersonalData personalData = responseData.getBody();
        return personalData;
    }

    public void updatePersonalDataForTraveler(PersonalData personalData) {
        byte[] bytes = personalData.getProfilepic();
        String base64String = java.util.Base64.getEncoder().encodeToString(bytes);
        personalData.setProfilepic(null);
        personalData.setDiplayablePicture(null);

        String json = restHelper.writeValue(personalData);
        if(json != null) {
            JsonNode jsonNode = restHelper.prepareJsonObject(json);
            ((ObjectNode)jsonNode).remove("profilepic");
            ((ObjectNode)jsonNode).put("profilepic", base64String); // encode picture for json
            restTemplate.exchange(URL_TRAVELER + "/personaldata/" + personalData.getId(), HttpMethod.PUT, restHelper.createAuthenticatedRequestWithData(jsonNode), Object.class, jsonNode);
        }
    }

    public Traveler getTravelerByPersonalDataId(int personalDataId) {
        ResponseEntity<Traveler> responseData = restTemplate.exchange(URL_TRAVELER + "/personaldata/" + personalDataId, HttpMethod.GET, restHelper.createAuthenticatedRequest(), Traveler.class);
        Traveler traveler = responseData.getBody();
        return traveler;
    }
}
