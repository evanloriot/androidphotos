package com.evanloriot.androidphotos18.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.evanloriot.androidphotos18.R;
import com.evanloriot.androidphotos18.activities.AlbumView;
import com.evanloriot.androidphotos18.activities.Home;
import com.evanloriot.androidphotos18.models.Album;
import com.evanloriot.androidphotos18.models.User;

public class Slideshow extends AppCompatActivity {

    private ImageView photo;

    private Button left;
    private Button right;

    User user;
    Album album;

    int curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.albumToolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        user = (User) extras.get("user");
        album = user.getAlbum(((Album) extras.get("album")).name);

        curr = 0;

        photo = (ImageView) findViewById(R.id.slide);
        photo.setImageURI(album.photos.get(curr).getUri());

        left = (Button) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr == 0){
                    curr = album.photos.size() - 1;
                }
                else{
                    curr--;
                }
                photo.setImageURI(album.photos.get(curr).getUri());
            }
        });

        right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr == album.photos.size() - 1){
                    curr = 0;
                }
                else{
                    curr++;
                }
                photo.setImageURI(album.photos.get(curr).getUri());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent(this, AlbumView.class);
                returnIntent.putExtra("user", user);
                returnIntent.putExtra("album", album);
                startActivity(returnIntent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
