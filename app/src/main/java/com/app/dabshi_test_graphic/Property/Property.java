package com.app.dabshi_test_graphic.Property;


public class Property   {
    private String Idproperty;
    private String Idowner;

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        this.ImageLink = imageLink;
    }

    private String ImageLink;
    private String Title;
    private String Location;
    private String Description;
    private String Rooms;
    private String Price;

    public Property(String idowner,String idproperty, String title, String location, String description, String rooms, String price,String imageLink) {
        Idowner = idowner;
        Title = title;
        Location = location;
        Description = description;
        Rooms = rooms;
        Price = price;
        Idproperty = idproperty;
        ImageLink =  imageLink;
    }

    public Property() {}

    public Property(String idowner,String idproperty, String title, String location, String description, String rooms, String price) {
        Idowner = idowner;
        Title = title;
        Location = location;
        Description = description;
        Rooms = rooms;
        Price = price;
        Idproperty = idproperty;
    }


    public String getIdproperty() {
        return Idproperty;
    }

    public void setIdproperty(String Idproperty) {
        this.Idproperty = Idproperty;
    }

    public String getIdowner() {
        return Idowner;
    }

    public void setIdowner(String Idowner) {
        this.Idowner = Idowner;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getRooms() {
        return Rooms;
    }

    public void setRooms(String Rooms) {
        this.Rooms = Rooms;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

}
