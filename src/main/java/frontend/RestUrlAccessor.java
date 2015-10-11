package frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import frontend.domain.Friendship;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import frontend.domain.Trip;
import frontend.security.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class RestUrlAccessor {
//todo: create universal resturl accessor (base url + scope from parameter)

    public static final String URL_TRAVELER = "http://localhost:8070/travelers/";
    public static final String BASE_URL = "http://localhost:8070/";
    public static final String URL_AUTHENTICATION = "authenticationdata";

    //@Autowired
    RestTemplate restTemplate = new RestTemplate();

    public User loadUserByUsername() {
        User user = new User();
        ResponseEntity<User> responseData = restTemplate.getForEntity(BASE_URL + URL_AUTHENTICATION, User.class);
        user.setUsername(responseData.getBody().getUsername());
        user.setPassword(responseData.getBody().getPassword());
        return user;
    }

    public List<Traveler> loadAllTravelers() {
        ResponseEntity<Traveler[]> responseData = restTemplate.getForEntity(URL_TRAVELER, Traveler[].class);
        Traveler[] travelerArray = responseData.getBody();
        List<Traveler> travelers = Arrays.asList(travelerArray);
        return travelers;
    }

    public List<Friendship> loadAllFriendsForTraveler(String id) {
        ResponseEntity<Friendship[]> responseData = restTemplate.getForEntity(BASE_URL + id + "/friendships", Friendship[].class);
        Friendship[] travelerArray = responseData.getBody();
        List<Friendship> travelers = Arrays.asList(travelerArray);
        return travelers;
    }

    public List<Trip> loadAllTripsForTraveler(String id) {
        ResponseEntity<Trip[]> responseData = restTemplate.getForEntity(BASE_URL + id + "/trips", Trip[].class);
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
        try {
            json = ow.writeValueAsString(newTraveler);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(json != null){
            JsonNode jsonNode = prepareJsonObject(json);
            restTemplate.postForObject(URL_TRAVELER, jsonNode, JsonNode.class);
        }
        System.out.println("Registering new Traveler: " + json);
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
}
