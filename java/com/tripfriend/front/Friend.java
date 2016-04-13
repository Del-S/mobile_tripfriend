package com.tripfriend.front;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Friend {
    int id;
    String name, image, description;
    Calendar schedule;
    List<String> languages;
    private Date update_time;
    private Boolean up_to_date;

    public Friend() {

    }

    public Friend(int id, String name, String image, String description, Calendar schedule, List<String> languages, Date update_time) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.schedule = schedule;
        this.languages = languages;
        this.update_time = update_time;
        checkIs_up_to_date();
    }

    private void checkIs_up_to_date() {
        Date today = new Date();
        // Check if update_date is close to today..
        /*if(today - this.update_time <= 3 ) {
            setUp_to_date(true);
        } else {
            setUp_to_date(false);
        }*/
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

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Boolean isUp_to_date() {
        return up_to_date;
    }

    public void setUp_to_date(Boolean up_to_date) {
        this.up_to_date = up_to_date;
    }
}
