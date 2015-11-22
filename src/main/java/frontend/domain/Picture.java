package frontend.domain;

public class Picture {

    private int id;

    private byte[] data;

    private int gallery_id;

    private int places_id;

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

    public int getGallery_id() {
        return gallery_id;
    }

    public void setGallery_id(int gallery_id) {
        this.gallery_id = gallery_id;
    }

    public int getPlaces_id() {
        return places_id;
    }

    public void setPlaces_id(int places_id) {
        this.places_id = places_id;
    }
}
