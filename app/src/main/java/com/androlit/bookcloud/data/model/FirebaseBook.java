package com.androlit.bookcloud.data.model;

/**
 * Created by rubel on 6/7/2017.
 */

public class FirebaseBook {

    private String title;
    private String author;
    private String description;
    private long isbn;
    private String offer;
    private String photoUrl;
    private int price;
    private String condition;

    public FirebaseBook(){
        this(null, null, null, 0, null, null);
    }

    public FirebaseBook(String title, String author, String description, long isbn, String offer, String photoUrl) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.offer = offer;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "{" + this.title + "," + this.author + "," + this.description
                + "," + this.isbn + "," +  this.photoUrl + ", " + this.offer + "}";
    }
}
