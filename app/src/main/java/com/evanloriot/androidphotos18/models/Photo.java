package com.evanloriot.androidphotos18.models;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
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
    public String location;
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
    public Photo(String location, int instance) {
        this.location = location;
        tags = new ArrayList<String>();
        this.instance = instance;
    }

    public Uri getUri(){
        Uri image = Uri.parse(location);
        return image;
    }

    /**
     * Adds a tag to the photo object.
     * @param tag A tag to be associated with the photo.
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    public static boolean isTagFormatted(String tag){
        String[] text = tag.split("=");
        if(text.length != 2){
            return false;
        }
        if(text[0].equals("location") || text[0].equals("person")){
            return true;
        }
        return false;
    }

    public boolean doesTagExist(String s){
        for(int i = 0; i < tags.size(); i++){
            if(tags.get(i).equals(s)){
                return true;
            }
        }
        return false;
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
