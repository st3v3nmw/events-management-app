package com.aspinax.lanaevents;

import com.google.firebase.Timestamp;

public class Agent extends Person {
    Agent() {}
    Agent(String fName, String lName, String email, Timestamp created, Integer access) {
        super(fName, lName, email, created, access);
    }
}
