package com.codepath.network_example.Tasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JaneChung on 7/19/16.
 */
public class JsonParsingTask extends AsyncTask<String, String, JSONObject> {

    private JsonParsingListener listener;

    public interface JsonParsingListener {
        void onPreExecute();

        void onException();

        void onFinally(JSONObject firstObject);
    }

    public JsonParsingTask(JsonParsingListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null)
            listener.onPreExecute();
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
            if (listener != null)
                listener.onException();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return jsonDict;
    }
    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            // Getting first article

            JSONObject response = json.getJSONObject("response");
            JSONArray docs = response.getJSONArray("docs");
            JSONObject first = docs.getJSONObject(0);

            //call listener with the json object
            if (listener != null)
                listener.onFinally(first);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
