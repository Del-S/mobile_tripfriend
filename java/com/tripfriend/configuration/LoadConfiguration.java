package com.tripfriend.configuration;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;

import com.tripfriend.front.Friend;
import com.tripfriend.front.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoadConfiguration {
    private final String config_url = "http://10.0.2.2/tripfriend/tf-api/";
    //private final String config_url = "http://10.0.2.2/weather/";
    private final String friend_url = "http://10.0.2.2/tripfriend/tf-api/friends/";
    private final String friends_available_url = "http://10.0.2.2/tripfriend/tf-api/friends-available/";
    private final String schedule_url = "http://10.0.2.2/tripfriend/tf-api/schedule/";
    private final String schedule_create_url = "http://10.0.2.2/tripfriend/tf-api/schedule/create/";
    private Context context;
    private ApiService apiService;

    private static final LoadConfiguration loadConfiguration = new LoadConfiguration();
    public static LoadConfiguration getInstance(Context context) {
        loadConfiguration.setContext(context);
        loadConfiguration.bindApiService(context);
        return loadConfiguration;
    }

    public void loadConfiguration() throws IOException, JSONException {
        JSONObject jsonObject = apiService.getJsonObject(config_url);

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


        Configuration config = Configuration.getInstance();
        config.setLocations(locations);
        config.setLanguages(languages);
        config.setTime_spans(timespans);
        config.setPreferences(preferences);
        config.setStart_time(start_time);
        config.setEnd_time(end_time);
        config.setFriends(loadFriends());
        config.setIsSet(true);
    }

    private List<Friend> loadFriends() throws IOException, JSONException {
        List<Friend> friends = new ArrayList<>();

        JSONObject jsonObject = apiService.getJsonObject(config_url);

        JSONObject friendsObject = jsonObject.getJSONObject("friends");
        JSONArray names = friendsObject.names();
        for(int i = 0; friendsObject.length() > i; i++ ) {
            String name = (String) names.get(i);
            JSONObject friendObject = friendsObject.getJSONObject(name);
            String thumbnail_url = friendObject.getString("image");
            String thumbnail = apiService.downloadFile(thumbnail_url);

            List<String> languages_url = parseSingleArrayNoID(friendObject.getJSONArray("languages"));
            String[] languages_url_parsed = languages_url.get(0).split(",");
            List<String> languages = apiService.downloadFiles(languages_url_parsed);

            friends.add(new Friend(friendObject.getInt("id"),
                    friendObject.getString("name"),
                    thumbnail,
                    "",
                    languages,
                    new Date()
            ));
        }
        return friends;
    }

    public HashMap<Integer, String> parseSingleArray(JSONObject jsonObject) throws IOException, JSONException {
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

    public List<String> parseSingleArrayNoID(JSONArray jsonArray) throws IOException, JSONException {
        List<String> returnArray = new ArrayList<String>();

        for(int i = 0; jsonArray.length() > i; i++) {
            returnArray.add(jsonArray.getString(i));
        }

        return returnArray;
    }

    public void getAvailableFriends() throws IOException, JSONException {
        Schedule schedule = Schedule.getInstance();

        Calendar c = schedule.getCalendar_start();
        String date = getDateTime(c.getTime());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int time_minutes = (hour*60) + minutes;

        JSONObject jsonSend = new JSONObject();
        jsonSend.put("location_id", schedule.getLocation());
        jsonSend.put("service_id", schedule.getLanguage());
        jsonSend.put("date", date);
        jsonSend.put("time", time_minutes);
        jsonSend.put("timespan", schedule.getTime_span());

        apiService.sendPost(0, friends_available_url, jsonSend);
    }

    public void completeOrder() throws IOException, JSONException {
        Schedule schedule = Schedule.getInstance();

        Calendar c = schedule.getCalendar_start();
        String date = getDateTime(c.getTime());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int time_minutes = (hour*60) + minutes;

        String preferences = "";
        for( String pref : schedule.getPreferences() ) {
            preferences += pref + ",";
        }
        if(!preferences.equals("")) {
            preferences = preferences.substring(0, preferences.length() - 1);
        }

        JSONObject jsonSend = new JSONObject();
        jsonSend.put("location_id", schedule.getLocation());
        jsonSend.put("service_id", schedule.getLanguage());
        jsonSend.put("staff_id", schedule.getId_friend());
        jsonSend.put("date", date);
        jsonSend.put("time", time_minutes);
        jsonSend.put("timespan", schedule.getTime_span());
        jsonSend.put("preferences", preferences);

        jsonSend.put("pickup_location", schedule.getPickup_location());
        jsonSend.put("notes", schedule.getNotes());

        jsonSend.put("name", schedule.getName());
        jsonSend.put("surname", schedule.getSurname());
        jsonSend.put("email", schedule.getEmail());
        jsonSend.put("phone", schedule.getPhone_number());
        jsonSend.put("group", schedule.getGroup());

        apiService.sendPost(1, schedule_create_url, jsonSend);
    }

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date);
    }

    public void getSchedules(String email) throws JSONException, IOException {
        JSONObject jsonSend = new JSONObject();
        jsonSend.put("email", email);

        apiService.sendPost(2, schedule_url, jsonSend);
    }

    /**
     * Load dummy configuration
     */
    /*public static void loadConfigurationDummy() {
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

    /**
     * Load dummy friends
     */
    /*public List<Friend> getFriendsDummy() {
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
    }*/

    public List<Schedule> getSchedulesDummy() {
        List<Schedule> schedules = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        List<String> preferences = new ArrayList<>();
        preferences.add("Neco");
        preferences.add("Pub");
        Schedule s = new Schedule(1, 245, 244, 180, 241, 1, c, "Test", "Testovic", "test@supertest.csz", "123456789", "Here and there", "Notes", preferences, null);
        Schedule s2 = new Schedule(2, 245, 242, 360, 241, 2, c, "Super", "Test", "test@supertest.csz", "123456789", "North Pole", "Notes", preferences, null);
        Schedule s3 = new Schedule(3, 245, 243, 180, 241, 1, c, "Name", "Namovic", "test@supertest.csz", "123456789", "Nowhere", "Notes", preferences, null);

        schedules.add(s);
        schedules.add(s2);
        schedules.add(s3);

        return schedules;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void bindApiService(Context context) { apiService = new ApiService(context); }
}
