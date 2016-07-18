package com.codepath.network_example;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String urlString = "http://cdn.skim.gs/image/upload/v1456344012/msi/Puppy_2_kbhb4a.jpg";
    String nyUrlString = "http://api.nytimes.com/svc/search/v2/articlesearch.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //grabImageFromOnline();
        new MyAsyncTask().execute(urlString);


        EditText et = (EditText) findViewById(R.id.etSearch);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new JSONAsyncTask().execute(nyUrlString, v.getText().toString());
                }
                return false;
            }
        });
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


    //Getting JSOn from online with AsyncTask
    private class JSONAsyncTask extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Getting Data ...");
            dialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            URL url;
            HttpURLConnection urlConnection = null;
            JSONObject jsonDict = null;
            try {
                //Create new url
                url = new URL(args[0] + "?api-key=7244297877ff7ee03e6286c9436d7d58:19:74355132&page=0&q=" + args[1]);

                //Open a connection
                urlConnection = (HttpURLConnection) url
                        .openConnection();

                //Grab the input stream
                InputStream in = urlConnection.getInputStream();

                //Convert stream to json
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                in.close();

                jsonDict = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return jsonDict;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            dialog.dismiss();
            String nytimes = "http://www.nytimes.com/";
            try {
                // Getting first article

                JSONObject response = json.getJSONObject("response");
                JSONArray docs = response.getJSONArray("docs");
                JSONObject first = docs.getJSONObject(0);

                //Get title
                JSONObject headline = first.getJSONObject("headline");
                String main = headline.getString("main");
                TextView title = (TextView) findViewById(R.id.tvTitle);
                title.setText(main);

                //Get image url
                JSONArray multimedia = first.getJSONArray("multimedia");
                JSONObject firstImage = multimedia.getJSONObject(0);
                String imageUrl = firstImage.getString("url");


                if (imageUrl != null) {
                    nytimes += imageUrl;

                    //Set it to image view
                    new MyAsyncTask().execute(nytimes);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}


