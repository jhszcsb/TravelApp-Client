package frontend.domain;

import java.util.Date;

public class Place {

    private int id;

    private String name;

    private String description;

    private int trip;

    private Date arrived;

    private Date left;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTrip() {
        return trip;
    }

    public void setTrip(int trip) {
        this.trip = trip;
    }

    public Date getArrived() {
        return arrived;
    }

    public void setArrived(Date arrived) {
        this.arrived = arrived;
    }

    public Date getLeft() {
        return left;
    }

    public void setLeft(Date left) {
        this.left = left;
    }
}
