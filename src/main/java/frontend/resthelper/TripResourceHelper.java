package frontend.resthelper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import frontend.domain.Picture;
import frontend.domain.Place;
import frontend.domain.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class TripResourceHelper {

    public static final String HOST = "localhost";
    public static final String PORT = "8099";
    public static final String BASE_URL = "http://" + HOST + ":" + PORT + "/";
    public static final String URL_TRIP = BASE_URL + "trips/";

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    RestHelper restHelper;

    public List<Trip> loadAllTripsForTraveler(String id) {
        ResponseEntity<Trip[]> responseData = restTemplate.exchange(BASE_URL + id + "/trips", HttpMethod.GET, restHelper.createAuthenticatedRequest(), Trip[].class);
        Trip[] tripArray = responseData.getBody();
        List<Trip> trips = Arrays.asList(tripArray);
        return trips;
    }

    public List<Trip> loadAllTripsOfFollowedsForTraveler(String name) {
        ResponseEntity<Trip[]> responseData = restTemplate.exchange(BASE_URL + name + "/timeline", HttpMethod.GET, restHelper.createAuthenticatedRequest(), Trip[].class);
        Trip[] tripArray = responseData.getBody();
        List<Trip> trips = Arrays.asList(tripArray);
        return trips;
    }

    public Trip createTrip(int id) {
        return restTemplate.exchange(BASE_URL + id + "/trips", HttpMethod.POST, restHelper.createAuthenticatedRequest(), Trip.class).getBody();
    }

    public void updateTrip(Trip trip) {
        String json = restHelper.writeValue(trip);
        if(json != null) {
            JsonNode jsonNode = restHelper.prepareJsonObject(json);
            ((ObjectNode)jsonNode).remove("traveler");  // traveler node is not needed
            ((ObjectNode)jsonNode).remove("place");  // place node is not needed
            restTemplate.exchange(URL_TRIP, HttpMethod.PUT, restHelper.createAuthenticatedRequestWithData(jsonNode), Object.class);
        }
    }

    public void deleteTrip(int selectedTripId) {
        restTemplate.exchange(URL_TRIP + selectedTripId, HttpMethod.DELETE, restHelper.createAuthenticatedRequest(), Object.class);
    }

    public void addPlaceForTrip(Place newPlace, int tripId) {
        newPlace.setTrip(tripId);
        String json = restHelper.writeValue(newPlace);
        if(json != null) {
            JsonNode jsonNode = restHelper.prepareJsonObject(json);
            ((ObjectNode)jsonNode).remove("trip");  // trip node is not needed
            restTemplate.exchange(URL_TRIP + tripId + "/place", HttpMethod.POST,
                    restHelper.createAuthenticatedRequestWithData(jsonNode), Object.class);
        }
    }

    public void uploadPicturesForTrip(int galleryId, int placeId, List<Picture> pictures) {
        String json = null;
        // improvement: use one http request for multiple images?

        for(Picture picture : pictures) {
            picture.setPlace(placeId);
            picture.setDiplayablePicture(null);
            byte[] bytes = picture.getData();
            String base64String = java.util.Base64.getEncoder().encodeToString(bytes);
            json = restHelper.writeValue(picture);
            if(json != null) {
                JsonNode jsonNode = restHelper.prepareJsonObject(json);
                ((ObjectNode)jsonNode).remove("data");
                ((ObjectNode)jsonNode).put("data", base64String); // encode picture for json
                restTemplate.exchange(BASE_URL + "/gallery/" + galleryId + "/pictures", HttpMethod.POST,
                        restHelper.createAuthenticatedRequestWithData(jsonNode), Object.class);
            }
        }
    }

    public List<Picture> loadPicturesForTripByGalleryId(int galleryId) {
        ResponseEntity<Picture[]> responseData = restTemplate.exchange(BASE_URL + "gallery/" + galleryId + "/pictures", HttpMethod.GET, restHelper.createAuthenticatedRequest(), Picture[].class);
        Picture[] pictureArray = responseData.getBody();
        List<Picture> pictures = Arrays.asList(pictureArray);
        return pictures;
    }

    public List<Picture> loadPicturesForPlaceByPlaceId(int placeId) {
        ResponseEntity<Picture[]> responseData = restTemplate.exchange(BASE_URL + "place/" + placeId + "/pictures", HttpMethod.GET, restHelper.createAuthenticatedRequest(), Picture[].class);
        Picture[] pictureArray = responseData.getBody();
        List<Picture> pictures = Arrays.asList(pictureArray);
        return pictures;
    }

    public Resources<Place> loadAllPlacesForTrip(int trip_id) {
        ResponseEntity<Resource[]> responseData = restTemplate.exchange(
                BASE_URL + "hateoas/trips/" + trip_id + "/place",
                HttpMethod.GET,
                restHelper.createAuthenticatedRequest(),
                Resource[].class
        );
        return null;
        //List<Place> places = placeResources.stream().map(Resource<Place>::getContent).collect(Collectors.toList());
        //return places;
    }

    public Resource<Place> loadOnePlace() {
        ResponseEntity<Resource<Place>> responseData = restTemplate.exchange(
                BASE_URL + "hateoas/one/trips/place/",
                HttpMethod.GET,
                restHelper.createAuthenticatedRequest(),
                new ParameterizedTypeReference<Resource<Place>>() {}
        );
        return null;
    }
}
