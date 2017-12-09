package com.evanloriot.androidphotos18.activities;

import android.content.Context;

import com.evanloriot.androidphotos18.models.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SerialUtils {
    public static void writeContextToFile(Context context, User u) throws IOException {
        FileOutputStream fos = context.openFileOutput("user.dat", Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(u);
        oos.close();
    }

    public static User readContextFromFile(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput("user.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        User u = (User)ois.readObject();
        ois.close();
        return u;
    }
}
