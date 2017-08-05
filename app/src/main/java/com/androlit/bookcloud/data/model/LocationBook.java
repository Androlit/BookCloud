package com.androlit.bookcloud.data.model;

/**
 * Created by rubel on 8/5/2017.
 */

public class LocationBook {
    Float distance;
    FirebaseBook book;

    public LocationBook() {
    }

    public LocationBook(Float distance, FirebaseBook book) {
        this.distance = distance;
        this.book = book;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public FirebaseBook getBook() {
        return book;
    }

    public void setBook(FirebaseBook book) {
        this.book = book;
    }
}
