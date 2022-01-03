package com.app.dabshi_test_graphic.Property;

public class PropertyImages {
    private String Idimage, Imagename;

    public PropertyImages(String Idimage, String Imagename) {
        this.Idimage = Idimage;
        this.Imagename = Imagename;
    }

    public PropertyImages(String idimage) {
        Idimage = idimage;
    }

    public String getIdimage() {
        return Idimage;
    }

    public void setIdimage(String Idimage) {
        this.Idimage = Idimage;
    }

    public String getImagename() {
        return Imagename;
    }

    public void setImagename(String Imagename) {
        this.Imagename = Imagename;
    }
}
