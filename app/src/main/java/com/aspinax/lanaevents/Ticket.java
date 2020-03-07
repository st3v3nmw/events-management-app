package com.aspinax.lanaevents;

import com.google.firebase.Timestamp;

public class Ticket {
    public String eventId, ticketId, userId;
    public Timestamp createdAt;
    public Ticket() {}
    public Ticket(Timestamp createdAt, String eventId, String userId) {
        this.createdAt = createdAt;
        this.eventId = eventId;
        this.userId = userId;
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
}