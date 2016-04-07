package com.tripfriend.front;

import java.util.Calendar;
import java.util.List;

public class Friend {
    int id;
    String name, image, description;
    Calendar schedule;
    List<String> languages;

    public Friend() {

    }

    public Friend(int id, String name, String image, String description, Calendar schedule, List<String> languages) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.schedule = schedule;
        this.languages = languages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getSchedule() {
        return schedule;
    }

    public void setSchedules_available(Calendar schedule) {
        this.schedule = schedule;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
