package com.tripfriend.configuration;

import java.util.HashMap;
import java.util.List;

public class Configuration {
    private HashMap<Integer, String> locations, languages, time_spans;
    private List preferences;
    private String start_time, end_time, date_format;

    public Configuration() {
    }

    public Configuration(HashMap<Integer, String> locations, HashMap<Integer, String> languages, HashMap<Integer, String> time_spans, List preferences, String start_time, String end_time) {
        this.locations = locations;
        this.languages = languages;
        this.time_spans = time_spans;
        this.preferences = preferences;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public HashMap<Integer, String> getLocations() {
        return locations;
    }

    public void setLocations(HashMap<Integer, String> locations) {
        this.locations = locations;
    }

    public HashMap<Integer, String> getLanguages() {
        return languages;
    }

    public void setLanguages(HashMap<Integer, String> languages) {
        this.languages = languages;
    }

    public HashMap<Integer, String> getTime_spans() {
        return time_spans;
    }

    public void setTime_spans(HashMap<Integer, String> time_spans) {
        this.time_spans = time_spans;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDate_format() {
        return date_format;
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }

    public List getPreferences() {
        return preferences;
    }

    public void setPreferences(List preferences) {
        this.preferences = preferences;
    }
}
