package com.tripfriend.front;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Schedule {

    // Location, Language, Timespan
    // Date and StartTime
    // Friend
    // Pickup location and Notes
    // User info (name, surname, mail, group, phone number)

    private int id, location, language, time_span, id_friend, group;
    private Calendar calendar_start;
    private String name, surname, email, phone_number, pickup_location, notes;
    private List<String> preferences, availableFriends;

    private static final Schedule schedule = new Schedule();
    public static Schedule getInstance() { return schedule; }

    public Schedule(int id, int location, int language, int time_span, int id_friend, int group, Calendar calendar_start, String name, String surname, String email, String phone_number, String pickup_location, String notes, List<String> preferences, List<String> availableFriends) {
        this.id = id;
        this.location = location;
        this.language = language;
        this.time_span = time_span;
        this.id_friend = id_friend;
        this.group = group;
        this.calendar_start = calendar_start;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone_number = phone_number;
        this.preferences = preferences;
        this.availableFriends = availableFriends;
        this.pickup_location = pickup_location;
        this.notes = notes;
    }

    public Schedule(){
        this.preferences = new ArrayList<String>();
        this.location = -1;
        this.language = -1;
        this.time_span = -1;
    }

    public void resetSchedule() {
        this.id = -1;
        this.location = -1;
        this.language = -1;
        this.time_span = -1;
        this.id_friend = -1;
        this.calendar_start = null;
        this.preferences = new ArrayList<String>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getTime_span() {
        return time_span;
    }

    public void setTime_span(int time_span) {
        this.time_span = time_span;
    }

    public int getId_friend() {
        return id_friend;
    }

    public void setId_friend(int id_friend) {
        this.id_friend = id_friend;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public Calendar getCalendar_start() {
        return calendar_start;
    }

    public void setCalendar_start(Calendar calendar_start) {
        this.calendar_start = calendar_start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public List<String> getAvailableFriends() {
        return availableFriends;
    }

    public void setAvailableFriends(List<String> availableFriends) {
        this.availableFriends = availableFriends;
    }
}
