package com.evanloriot.androidphotos18.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.evanloriot.androidphotos18.R;
import com.evanloriot.androidphotos18.activities.AlbumView;
import com.evanloriot.androidphotos18.activities.Home;
import com.evanloriot.androidphotos18.models.Album;
import com.evanloriot.androidphotos18.models.Photo;
import com.evanloriot.androidphotos18.models.User;

import java.io.IOException;
import java.util.ArrayList;

public class PhotoView extends AppCompatActivity {

    private ImageView photo;

    private Button addTag;
    private Button deleteTag;

    private ListView tags;

    User user;
    Photo photoObj;
    ArrayList<String> tagsArray;
    String back;
    String backParameters;

    String selectedTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.albumToolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        user = (User) extras.get("user");
        Photo p = (Photo) extras.get("photo");
        photoObj = (Photo) user.getAlbum(p.album.name).getPhoto(p.location, p.instance);
        back = (String) extras.get("back");
        backParameters = (String) extras.get("backParameters");

        photo = (ImageView) findViewById(R.id.photo);
        photo.setImageURI(photoObj.getUri());

        tags = (ListView) findViewById(R.id.tags);
        tagsArray = photoObj.tags;

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.albums_list_view_item, tagsArray);
        tags.setAdapter(adapter);

        tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTag = (String) adapterView.getItemAtPosition(i);
                view.setSelected(true);
            }
        });

        addTag = (Button) findViewById(R.id.addTag);
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Add Tag");
                builder.setMessage("Enter a tag of format \"person=tag\" or \"location=tag\".");

                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tag = input.getText().toString();
                        if(tag.length() == 0){
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("No tag.");
                            alertDialog.setMessage("Tag cannot be blank.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                        else if(!Photo.isTagFormatted(tag)){
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("Improper Format");
                            alertDialog.setMessage("Tag is improperly formatted. Please format as either \"person=tag\" or \"location=tag\".");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                        else{
                            if(photoObj.doesTagExist(tag)){
                                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                                alertDialog.setTitle("Duplicate Tag");
                                alertDialog.setMessage("Tag exists with that key-value pair. Please enter another tag.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                            else{
                                addTag(tag);
                                adapter.notifyDataSetChanged();
                            }
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
        });

        deleteTag = (Button) findViewById(R.id.deleteTag);
        deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTag == null || selectedTag.isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("No Tag Selected");
                    alertDialog.setMessage("Please select a tag to delete.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    return;
                }
                deleteTag(selectedTag);
                adapter.notifyDataSetChanged();
                selectedTag = null;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(back.equals("search")){
                    Intent returnIntent = new Intent(this, SearchView.class);
                    returnIntent.putExtra("user", user);
                    returnIntent.putExtra("backParameters", backParameters);
                    startActivity(returnIntent);
                    this.finish();
                    return true;
                }
                else if(back.equals("album")) {
                    Intent returnIntent = new Intent(this, AlbumView.class);
                    returnIntent.putExtra("user", user);
                    returnIntent.putExtra("album", user.getAlbum(photoObj.album.name));
                    startActivity(returnIntent);
                    this.finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addTag(String tag){
        try {
            photoObj.addTag(tag);
            SerialUtils.writeContextToFile(this.getBaseContext(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTag(String tag){
        try {
            photoObj.deleteTag(tag);
            SerialUtils.writeContextToFile(this.getBaseContext(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
