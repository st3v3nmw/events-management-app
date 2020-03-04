package com.aspinax.lanaevents;

import com.google.firebase.Timestamp;

public class Person {
    public String fName, lName, email;
    public Integer access;
    public Timestamp created;

    public Person() {}
    public Person(String fName, String lName, String email, Timestamp created, Integer access) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.created = created;
        this.access = access;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }

    public static void updateProfile() {

    }
}
