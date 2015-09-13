package frontend;

public class Traveler {

    private int id;

    private int personalDataId;

    private int socialDataId;

    private int friendshipDataId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonalDataId() {
        return personalDataId;
    }

    public void setPersonalDataId(int personalDataId) {
        this.personalDataId = personalDataId;
    }

    public int getSocialDataId() {
        return socialDataId;
    }

    public void setSocialDataId(int socialDataId) {
        this.socialDataId = socialDataId;
    }

    public int getFriendshipDataId() {
        return friendshipDataId;
    }

    public void setFriendshipDataId(int friendshipDataId) {
        this.friendshipDataId = friendshipDataId;
    }
}
