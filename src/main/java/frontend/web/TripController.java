package frontend.web;

import frontend.RestUrlAccessor;
import frontend.domain.Trip;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("request")
public class TripController {

    //@Autowired    //todo: add dependency injection
    RestUrlAccessor restUrlAccessor = new RestUrlAccessor();

    private List<Trip> trips = new ArrayList<>();

    public void loadTripsForTraveler() {
        trips = restUrlAccessor.loadAllTripsForTraveler("1");   // todo: id is hardcoded
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}
