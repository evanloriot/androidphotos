package com.evanloriot.androidphotos18.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.evanloriot.androidphotos18.R;
import com.evanloriot.androidphotos18.adapters.HomeGridAdapter;
import com.evanloriot.androidphotos18.models.Album;
import com.evanloriot.androidphotos18.models.Photo;
import com.evanloriot.androidphotos18.models.User;

import java.util.ArrayList;

public class AlbumView extends AppCompatActivity {

    private GridView photosGrid;
    private ArrayList<Photo> photos;
    HomeGridAdapter adapter;

    private Button addPhoto;
    private Button movePhoto;
    private Button deletePhoto;
    private Button slideshow;

    private static int RESULT_LOAD_IMAGE = 1;

    User user;
    Album album;

    Album selectedMoveAlbum;

    ArrayList<Photo> selected = new ArrayList<Photo>();

    private static final int REQUEST_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_RESULT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.albumToolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle variables = getIntent().getExtras();
        user = (User) variables.get("user");
        album = user.getAlbum(((Album)variables.get("album")).name);

        photosGrid = (GridView) findViewById(R.id.photos);
        photos = user.getAlbum(album.name).photos;

        adapter = new HomeGridAdapter(this, photos);
        photosGrid.setAdapter(adapter);

        photosGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent photoIntent = new Intent(view.getContext(), PhotoView.class);
                photoIntent.putExtra("user", user);
                photoIntent.putExtra("photo", (Photo) adapterView.getItemAtPosition(i));
                startActivity(photoIntent);
            }
        });

        photosGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Photo p = (Photo) adapterView.getItemAtPosition(i);
                ImageView iv = (ImageView) view.findViewById(R.id.check);
                if(iv.getVisibility() == View.GONE){
                    iv.setVisibility(View.VISIBLE);
                    selected.add(p);
                }
                else{
                    iv.setVisibility(View.GONE);
                    removeFromSelected(p);
                }
                return true;
            }
        });

        addPhoto = (Button) findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(Intent.createChooser(i, "Choose File"), RESULT_LOAD_IMAGE);
                }
                clearSelected();
            }
        });

        movePhoto = (Button) findViewById(R.id.movePhoto);
        movePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(selected.size() == 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("No Photos Selected");
                    alertDialog.setMessage("Please select photos by long pressing to select.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    return;
                }
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.move_photo_dialog, null);

                final ListView lv = alertLayout.findViewById(R.id.movePhotoToAlbumListView);
                final Button move = alertLayout.findViewById(R.id.movePhotoAlertButton);
                final Button cancel = alertLayout.findViewById(R.id.cancelMovePhotoAlertButton);

                final ArrayAdapter<Album> albumAdapter = new ArrayAdapter<Album>(view.getContext(), R.layout.albums_list_view_item, user.getAlbums());

                lv.setAdapter(albumAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedMoveAlbum = (Album) albumAdapter.getItem(i);
                        view.setSelected(true);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Move Photo");
                builder.setMessage("Please select an album to move photo to.");

                builder.setView(alertLayout);

                final AlertDialog dialog = builder.create();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                move.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selectedMoveAlbum == null){
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("No Album Selected");
                            alertDialog.setMessage("Please select an album from list when moving photos.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            return;
                        }
                        else{
                            for(int i = 0; i < selected.size(); i++){
                                movePhotoToAlbum(selected.get(i), selectedMoveAlbum.name);
                            }
                            adapter.notifyDataSetChanged();
                            selectedMoveAlbum = null;
                            clearSelected();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        deletePhoto = (Button) findViewById(R.id.deletePhoto);
        deletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected.size() == 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("No Photos Selected");
                    alertDialog.setMessage("Please select photos by long pressing to select.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    return;
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Confirm Delete");
                    builder.setMessage("Are you sure you want to delete these photos?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for(int j = 0; j < selected.size(); j++){
                                Photo p = selected.get(j);
                                deletePhoto(p.location, p.instance);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });

        slideshow = (Button) findViewById(R.id.slideshow);
        slideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photos.size() == 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("No Photos");
                    alertDialog.setMessage("Please add photos to album.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    return;
                }
                else {
                    Intent slideshowIntent = new Intent(view.getContext(), Slideshow.class);
                    slideshowIntent.putExtra("user", user);
                    slideshowIntent.putExtra("album", album);
                    startActivity(slideshowIntent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RESULT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent(this, Home.class);
                returnIntent.putExtra("user", user);
                startActivity(returnIntent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            addPhoto(data.getData().toString());
            adapter.notifyDataSetChanged();
        }
    }

    public void addPhoto(String location){
        Photo p = user.getAlbum(album.name).addPhoto(location);
        p.album = album;
    }

    public void movePhotoToAlbum(Photo p, String albumName){
        user.getAlbum(albumName).addPhoto(p.location);
        deletePhoto(p.location, p.instance);
    }

    public void deletePhoto(String location, int instance){
        album.deletePhoto(location, instance);
    }

    public void removeFromSelected(Photo p){
        for(int i = 0; i < selected.size(); i++) {
            if(p.location.equals(selected.get(i).location) && p.instance == selected.get(i).instance) {
                selected.remove(i);
                return;
            }
        }
    }

    public void clearSelected(){
        while(selected.size() > 0){
            selected.remove(0);
        }
        for(int i = 0; i < adapter.getCount(); i++){
            ImageView iv = (ImageView) adapter.getView(i, null, photosGrid).findViewById(R.id.check);
            iv.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
}
