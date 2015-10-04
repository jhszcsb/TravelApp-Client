package frontend.domain;

public class Friendship {

    private int id;

    private Traveler traveler1;

    private Traveler traveler2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Traveler getTraveler1() {
        return traveler1;
    }

    public void setTraveler1(Traveler traveler1) {
        this.traveler1 = traveler1;
    }

    public Traveler getTraveler2() {
        return traveler2;
    }

    public void setTraveler2(Traveler traveler2) {
        this.traveler2 = traveler2;
    }
}
