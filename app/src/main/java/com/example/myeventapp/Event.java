package com.example.myeventapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.time.LocalDate;
import java.time.LocalTime;


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
}
