package com.aspinax.lanaevents;

import com.google.firebase.Timestamp;

public class Agent extends Person {
    public Agent() {}
    public Agent(String fName, String lName, String email, Timestamp created, Integer access, String organization, String phoneNumber) {
        super(fName, lName, email, created, access, phoneNumber, organization);
    }
}