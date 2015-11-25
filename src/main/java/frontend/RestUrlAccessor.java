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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

@Component
public class RestUrlAccessor {
    public static final String HOST = "localhost";
    public static final String PORT = "8099";
    public static final String BASE_URL = "http://" + HOST + ":" + PORT + "/";
    public static final String URL_TRAVELER = BASE_URL + "travelers/";
    public static final String URL_TRIP = BASE_URL + "trips/";
    public static final String URL_FOLLOW= BASE_URL + "follows/";

    public static final String URL_AUTHENTICATION = "authenticationdata/";

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    //@Autowired // fix this injection
    RestTemplate restTemplate = new RestTemplate();

    public User loadUserByUsername(String username) {
        User user = new User();
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
        ResponseEntity<Traveler[]> responseData = restTemplate.exchange(URL_TRAVELER, HttpMethod.GET, createAuthenticatedRequest(), Traveler[].class);
        Traveler[] travelerArray = responseData.getBody();
        List<Traveler> travelers = Arrays.asList(travelerArray);
        return travelers;
    }

    public List<FollowerData> loadAllFollowsForTraveler(String id) {
        ResponseEntity<FollowerData[]> responseData = restTemplate.exchange(BASE_URL + id + "/follows", HttpMethod.GET, createAuthenticatedRequest(), FollowerData[].class);
        FollowerData[] travelerArray = responseData.getBody();
        List<FollowerData> travelers = Arrays.asList(travelerArray);
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
        String json = null;
        String encoded = passwordEncoder.encode(newTraveler.getPersonaldata().getPassword());
        newTraveler.getPersonaldata().setPassword(encoded);
        if (encoded != null) json = writeValue(newTraveler);
        if(json != null){
            JsonNode jsonNode = prepareJsonObject(json);
            restTemplate.exchange(URL_TRAVELER, HttpMethod.POST, createAuthenticatedRequestWithData(jsonNode), Object.class, jsonNode);
        }
    }

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

    public PersonalData loadPersonalDataForTraveler(String name) {
        ResponseEntity<PersonalData> responseData = restTemplate.exchange(URL_TRAVELER + name + "/personaldata", HttpMethod.GET, createAuthenticatedRequest(), PersonalData.class);
        PersonalData personalData = responseData.getBody();
        return personalData;
    }

    public void updatePersonalDataForTraveler(PersonalData personalData) {
        byte[] bytes = personalData.getProfilepic();
        String base64String = java.util.Base64.getEncoder().encodeToString(bytes);
        personalData.setProfilepic(null);
        personalData.setDiplayablePicture(null);

        String json = writeValue(personalData);
        if(json != null) {
            JsonNode jsonNode = prepareJsonObject(json);
            ((ObjectNode)jsonNode).remove("profilepic");
            ((ObjectNode)jsonNode).put("profilepic", base64String); // encode picture for json
            restTemplate.exchange(URL_TRAVELER + "/personaldata/" + personalData.getId(), HttpMethod.PUT, createAuthenticatedRequestWithData(jsonNode), Object.class, jsonNode);
        }
    }

    public Traveler getTravelerByPersonalDataId(int personalDataId) {
        ResponseEntity<Traveler> responseData = restTemplate.exchange(URL_TRAVELER + "/personaldata/" + personalDataId, HttpMethod.GET, createAuthenticatedRequest(), Traveler.class);
        Traveler traveler = responseData.getBody();
        return traveler;
    }

    public List<Trip> loadAllTripsOfFollowedsForTraveler(String name) {
        ResponseEntity<Trip[]> responseData = restTemplate.exchange(BASE_URL + name + "/timeline", HttpMethod.GET, createAuthenticatedRequest(), Trip[].class);
        Trip[] tripArray = responseData.getBody();
        List<Trip> trips = Arrays.asList(tripArray);
        return trips;
    }

    public void createFollowed(String traveler1_name, String traveler2_name) {
        FollowRequest followRequest = new FollowRequest(traveler1_name, traveler2_name);
        String json = writeValue(followRequest);
        if(json != null) {
            JsonNode jsonNode = prepareJsonObject(json);
            restTemplate.exchange(BASE_URL + "/follows", HttpMethod.POST, createAuthenticatedRequestWithData(jsonNode), Object.class);
        }
    }

    public Trip createTrip(int id) {
        return restTemplate.exchange(BASE_URL + id + "/trips", HttpMethod.POST, createAuthenticatedRequest(), Trip.class).getBody();
    }

    public void updateTrip(Trip trip) {
        String json = writeValue(trip);
        if(json != null) {
            JsonNode jsonNode = prepareJsonObject(json);
            ((ObjectNode)jsonNode).remove("traveler");  // traveler node is not needed
            ((ObjectNode)jsonNode).remove("gallery");  // gallery node is not needed
            ((ObjectNode)jsonNode).remove("place");  // place node is not needed
            restTemplate.exchange(URL_TRIP, HttpMethod.PUT, createAuthenticatedRequestWithData(jsonNode), Object.class);
        }
    }

    public void deleteTrip(int selectedTripId) {
        restTemplate.exchange(URL_TRIP + selectedTripId, HttpMethod.DELETE, createAuthenticatedRequest(), Object.class);
    }

    public void addPlaceForTrip(Place newPlace, int tripId) {
        String json = writeValue(newPlace);
        if(json != null) {
            JsonNode jsonNode = prepareJsonObject(json);
            restTemplate.exchange(URL_TRIP + tripId + "/place", HttpMethod.POST,
                    createAuthenticatedRequestWithData(jsonNode), Object.class);
        }
    }



    public void uploadPicturesForTrip(int galleryId, List<Picture> pictures) {
        String json = null;
        for(Picture picture : pictures) {
            json = writeValue(picture);
            if(json != null) {
                JsonNode jsonNode = prepareJsonObject(json);
                // todo: base64 encode
                restTemplate.exchange(BASE_URL + "/gallery/" + galleryId + "/pictures", HttpMethod.POST,
                        createAuthenticatedRequestWithData(jsonNode), Object.class);
            }
        }
    }

    private String writeValue(Object o) {
        String json = null;
        try {
            json = ow.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}