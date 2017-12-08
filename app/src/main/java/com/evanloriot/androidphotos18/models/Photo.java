package com.evanloriot.androidphotos18.models;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * The photo class keeps track of the caption that is associated with each photo,
 * the date of the photo, the tags associated with each photo, the location of the photo,
 * and the album it is associated with.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class Photo implements Serializable{

    /**
     * Need for implementation of Serializable.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The list of tags associated with the photo.
     */
    public ArrayList<String> tags;
    /**
     * The location of the photo.
     */
    public Uri location;
    /**
     * The album that is associated with the photo.
     */
    public Album album;

    public int instance;

    /**
     * Constructor
     * @param location The location of the photo.
     * @param instance number of photo in album if duplicate
     */
    public Photo(Uri location, int instance) {
        this.location = location;
        tags = new ArrayList<String>();
        this.instance = instance;
    }

    /**
     * Adds a tag to the photo object.
     * @param tag A tag to be associated with the photo.
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * Removes a tag from the photo object.
     * @param tag Removes a tag associated with the photo.
     */
    public void deleteTag(String tag) {
        for(int i = 0; i < tags.size(); i++) {
            if(tag.equals(tags.get(i))) {
                tags.remove(i);
                return;
            }
        }
    }
}
