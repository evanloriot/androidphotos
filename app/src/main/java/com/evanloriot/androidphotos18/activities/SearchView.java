package com.evanloriot.androidphotos18.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.evanloriot.androidphotos18.R;
import com.evanloriot.androidphotos18.activities.AlbumView;
import com.evanloriot.androidphotos18.activities.Home;
import com.evanloriot.androidphotos18.adapters.HomeGridAdapter;
import com.evanloriot.androidphotos18.adapters.SearchGridAdapter;
import com.evanloriot.androidphotos18.models.Photo;
import com.evanloriot.androidphotos18.models.User;

import java.util.ArrayList;

public class SearchView extends AppCompatActivity {

    private EditText parameters;

    private Button search;

    private GridView searchResults;

    User user;
    String searchParameters;
    SearchGridAdapter adapter;
    ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        user = (User) extras.get("user");

        parameters = (EditText) findViewById(R.id.parameters);

        searchResults = (GridView) findViewById(R.id.searchResults);

        photos = new ArrayList<Photo>();
        adapter = new SearchGridAdapter(this, photos);
        searchResults.setAdapter(adapter);

        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(parameters.getText().toString().isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("No Parameters");
                    alertDialog.setMessage("Please enter a search parameter of the form \"location=tag\" or \"person=tag\".");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    return;
                }
                else if(!areTagsFormatted(parameters.getText().toString())){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("Improper Format");
                    alertDialog.setMessage("Please format search parameter to the form \"location=tag\" or \"person=tag\".");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    return;
                }
                else{
                    searchParameters = parameters.getText().toString();
                    setPhotos(getPhotosFromSearch(searchParameters));
                    adapter.notifyDataSetChanged();
                }
            }
        });

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

    public boolean areTagsFormatted(String tags){
        String[] t = tags.split(",");
        for(int i = 0; i < t.length; i++){
            if(!Photo.isTagFormatted(t[i])){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Photo> getPhotosFromSearch(String parameters){
        ArrayList<Photo> output = new ArrayList<Photo>();
        for(int i = 0; i < user.getAlbums().size(); i++){
            for(int j = 0; j < user.albums.get(i).photos.size(); j++){
                for(int k = 0; k < user.albums.get(i).photos.get(j).tags.size(); k++){
                    String[] t = user.albums.get(i).photos.get(j).tags.get(k).split("=");
                    String[] ps = parameters.split(",");
                    for(int l = 0; l < ps.length; l++){
                        String[] p = ps[l].split("=");
                        if(p[0].equals(t[0]) && t[1].contains(p[1])){
                            output.add(user.albums.get(i).photos.get(j));
                            break;
                        }
                    }
                }
            }
        }
        return output;
    }

    public void setPhotos(ArrayList<Photo> p){
        while(photos.size() > 0){
            photos.remove(0);
        }
        for(int i = 0; i < p.size(); i++){
            photos.add(p.get(i));
        }
    }
}
