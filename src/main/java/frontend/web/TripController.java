package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.PersonalData;
import frontend.domain.Traveler;
import frontend.domain.Trip;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("request")
public class TripController {

    //@Autowired    //todo: add dependency injection
    RestUrlAccessor restUrlAccessor = new RestUrlAccessor();

    private List<Trip> trips = new ArrayList<>();
    private List<Trip> tripsOfFriends = new ArrayList<>();
    private Trip selectedTrip;

    public String loadTripsForTraveler() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   // todo: refactor to a variable
        String name = user.getUsername();
        PersonalData personalData = restUrlAccessor.loadPersonalataForTraveler(name);   // todo: optimize (move all queries to backend)
        Traveler traveler = restUrlAccessor.getTravelerByPersonalDataId(personalData.getId());
        trips = restUrlAccessor.loadAllTripsForTraveler(String.valueOf(traveler.getId()));
        return "trips";
    }

    public void loadAllTripsOfFriends() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();   // todo: refactor to a variable
        String name = user.getUsername();
        tripsOfFriends = restUrlAccessor.loadAllTripsOfFriendsForTraveler(name);
    }

    public String loadTrip(Trip trip) {
        selectedTrip = trip;
        return "tripprofile";
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Trip> getTripsOfFriends() {
        return tripsOfFriends;
    }

    public void setTripsOfFriends(List<Trip> tripsOfFriends) {
        this.tripsOfFriends = tripsOfFriends;
    }

    public Trip getSelectedTrip() {
        return selectedTrip;
    }

    public void setSelectedTrip(Trip selectedTrip) {
        this.selectedTrip = selectedTrip;
    }
}
