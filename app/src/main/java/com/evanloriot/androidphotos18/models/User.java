package com.evanloriot.androidphotos18.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The user class keeps track of the albums associated with this user and
 * does bookkeeping for those albums.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class User implements Serializable{
    /**
     * Need for implementation of Serializable.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The name of the user.
     */
    public String username;
    /**
     * The list of albums associated with the user.
     */
    public ArrayList<Album> albums;

    /**
     * Constructor
     * @param username The name of the user object that will be created.
     */
    public User(String username) {
        this.username = username;
        albums = new ArrayList<Album>();
    }

    /**
     * Just returns the user's name
     * @return String The name of the user.
     */
    public String toString() {
        return username;
    }

    /**
     * Adds an album object to the user's album list.
     * @param album The album to be added.
     */
    public void addAlbum(Album album) {
        albums.add(album);
    }

    /**
     * Changes the name of an album that's already associated with a user.
     * @param name The original name of the album.
     * @param newName The new name of the album.
     */
    public void renameAlbum(String name, String newName) {
        for(int i = 0; i < albums.size(); i++) {
            if(name.equals(albums.get(i).name)) {
                albums.get(i).name = newName;
            }
        }
    }

    /**
     * Removes an album that is associated with the user from the album list.
     * @param name The name of the album to be removed
     */
    public void deleteAlbum(String name) {
        for(int i = 0; i < albums.size(); i++) {
            if(name.equals(albums.get(i).name)) {
                albums.remove(i);
                return;
            }
        }
    }

    /**
     * Gets a specific album associated with the user.
     * @param name The name of the requested album.
     * @return Album -- instance of album with given name
     */
    public Album getAlbum(String name) {
        for(int i = 0; i < albums.size(); i++) {
            if(name.equals(albums.get(i).name)) {
                return albums.get(i);
            }
        }
        return null;
    }

    /**
     * Gets the List of albums associated with the user.
     * @return ArrayList -- The list of albums associated with the user.
     */
    public ArrayList<Album> getAlbums() {
        return this.albums;
    }
}
