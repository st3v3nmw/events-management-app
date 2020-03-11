package com.aspinax.lanaevents;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

import java.util.Map;

import static com.aspinax.lanaevents.UserMainActivity.decodeBase64;

public class Event implements Comparable<Event> {
    public String addedBy, eventId, location, name, orgId, image;
    public Map<String, Double> coordinates;
    public int attendeeCount, checkInCount, type;
    public Timestamp start, end;
    public boolean posted;
    public Bitmap imageBitmap;

    public Event() {}
    Event(String addedBy, int attendeeCount, int checkInCount, Timestamp end, String image, String location, String name, String orgId, boolean posted, Timestamp start, int type, Map<String, Double> coordinates) {
        this.addedBy = addedBy;
        this.attendeeCount = attendeeCount;
        this.checkInCount = checkInCount;
        this.end = end;
        this.location = location;
        this.name = name;
        this.orgId = orgId;
        this.posted = posted;
        this.start = start;
        this.type = type;
        this.coordinates = coordinates;
        this.image = image;
        this.imageBitmap = Bitmap.createScaledBitmap(decodeBase64(image), 720, 430, true);
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public void setCheckInCount(int checkInCount) {
        this.checkInCount = checkInCount;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public void setCoordinates(Map<String, Double> coordinates) {
        this.coordinates = coordinates;
    }

    public void setImage(String image) {
        this.image = image;
        this.imageBitmap = decodeBase64(image);
    }

    @Override
    public int compareTo(Event o) {
        return Long.compare(this.end.getSeconds(), o.end.getSeconds());
    }
}