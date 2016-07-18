package com.codepath.network_example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String urlString = "http://cdn.skim.gs/image/upload/v1456344012/msi/Puppy_2_kbhb4a.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grabImageFromOnline();
    }



    private void grabImageFromOnline() {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            //Create new url
            url = new URL(urlString);

            //Open a connection
            urlConnection = (HttpURLConnection) url
                    .openConnection();

            //Grab the input stream
            InputStream in = urlConnection.getInputStream();

            //Convert stream to Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            in.close();

            // Load Bitmap into ImageView
            ImageView img = (ImageView) findViewById(R.id.ivNetworkImage);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


}


