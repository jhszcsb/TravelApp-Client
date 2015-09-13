package frontend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class RestUrlAccessor {
//todo: create universal resturl accessor (base url + scope from parameter)

    public static final String URL = "http://localhost:8070/travelers/";

    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    public void getTravelers() {
        ResponseEntity<Object> responseData = restTemplate.getForEntity(URL, Object.class);
        printData(responseData);
    }

    public void getTravelerById(String id) {
        ResponseEntity<Object> responseData = restTemplate.getForEntity(URL + id, Object.class);
        printData(responseData);
    }

    public void createNewTraveler() {
        // todo: use more universal json parsing (from object)
        JsonNode node = prepareJsonObject("{\"id\":3,\"personaldata\":null,\"socialdata\":null,\"friendshipdata\":{\"id\":1}}");
        restTemplate.postForObject(URL, node, JsonNode.class);
    }

    public void updateTraveler(String id) {
        JsonNode node = prepareJsonObject("{\"id\":" + id + ",\"personaldata\":null,\"socialdata\":null,\"friendshipdata\":{\"id\":2}}");
        restTemplate.put(URL, node);
    }

    public void deleteTraveler(String id) {
        restTemplate.delete(URL + id);
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

}
