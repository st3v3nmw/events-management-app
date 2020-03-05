package com.aspinax.lanaevents;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Database {
    public static FirebaseFirestore db;
    private AsyncResponse response;

    public Database(AsyncResponse obj) {
        db = FirebaseFirestore.getInstance();
        this.response = obj;
    }

    // CREATE
    public void insert(String collectionName, String docId, Map data) {
        db.collection(collectionName).document(docId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {}
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.resultHandler(e.toString(), 0);
                    }
                });
    }

    // READ
    public void readCollection(String collectionName, final Class<?> className, final int resultCode) {
        final Map<String, Object> result = new HashMap<>();
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                result.put(document.getId(), document.toObject(className));
                            }
                            response.resultHandler(result, resultCode);
                        } else {
                                response.resultHandler(task.getException().toString(), 0);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.resultHandler(e.toString(), 0);
                    }
                });
    }

    // READ SINGLE DOCUMENT
    public void read(String collectionName, String docId, final Class<?> className, final int resultCode) {
        final Map<String, Object> result = new HashMap<>();
        db.collection(collectionName).document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                result.put(document.getId(), document.toObject(className));
                            }
                            if (response != null) {
                                response.resultHandler(result, resultCode);
                            }
                        } else {
                            response.resultHandler(task.getException().toString(), 0);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.resultHandler(e.toString(), 0);
                    }
                });
    }

    // UPDATE
    public void update(String collectionName, String docId, Map<String, Object> data) {
        db.collection(collectionName).document(docId)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.resultHandler(e.toString(), 0);
                    }
                });
    }

    // INCREMENT, DECREMENT
    public void increment(String collectionName, String docId, String fieldName, Integer val) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(fieldName, FieldValue.increment(val));
        this.update(collectionName, docId, updates);
    }

    // DELETE
    public void delete(String collectionName, String docId) {
        db.collection(collectionName).document(docId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.resultHandler(e.toString(), 0);
                    }
                });
    }

    // DELETE FIELDS
    public void deleteFields(String collectionName, String docId, String[] fields) {
        Map<String, Object> updates = new HashMap<>();
        for (String field: fields) {
            updates.put(field, FieldValue.delete());
        }
        this.update(collectionName, docId, updates);
    }
}