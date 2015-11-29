package frontend.rest;

import com.fasterxml.jackson.databind.JsonNode;
import frontend.domain.FollowRequest;
import frontend.domain.FollowerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class FollowerResourceHelper {

    public static final String HOST = "localhost";
    public static final String PORT = "8099";
    public static final String BASE_URL = "http://" + HOST + ":" + PORT + "/";
    public static final String URL_TRAVELER = BASE_URL + "travelers/";
    public static final String URL_TRIP = BASE_URL + "trips/";
    public static final String URL_FOLLOW= BASE_URL + "follows/";

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    RestHelper restHelper;

    public List<FollowerData> loadAllFollowsForTraveler(String id) {
        ResponseEntity<FollowerData[]> responseData = restTemplate.exchange(BASE_URL + id + "/follows", HttpMethod.GET, restHelper.createAuthenticatedRequest(), FollowerData[].class);
        FollowerData[] travelerArray = responseData.getBody();
        List<FollowerData> travelers = Arrays.asList(travelerArray);
        return travelers;
    }

    public void createFollowed(String traveler1_name, String traveler2_name) {
        FollowRequest followRequest = new FollowRequest(traveler1_name, traveler2_name);
        String json = restHelper.writeValue(followRequest);
        if(json != null) {
            JsonNode jsonNode = restHelper.prepareJsonObject(json);
            restTemplate.exchange(BASE_URL + "/follows", HttpMethod.POST, restHelper.createAuthenticatedRequestWithData(jsonNode), Object.class);
        }
    }
}
