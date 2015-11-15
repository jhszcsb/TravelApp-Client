package frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import frontend.domain.*;
import frontend.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

@Component
public class RestUrlAccessor {
//todo: create universal resturl accessor (base url + scope from parameter)

    public static final String URL_TRAVELER = "http://localhost:8099/travelers/";
    public static final String BASE_URL = "http://localhost:8099/";
    public static final String URL_AUTHENTICATION = "authenticationdata/";

    @Autowired
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    //@Autowired
    RestTemplate restTemplate = new RestTemplate();

    public User loadUserByUsername(String username) {
        User user = new User();
        // TODO: add auth for other services
        ResponseEntity<User> responseData = restTemplate.exchange(
                BASE_URL + URL_AUTHENTICATION + username,
                HttpMethod.GET,
                createAuthenticatedRequest(),
                User.class);
        user.setUsername(responseData.getBody().getUsername());
        user.setPassword(responseData.getBody().getPassword());
        return user;
    }

    public List<Traveler> loadAllTravelers() {
        //ResponseEntity<Traveler[]> responseData = restTemplate.getForEntity(URL_TRAVELER, Traveler[].class);
        ResponseEntity<Traveler[]> responseData = restTemplate.exchange(URL_TRAVELER, HttpMethod.GET, createAuthenticatedRequest(), Traveler[].class);
        Traveler[] travelerArray = responseData.getBody();
        List<Traveler> travelers = Arrays.asList(travelerArray);
        return travelers;
    }

    public List<Friendship> loadAllFriendsForTraveler(String id) {
        ResponseEntity<Friendship[]> responseData = restTemplate.exchange(BASE_URL + id + "/friendships", HttpMethod.GET, createAuthenticatedRequest(), Friendship[].class);
        Friendship[] travelerArray = responseData.getBody();
        List<Friendship> travelers = Arrays.asList(travelerArray);
        return travelers;
    }

    public List<Trip> loadAllTripsForTraveler(String id) {
        ResponseEntity<Trip[]> responseData = restTemplate.exchange(BASE_URL + id + "/trips", HttpMethod.GET, createAuthenticatedRequest(), Trip[].class);
        Trip[] tripArray = responseData.getBody();
        List<Trip> trips = Arrays.asList(tripArray);
        return trips;
    }

    public void printTravelersToConsole() {
        ResponseEntity<Object> responseData = restTemplate.getForEntity(URL_TRAVELER, Object.class);
        printData(responseData);
    }

    public void getTravelerById(String id) {
        ResponseEntity<Object> responseData = restTemplate.getForEntity(URL_TRAVELER + id, Object.class);
        printData(responseData);
    }

    public void registerTraveler(Traveler newTraveler) {    // todo add a return value (string message or else)
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        //System.out.println(newTraveler.getPersonaldata().getPassword());    // todo delete
        String encoded = passwordEncoder.encode(newTraveler.getPersonaldata().getPassword());
        newTraveler.getPersonaldata().setPassword(encoded);
        if (encoded != null)
            //System.out.println(encoded);
        try {
            json = ow.writeValueAsString(newTraveler);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(json != null){
            JsonNode jsonNode = prepareJsonObject(json);
            //restTemplate.postForObject(URL_TRAVELER, jsonNode, JsonNode.class);
            // todo: password is not set on server side when registering
            restTemplate.exchange(URL_TRAVELER, HttpMethod.POST, createAuthenticatedRequestWithData(jsonNode), Object.class, jsonNode);
        }
        System.out.println("Registering new Traveler: " + json);
        // TODO: add password hashing
    }

    /*public void createNewTraveler() {
        // todo: use more universal json parsing (from object)
        JsonNode node = prepareJsonObject("{\"id\":3,\"personaldata\":null,\"socialdata\":null,\"friendshipdata\":{\"id\":1}}");
        restTemplate.postForObject(URL_TRAVELER, node, JsonNode.class);
    }

    public void updateTraveler(String id) {
        JsonNode node = prepareJsonObject("{\"id\":" + id + ",\"personaldata\":null,\"socialdata\":null,\"friendshipdata\":{\"id\":2}}");
        restTemplate.put(URL_TRAVELER, node);
    }

    public void deleteTraveler(String id) {
        restTemplate.delete(URL_TRAVELER + id);
    }*/

    private JsonNode prepareJsonObject(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actualObj;
    }

    private void printData(ResponseEntity<Object> responseData) {
        if(responseData != null) {
            System.out.println(responseData.getStatusCode());
            System.out.println();
            System.out.println(responseData.getHeaders());
            System.out.println();
            System.out.println(responseData.getBody().toString());
            System.out.println();
        }
        else {
            System.out.println("No Data found!");
        }
    }

    private HttpEntity<String> createAuthenticatedRequest() {
        String plainCreds = "user:user";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        return request;
    }

    private HttpEntity<Map> createAuthenticatedRequestWithData(JsonNode body) {
        String plainCreds = "user:user";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(body, Map.class);
        HttpEntity<Map> request = new HttpEntity<Map>(map, headers);
        return request;
    }

    public PersonalData loadPersonalataForTraveler(String name) {
        ResponseEntity<PersonalData> responseData = restTemplate.exchange(URL_TRAVELER + name + "/personaldata", HttpMethod.GET, createAuthenticatedRequest(), PersonalData.class);
        PersonalData personalData = responseData.getBody();
        return personalData;
    }

    public void updatePersonalDataForTraveler(PersonalData personalData) {
        // todo: error handling (null)
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;

        byte[] bytes = personalData.getProfilepic();
        String base64String = java.util.Base64.getEncoder().encodeToString(bytes);
        personalData.setProfilepic(null);
        personalData.setDiplayablePicture(null);

        try {
            json = ow.writeValueAsString(personalData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(json != null) {
            JsonNode jsonNode = prepareJsonObject(json);
            ((ObjectNode)jsonNode).remove("profilepic");
            ((ObjectNode)jsonNode).put("profilepic", base64String);
            //System.out.println(jsonNode);
            restTemplate.exchange(URL_TRAVELER + personalData.getId() + "/personaldata", HttpMethod.PUT, createAuthenticatedRequestWithData(jsonNode), Object.class, jsonNode);
        }
    }

    public Traveler getTravelerByPersonalDataId(int personalDataId) {
        ResponseEntity<Traveler> responseData = restTemplate.exchange(URL_TRAVELER + "/personaldataid/" + personalDataId, HttpMethod.GET, createAuthenticatedRequest(), Traveler.class);
        Traveler traveler = responseData.getBody();
        return traveler;
    }

    public List<Trip> loadAllTripsOfFriendsForTraveler(String name) {
        ResponseEntity<Trip[]> responseData = restTemplate.exchange(BASE_URL + name + "/timeline", HttpMethod.GET, createAuthenticatedRequest(), Trip[].class);
        Trip[] tripArray = responseData.getBody();
        List<Trip> trips = Arrays.asList(tripArray);
        return trips;
    }

    public void createFriendship(String traveler1_name, String traveler2_name) {
        FriendRequest friendRequest = new FriendRequest(traveler1_name, traveler2_name);
        // todo: error handling (null)
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(friendRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(json != null) {
            JsonNode jsonNode = prepareJsonObject(json);
            restTemplate.exchange(BASE_URL + "/friendships", HttpMethod.POST, createAuthenticatedRequestWithData(jsonNode), Object.class);
        }
    }
}
