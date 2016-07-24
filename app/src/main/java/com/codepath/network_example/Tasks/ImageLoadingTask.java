package com.codepath.network_example.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JaneChung on 7/18/16.
 */
public class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {

    private ImageLoadingListener listener;

    public interface ImageLoadingListener {
        void onPreExecute();

        void onException();

        void onFinally(Bitmap result);
    }

    public ImageLoadingTask(ImageLoadingListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null)
            listener.onPreExecute();
    }

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
            if (listener != null)
                listener.onException();
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
        // post to the listener the bitmap result

        if (listener != null)
            listener.onFinally(result);
    }

}
