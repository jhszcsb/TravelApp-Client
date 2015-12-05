package frontend.domain;

public class FollowRequest {

    private String follower_name;

    private String followed_name;

    public FollowRequest() {}

    public FollowRequest(String follower_name, String followed_name) {
        this.follower_name = follower_name;
        this.followed_name = followed_name;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    public String getFollowed_name() {
        return followed_name;
    }

    public void setFollowed_name(String followed_name) {
        this.followed_name = followed_name;
    }
}
