package com.carson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class XKCD {

    public XKCD() {}

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String jsonText = rd.lines().collect(Collectors.joining());

            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String getJson(String url, String get) {
        try {
            return readJsonFromUrl(url).get(get).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }





    //static classes
    public static String getAlt(String url) {
        return getJson(url, "alt");
    }

    public static String getAlt(int no) {
        return getJson("https://www.xkcd.com/" + no + "/info.0.json", "alt");
    }



}
