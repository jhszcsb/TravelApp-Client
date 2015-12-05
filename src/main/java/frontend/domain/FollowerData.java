package frontend.domain;

public class FollowerData {

    private int id;

    private Traveler follower;

    private Traveler followed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Traveler getFollower() {
        return follower;
    }

    public void setFollower(Traveler follower) {
        this.follower = follower;
    }

    public Traveler getFollowed() {
        return followed;
    }

    public void setFollowed(Traveler followed) {
        this.followed = followed;
    }
}
