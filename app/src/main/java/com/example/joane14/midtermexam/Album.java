package com.example.joane14.midtermexam;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Joane14 on 12/02/2017.
 */

public class Album {

    private String albumName;
    private String artist;
    private String url;
    private String imageUrl;
    private Bitmap image;

    public Album(String albumName, String artist, String url, String imageUrl) {
        this.albumName = albumName;
        this.artist = artist;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public Album(){

    }

    public String getAlbumName() {
        return albumName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
