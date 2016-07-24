package com.codepath.network_example;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JaneChung on 7/19/16.
 */
public class Utils {

    static public String LAST_USED_IMAGE_URL = "last_used_image_url";

    static public String getTitleFromArticle(JSONObject first) {
        String title = null;
        try {
            JSONObject headline = first.getJSONObject("headline");
            title = headline.getString("main");
        } catch (JSONException e) {

        }

        return title;
    }

    static public String getImageURLfromArticle(JSONObject first) {
        String nytimes = "http://www.nytimes.com/";
        String url = null;
        try {
            JSONArray multimedia = first.getJSONArray("multimedia");
            JSONObject firstImage = multimedia.getJSONObject(0);
            url = firstImage.getString("url");
        } catch (JSONException e) {

        }

        return url == null ? url : nytimes + url;
    }

    static public void saveImageURLToPrefs(Context context, String url) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = prefs.edit();

        e.putString(Utils.LAST_USED_IMAGE_URL, url);
        e.apply();
    }

    static public String getSavedImageURL(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Utils.LAST_USED_IMAGE_URL, null);
    }
}
