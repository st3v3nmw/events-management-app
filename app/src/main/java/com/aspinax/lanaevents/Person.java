package com.aspinax.lanaevents;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class Person {
    public String fName, lName, email;
    public Integer access;
    public Timestamp created;
    private FirebaseAuth auth;
    private Database db = new Database(new AsyncResponse() {
        @Override
        public void resultHandler(Map<String, Object> result, int resultCode) { }

        @Override
        public void resultHandler(String msg, int resultCode) {

        }
    });

    public Person() {}
    public Person(String fName, String lName, String email, Timestamp created, Integer access) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.created = created;
        this.access = access;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
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

    public void updateProfile(Map<String, Object> data) {
        this.db.update("users", this.auth.getUid(), data, 0);
    }

    public static void saveUser(FirebaseAuth auth, String fullname, String email, FieldValue created) {
        FirebaseUser user = auth.getCurrentUser();
        String[] names = fullname.split(" ");
        Map<String, Object> data = new HashMap<>();
        data.put("fName", names[0]);
        data.put("lName", names[1]);
        data.put("email", email);
        data.put("created", created);
        data.put("access", 0);

        Database db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) { }

            @Override
            public void resultHandler(String msg, int resultCode) {

            }
        });
        db.set("users", user.getUid(), data, 0);
    }
}
