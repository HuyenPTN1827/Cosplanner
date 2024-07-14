package fpt.huyenptnhe160769.cosplanner.models;

public class CPImage {
    private long fkCos;
    private long id;
    private String notes;
    private int order;
    private String urlImage;
    private String urlThumb;

    public CPImage() {
        this.id = 0;
        this.fkCos = 0;
        this.urlThumb = "";
        this.urlImage = "";
        this.order = 0;
        this.notes = "";
    }

    public CPImage(long id2, long fkCos2, String urlThumb2, String urlImage2, int order2, String notes2) {
        this.id = id2;
        this.fkCos = fkCos2;
        this.urlThumb = urlThumb2;
        this.urlImage = urlImage2;
        this.order = order2;
        this.notes = notes2;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public long getFkCos() {
        return this.fkCos;
    }

    public void setFkCos(long fkCos2) {
        this.fkCos = fkCos2;
    }

    public String getUrlThumb() {
        return this.urlThumb;
    }

    public int getOrder() {
        return this.order;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setUrlThumb(String urlThumb2) {
        this.urlThumb = urlThumb2;
    }

    public String getUrlImage() {
        return this.urlImage;
    }

    public void setUrlImage(String urlImage2) {
        this.urlImage = urlImage2;
    }

    public void setOrder(int o) {
        this.order = o;
    }

    public void setNotes(String n) {
        this.notes = n;
    }
}
