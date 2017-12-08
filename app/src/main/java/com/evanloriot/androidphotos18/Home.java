package com.evanloriot.androidphotos18;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import android.view.View.OnClickListener;
import android.view.View;

import com.evanloriot.androidphotos18.models.Album;
import com.evanloriot.androidphotos18.models.User;

public class Home extends AppCompatActivity {

    private ListView albumsListView;
    private ArrayList<Album> albums;

    private Button createAlbum;
    private Button renameAlbum;
    private Button deleteAlbum;

    Album selected;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = getUser();

        //Begin
        albumsListView = (ListView) findViewById(R.id.albums);
        albums = getAlbums();

        final ArrayAdapter<Album> adapter = new ArrayAdapter<Album>(this, R.layout.albums_list_view_item, albums);
        albumsListView.setAdapter(adapter);

        albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO open album
            }
        });

        createAlbum = (Button) findViewById(R.id.createAlbum);
        createAlbum.setOnClickListener(new OnClickListener(){
            String albumName;

            //final? TODO
            public void onClick(final View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Create Album");

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        albumName = input.getText().toString();
                        if(albumName.length() == 0){
                            //error TODO
                        }
                        else{
                            if(doesAlbumExist(albumName)){
                                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                                alertDialog.setTitle("Duplicate Album");
                                alertDialog.setMessage("Album exists with name" + input.getText().toString() + ", please try a different name.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                            else{
                                addAlbum(albumName);
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

        renameAlbum = (Button) findViewById(R.id.renameAlbum);
        renameAlbum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.rename_album_dialog, null);

                final ListView lv = alertLayout.findViewById(R.id.renameAlbumListView);
                final Button rename = alertLayout.findViewById(R.id.renameAlbumAlertButton);
                final Button cancel = alertLayout.findViewById(R.id.cancelAlbumRenameAlertButton);
                final EditText albumName = alertLayout.findViewById(R.id.renameAlbumField);

                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selected = (Album) adapterView.getItemAtPosition(i);
                        view.setSelected(true);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Rename Album");
                builder.setMessage("Please select an album to rename.");

                builder.setView(alertLayout);

                final AlertDialog dialog = builder.create();

                cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                rename.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selected == null){
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("No Album Selected");
                            alertDialog.setMessage("Please select an album from list when renaming.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            return;
                        }
                        if(albumName.getText().toString().length() == 0){
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("Invalid Name");
                            alertDialog.setMessage("Please enter a name for album when renaming.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            return;
                        }
                        if(doesAlbumExist(albumName.getText().toString())){
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("Duplicate Album");
                            alertDialog.setMessage("Album exists with name" + albumName.getText().toString() + ", please try a different name.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            return;
                        }
                        renameAlbum(selected.name, albumName.getText().toString());
                        adapter.notifyDataSetChanged();
                        selected = null;
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        deleteAlbum = (Button) findViewById(R.id.deleteAlbum);
        deleteAlbum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.delete_album_dialog, null);

                final ListView lv = alertLayout.findViewById(R.id.deleteAlbumListView);
                final Button delete = alertLayout.findViewById(R.id.deleteAlbumAlertButton);
                final Button cancel = alertLayout.findViewById(R.id.cancelAlbumDeleteAlertButton);

                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selected = (Album) adapter.getItem(i);
                        view.setSelected(true);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Album");
                builder.setMessage("Please select an album to delete.");

                builder.setView(alertLayout);

                final AlertDialog dialog = builder.create();

                cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selected == null){
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("No Album Selected");
                            alertDialog.setMessage("Please select an album from list when deleting.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            return;
                        }
                        else{
                            deleteAlbum(selected.name);
                            adapter.notifyDataSetChanged();
                            selected = null;
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

    }

    private User getUser(){
        //serial TODO
        return new User("me");
    }

    private boolean doesAlbumExist(String name) {
        for(int i = 0; i < user.getAlbums().size(); i++) {
            if(name.equals(user.getAlbums().get(i).name)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Album> getAlbums(){
        //TODO remove this
        ArrayList<Album> output = new ArrayList<Album>();
        output.add(new Album("Album 1"));
        output.add(new Album("Album 2"));
        output.add(new Album("Album 3"));
        output.add(new Album("Album 4"));
        output.add(new Album("Album 5"));
        output.add(new Album("Album 6"));
        output.add(new Album("Album 7"));
        output.add(new Album("Album 8"));
        output.add(new Album("Album 9"));
        output.add(new Album("Album 10"));
        output.add(new Album("Album 11"));
        output.add(new Album("Album 12"));
        user.albums = output;
        return user.getAlbums();
    }

    private void addAlbum(String name){
        Album newAlbum = new Album(name);
        user.addAlbum(newAlbum);
        //NOTE: Albums points to same object as user.albums
        //serialize TODO
    }

    private void renameAlbum(String name, String newName){
        user.renameAlbum(name, newName);
        //serialize TODO
    }

    private void deleteAlbum(String album){
        user.deleteAlbum(album);
        //serialize TODO
    }
}
