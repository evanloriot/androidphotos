package com.evanloriot.androidphotos18.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.evanloriot.androidphotos18.R;
import com.evanloriot.androidphotos18.adapters.GridAdapter;
import com.evanloriot.androidphotos18.models.Album;
import com.evanloriot.androidphotos18.models.Photo;
import com.evanloriot.androidphotos18.models.User;

import java.util.ArrayList;

public class AlbumView extends AppCompatActivity {

    private GridView photosGrid;
    private ArrayList<Photo> photos;
    GridAdapter adapter;

    private Button addPhoto;
    private Button movePhoto;
    private Button deletePhoto;

    private static int RESULT_LOAD_IMAGE = 1;

    User user;
    Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);

        Bundle variables = getIntent().getExtras();
        user = (User) variables.get("user");
        album = (Album) variables.get("album");

        photosGrid = (GridView) findViewById(R.id.photos);
        photos = user.getAlbum(album.name).photos;

        adapter = new GridAdapter(this, photos);
        photosGrid.setAdapter(adapter);

        addPhoto = (Button) findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            addPhoto(data.getData());
            adapter.notifyDataSetChanged();
        }
    }

    public void addPhoto(Uri location){
        Photo p = user.getAlbum(album.name).addPhoto(location);
        p.album = album;
    }
}
