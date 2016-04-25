package com.tripfriend.configuration;

import com.tripfriend.front.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadConfiguration {
    private Configuration config;
    private List<Friend> friends;

    private final String config_url = "http://10.0.2.2/tripfriend/tf-api/";
    //private final String config_url = "http://10.0.2.2/weather/";
    private final String friend_url = "http://10.0.2.2/tripfriend/tf-api/friends/";
    private final String friends_available_url = "http://10.0.2.2/tripfriend/tf-api/friends-available/";
    private final String schedule = "http://10.0.2.2/tripfriend/tf-api/schedule/";
    private final String schedule_create = "http://10.0.2.2/tripfriend/tf-api/schedule/create/";

    private static final LoadConfiguration loadConfiguration = new LoadConfiguration();
    public static LoadConfiguration getInstance() { return loadConfiguration; }

    public void loadConfiguration() throws IOException, JSONException {
        JSONObject jsonObject = getJsonObject(config_url);

        JSONObject configObject = jsonObject.getJSONObject("config");

        // Sub main json object
        JSONObject locationsArray = configObject.getJSONObject("locations");
        JSONObject languagesArray = configObject.getJSONObject("languages");
        JSONObject timespansArray = configObject.getJSONObject("time_spans");
        JSONArray preferencesArray = configObject.getJSONArray("preferences");

        // Parse from object to array
        HashMap<Integer, String> locations = parseSingleArray(locationsArray);
        HashMap<Integer, String> languages = parseSingleArray(languagesArray);
        HashMap<Integer, String> timespans = parseSingleArray(timespansArray);
        List<String> preferences = parseSingleArrayNoID(preferencesArray);

        String start_time = configObject.getString("start_time");
        String end_time = configObject.getString("end_time");

        config = new Configuration(locations, languages, timespans, preferences, start_time, end_time);
        //loadFriends();
    }

    private HashMap<Integer, String> parseSingleArray(JSONObject jsonObject) throws IOException, JSONException {
        HashMap<Integer, String> returnArray = new HashMap<>();
        JSONArray names = jsonObject.names();

        // Change lists to Map<Integer, String>
        for(int i = 0; jsonObject.length() > i; i++) {
            String name = (String) names.get(i);
            Integer id = Integer.valueOf(name);
            returnArray.put(id, jsonObject.getString(name));
        }

        return returnArray;
    }

    private List<String> parseSingleArrayNoID(JSONArray jsonArray) throws IOException, JSONException {
        List<String> returnArray = new ArrayList<String>();

        for(int i = 0; jsonArray.length() > i; i++) {
            returnArray.add(jsonArray.getString(i));
        }

        return returnArray;
    }

    private void loadFriends() throws IOException, JSONException {
        JSONArray friendsArray = getJsonObject(config_url).getJSONArray("friends");
        for(int i = 0; friendsArray.length() >= i; i++ ) {
            JSONObject friendObject = friendsArray.getJSONObject(i);
            String thumbnail_url = friendObject.getString("image");
            String thumbnail = downloadFile(thumbnail_url);

            //List<String> languages = parseSingleArray( friendObject.getJSONArray("languages") );

            /*friends.add(new Friend(friendObject.getInt("ID"),
                    friendObject.getString("name"),
                    thumbnail,
                    "",
                    languages,
                    new Date()
            ));*/
        }
    }

    private String downloadFile(String downloadUrl) {
        return "";
    }

    /*public static void loadConfiguration() {
        List<String> locations = new ArrayList<>();
        List<String> languages = new ArrayList<>();
        List<String> timespans = new ArrayList<>();
        List<String> preferences = new ArrayList<>();
        String start_time = "7:00";
        String end_time = "23:00";

        locations.add("Prague");
        languages.add("Czech");
        languages.add("English");
        languages.add("German");
        timespans.add("3");
        timespans.add("6");
        timespans.add("10");
        preferences.add("Pub");
        preferences.add("Strip bar");
        preferences.add("Museums");

        config = new Configuration(locations, languages, timespans, preferences, start_time, end_time);
    }*/

    public Configuration getConfig() throws IOException, JSONException {
        if(config == null) {
            loadConfiguration();
        }
        return config;
    }

    public List<Friend> getFriends() {
        List<String> lang = new ArrayList<String>();
        lang.add("Czech");
        lang.add("English");
        lang.add("Polish");
        lang.add("German");

        friends = new ArrayList<Friend>();

        for( int i = 0; i < 5; i++ ) {
            Friend f = new Friend(i,"Name"+i,"Image"+i,"Desc"+i, lang, new Date());
            friends.add(f);
        }
        return friends;
    }

    public JSONObject getJsonObject(String urlString) throws IOException, JSONException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(connection.getInputStream());

        StringBuilder jsonResult = new StringBuilder();

        int read;
        char[] buff = new char[1024];

        while((read = in.read(buff)) != -1) {
            jsonResult.append(buff, 0, read);
        }

        return new JSONObject(jsonResult.toString());
    }
}
