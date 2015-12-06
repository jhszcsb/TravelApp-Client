package frontend.domain;

public class SocialData {

    private int id;

    private String facebook_account;

    private String twitter_account;

    private String instagram_account;

    private String tumblr_account;

    private String youtube_account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacebook_account() {
        return facebook_account;
    }

    public void setFacebook_account(String facebook_account) {
        this.facebook_account = facebook_account;
    }

    public String getTwitter_account() {
        return twitter_account;
    }

    public void setTwitter_account(String twitter_account) {
        this.twitter_account = twitter_account;
    }

    public String getInstagram_account() {
        return instagram_account;
    }

    public void setInstagram_account(String instagram_account) {
        this.instagram_account = instagram_account;
    }

    public String getTumblr_account() {
        return tumblr_account;
    }

    public void setTumblr_account(String tumblr_account) {
        this.tumblr_account = tumblr_account;
    }

    public String getYoutube_account() {
        return youtube_account;
    }

    public void setYoutube_account(String youtube_account) {
        this.youtube_account = youtube_account;
    }
}
