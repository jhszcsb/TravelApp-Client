package frontend.domain;

public class FriendRequest {

    private String traveler1_name;

    private String traveler2_name;

    public FriendRequest() {}

    public FriendRequest(String traveler1_name, String traveler2_name) {
        this.traveler1_name = traveler1_name;
        this.traveler2_name = traveler2_name;
    }

    public String getTraveler1_name() {
        return traveler1_name;
    }

    public void setTraveler1_name(String traveler1_name) {
        this.traveler1_name = traveler1_name;
    }

    public String getTraveler2_name() {
        return traveler2_name;
    }

    public void setTraveler2_name(String traveler2_name) {
        this.traveler2_name = traveler2_name;
    }
}
