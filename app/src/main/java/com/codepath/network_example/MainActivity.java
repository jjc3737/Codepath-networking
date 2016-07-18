package com.codepath.network_example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
        //grabImageFromOnline();
        new MyAsyncTask().execute(urlString);
    }

    private void grabImageFromOnline() {

        //This doesn't work because network calls cannot be
        // made on UI thread
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

    //Getting Image from online with AsyncTask
    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        public Bitmap doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            Bitmap bitmap = null;
            try {
                //Create new url
                url = new URL(strings[0]);

                //Open a connection
                urlConnection = (HttpURLConnection) url
                        .openConnection();

                //Grab the input stream
                InputStream in = urlConnection.getInputStream();

                //Convert stream to Bitmap
                bitmap = BitmapFactory.decodeStream(in);
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            // This method is executed in the UIThread
            // with access to the result of the task
            // Load Bitmap into ImageView
            ImageView img = (ImageView) findViewById(R.id.ivNetworkImage);
            img.setImageBitmap(result);
        }
    }

}


