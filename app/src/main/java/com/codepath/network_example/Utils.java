package com.codepath.network_example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JaneChung on 7/19/16.
 */
public class Utils {

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
}
