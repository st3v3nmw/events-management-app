package com.aspinax.lanaevents;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import static com.aspinax.lanaevents.UserMainActivity.decodeBase64;

public class Event {
    public String addedBy, eventId, location, name, orgId, image;
    public int attendeeCount, checkInCount, hearts, type;
    public Timestamp start, end;
    public boolean posted;
    public Bitmap imageBitmap;

    public Event() {}
    Event(String addedBy, int attendeeCount, int checkInCount, Timestamp end, int hearts, String image, String location, String name, String orgId, boolean posted, Timestamp start, int type) {
        this.addedBy = addedBy;
        this.attendeeCount = attendeeCount;
        this.checkInCount = checkInCount;
        this.end = end;
        this.hearts = hearts;
        this.location = location;
        this.name = name;
        this.orgId = orgId;
        this.posted = posted;
        this.start = start;
        this.type = type;
        this.image = image;
        this.imageBitmap = decodeBase64(image);
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

    public void setHearts(int hearts) {
        this.hearts = hearts;
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

    public void setImage(String image) {
        this.image = image;
        this.imageBitmap = decodeBase64(image);
    }
}