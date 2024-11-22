package com.nehalmarshallopes.assignment3;

import java.io.Serializable;

public class Artwork implements Serializable {

    private String title;
    private String image_id;
    private String date_display;
    private String artist_display;
    private String department_title;
    private String gallery_title;
    private String gallery_id;
    private String place_of_origin;
    private String artwork_type_title;
    private String medium_display;
    private String credit_line;
    private String dimensions;
    private String api_link;
    private String id;

    // Here the constructor will initialize the values of the artwork
    public Artwork(String title, String image_id, String date_display, String artist_display,
                   String department_title, String gallery_title, String gallery_id,
                   String place_of_origin, String artwork_type_title, String medium_display,
                   String credit_line, String dimensions, String api_link, String id) {

        this.title = title;
        this.image_id = image_id;
        this.date_display = date_display;
        this.artist_display = artist_display;
        this.department_title = department_title;
        this.gallery_title = gallery_title;
        this.gallery_id = gallery_id;
        this.place_of_origin = place_of_origin;
        this.artwork_type_title = artwork_type_title;
        this.medium_display = medium_display;
        this.credit_line = credit_line;
        this.dimensions = dimensions;
        this.api_link = api_link;
        this.id = id;
    }

  // Here the the method will return the title of the artwork
    public String getTitle() {
        return title;
    }

    // Here the the method will return the image id of the artwork
    public String getImage_id() {

        return image_id;
    }

    // Here this method returns the thumbnail URL for the artwork
    public String getThumbnailUrl() {

        return "https://www.artic.edu/iiif/2/" + getImage_id() + "/full/200,/0/default.jpg";
    }

    // Here the the method will return the date of the artwork
    public String getDate_display() {

        return date_display;
    }

    // Here the the method will return the artist of the artwork
    public String getArtist_display() {

        return artist_display;
    }

    // Here the the method will return the department title of the artwork
    public String getDepartment_title() {

        return department_title;
    }

    // Here the the method will return the gallery title of the artwork
    public String getGallery_title() {

        return gallery_title;
    }

    // Here the the method will return the gallery id of the artwork
    public String getGallery_id() {
        return gallery_id;
    }

    // Here the the method will return the place of origin of the artwork
    public String getPlace_of_origin() {

        return place_of_origin;
    }

    // Here the the method will return the artwork type title of the artwork
    public String getArtwork_type_title() {

        return artwork_type_title;
    }

    // Here the the method will return the medium display of the artwork
    public String getMedium_display() {

        return medium_display;
    }

    // Here the the method will return the credit line of the artwork
    public String getCredit_line() {

        return credit_line;
    }

    // Here the the method will return the dimensions of the artwork
    public String getDimensions() {
        return dimensions;
    }

    // Here the the method will return the API link of the artwork
    public String getApi_link() {
        return api_link;
    }

    // Here the the method will return the id of the artwork
    public String getId() {

        return id;
    }
}
