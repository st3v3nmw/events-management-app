package com.aspinax.lanaevents;

import com.google.firebase.Timestamp;

public class Ticket implements Comparable<Ticket> {
    public String eventId, ticketId, userId;
    public Timestamp createdAt, end;
    public Ticket() {}
    public Ticket(Timestamp createdAt, String eventId, String userId, Timestamp end) {
        this.createdAt = createdAt;
        this.eventId = eventId;
        this.userId = userId;
        this.end = end;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    @Override
    public int compareTo(Ticket o) {
        return Long.compare(this.end.getSeconds(), o.end.getSeconds());
    }
}