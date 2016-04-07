package com.tripfriend.configuration;

import com.tripfriend.front.Friend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoadConfiguration {
    public static Configuration config;
    public static List<Friend> friends;

    public static void loadConfiguration() {
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
    }

    public static Configuration getConfig() {
        return config;
    }
    public static List<Friend> getFriends() {
        List<String> lang = new ArrayList<String>();
        lang.add("Czech");
        lang.add("English");
        lang.add("Polish");
        lang.add("German");

        Calendar schedule = Calendar.getInstance();

        for( int i = 0; i < 5; i++ ) {
            Friend f = new Friend(i,"Name"+i,"Image"+i,"Desc"+i, schedule, lang);
            friends.add(f);
        }
        return friends;
    }
}
