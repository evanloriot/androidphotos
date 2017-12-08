package com.evanloriot.androidphotos18.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * The album class keeps track of the photos that are added to it,
 * the album thumbnail, and the range of the dates that the album covers
 * @author Evan Loriot
 * @author Joseph Klaszky
 */

public class Album implements Serializable{
    /**
     * Need for implementation of Serializable.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Name of the album.
     */
    public String name;
    /**
     * Number of photos that the album contains.
     */
    public int numPhotos;
    /**
     * List of all the photo objects that the album contains.
     */
    public ArrayList<Photo> photos;

    /**
     * Constructor
     * @param name Name of the album
     */
    public Album(String name) {
        this.name = name;
        numPhotos = 0;
        photos = new ArrayList<Photo>();
    }

    /**
     * Iterates over the album and finds the photo that has the
     * oldest date.
     * @return String -- A string that represents date of the oldest photo.
     */
    public String earliestDate() {
        if(numPhotos == 0) {
            return "";
        }
        Date earliest = new Date();
        for(int i = 0; i < photos.size(); i++) {
            if(photos.get(i).captureDate.before(earliest)) {
                earliest = photos.get(i).captureDate;
            }
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
        return ft.format(earliest);
    }

    /**
     * Iterates over the album and finds the most recent photo.
     * @return String -- A string that represents the date of the most recent photo.
     */
    public String latestDate() {
        if(numPhotos == 0) {
            return "";
        }
        Date latest = new Date(Long.MIN_VALUE);
        for(int i = 0; i < photos.size(); i++) {
            if(photos.get(i).captureDate.after(latest)) {
                latest = photos.get(i).captureDate;
            }
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
        return ft.format(latest);
    }

    /**
     * @return String -- Returns a string of the form: "Album name: (number of photos) earliest date - latest date.
     */
    public String toString() {
        return name + ": (" + numPhotos + ")\n" + earliestDate() + "-" + latestDate();
    }

    /**
     * Gets the first image of an album to use as a thumbnail.
     * @return Image -- The first photo present in the album.
     */
    public Bitmap getThumb() {
        if(numPhotos == 0) {
            return BitmapFactory.decodeFile("/resources/albumthumb.png");
        }
        return BitmapFactory.decodeFile(photos.get(0).location);
    }

    /**
     * Adds a new photo to the album and increments the number of photos.
     * @param location The location of the photo being added to the album.
     * @return Photo -- instance of photo object added to album
     */
    public Photo addPhoto(String location) {
        int instance = 1;
        for(int i = 0; i < photos.size(); i++) {
            if(photos.get(i).location.equals(location)) {
                instance++;
            }
        }
        Photo output = new Photo(location, instance);
        photos.add(output);
        numPhotos++;
        return output;
    }

    /**
     * Removes a photo from the album and decrements the number of photos.
     * @param location The location of the photo being removed from the album.
     * @param instance the instance number of the photo in album if duplicate.
     */
    public void deletePhoto(String location, int instance) {
        for(int i = 0; i < photos.size(); i++) {
            if(location.equals(photos.get(i).location) && instance == photos.get(i).instance) {
                photos.remove(i);
                numPhotos--;
                return;
            }
        }
    }

    /**
     * Iterates over the album to find a particular photo.
     * @param location The location of the photo the method is looking for.
     * @param instance the instance number of a photo in album (if duplicate != 1 else 1)
     * @return Photo -- If found returns the photo object from the album.
     */
    public Photo getPhoto(String location, int instance) {
        for(int i = 0; i < photos.size(); i++) {
            if(location.equals(photos.get(i).location) && photos.get(i).instance == instance) {
                return photos.get(i);
            }
        }
        return null;
    }
}
