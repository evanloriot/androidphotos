package com.evanloriot.androidphotos18.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.evanloriot.androidphotos18.R;
import com.evanloriot.androidphotos18.models.Photo;

import java.util.ArrayList;

/**
 * Created by Evan on 12/8/2017.
 */

public class GridAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Photo> photos;

    private LayoutInflater inflater;

    public GridAdapter(Context context, ArrayList<Photo> photos){
        this.context = context;
        this.photos = photos;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return photos.size();
    }

    public Photo getItem(int position){
        return photos.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(R.layout.album_grid_item, null);
        }
        ImageView thumb = (ImageView) convertView.findViewById(R.id.photo_thumb);
        thumb.setImageURI(photos.get(position).location);
        return convertView;
    }
}
