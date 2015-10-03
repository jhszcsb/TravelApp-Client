package frontend.domain;

public class Traveler {

    private int id;

    private PersonalData personaldata;

    //private int socialdata;

    public Traveler() {}

    public Traveler(int id, PersonalData personaldata/*, int socialdata*/) {
        this.id = id;
        this.personaldata = personaldata;
        /*this.socialdata = socialdata;*/
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonalData getPersonaldata() {
        return personaldata;
    }

    public void setPersonaldata(PersonalData personaldata) {
        this.personaldata = personaldata;
    }

    /*public int getSocialdata() {
        return socialdata;
    }

    public void setSocialdata(int socialdata) {
        this.socialdata = socialdata;
    }*/
}
