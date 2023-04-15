package com.example.myeventapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Event {
    private String eventName;
    private String eventDate;
    private String eventTime;
    

    public Event() {
    }

    public Event(String eventName, String eventDate, String eventTime) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    @Exclude
    public Map<String, Object> eventMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("eventName", eventName);
        result.put("eventDate", eventDate);
        result.put("eventTime", eventTime);

        return result;
    }
}
