package frontend.resthelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

@Component
public class RestHelper {
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

    public void printTravelersToConsole() {
        ResponseEntity<Object> responseData = restTemplate.getForEntity(URL_TRAVELER, Object.class);
        printData(responseData);
    }

    JsonNode prepareJsonObject(String jsonString) {
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

    public HttpEntity<String> createAuthenticatedRequest() {
        String plainCreds = "user:user";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        return request;
    }

    public HttpEntity<Map> createAuthenticatedRequestWithData(JsonNode body) {
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

    String writeValue(Object o) {
        String json = null;
        try {
            json = ow.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}