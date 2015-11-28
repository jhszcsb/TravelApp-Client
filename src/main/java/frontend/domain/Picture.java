package frontend.domain;

import org.primefaces.model.DefaultStreamedContent;

public class Picture {

    private int id;

    private byte[] data;

    private DefaultStreamedContent diplayablePicture;

    private int gallery;

    private int place;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getGallery() {
        return gallery;
    }

    public void setGallery(int gallery) {
        this.gallery = gallery;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public DefaultStreamedContent getDiplayablePicture() {
        return diplayablePicture;
    }

    public void setDiplayablePicture(DefaultStreamedContent diplayablePicture) {
        this.diplayablePicture = diplayablePicture;
    }
}
