package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.autobots.innopark.data.Callbacks.HashmapCallback;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class DatabaseUtils {

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

//    public static void addData(String col, String doc, Object data){
//         db.collection(col).document(doc)
//                .set(data)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(Tags.SUCCESS.name(), "Successfully added data");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(Tags.FAILURE.name(), "ERROR: Failed to add data with the error - ", e);
//                    }
//                });
//
//    }

//    public static void getData(String collection, String doc, String field)
//    {
//        db.collection(collection).document(doc)
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.passStringResult();
//                        if (document != null && document.exists()) {
//                            Log.d("SUCCESS", document.getString(field.toString())); //Print the name
//                        } else {
//                            Log.d("SUCCESS", "No such document");
//                        }
//                    } else {
//                        Log.d("SUCCESS", "get failed with ", task.getException());
//                    }
//                }
//        });
//
//    }

    public static void getData(String collection, String doc, HashmapCallback callback){
        // Gets all the data in the document

        db.collection(collection).document(doc)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document != null && document.exists()) {
                        callback.passHashmapResult((HashMap<String, Object>) document.getData());
                    } else {
                        callback.passHashmapResult(null);
                        Log.w(Tags.FAILURE.name(), "DOCUMENT NOT FOUND IN DB");
                    }
                } else {
                    Log.w(Tags.FAILURE.name(), "ERROR: COULDN'T FETCH DOCUMENT", task.getException());
                }
            }
        });
    }

    public static void getFieldValue(String collection, String doc, String field, StringCallback callback){
        // Gets the value of a specific field in a document

        db.collection(collection).document(doc)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document != null && document.exists()) {
                        callback.passStringResult(document.getString(field));
                    } else {
                        callback.passStringResult("");
                        Log.w(Tags.FAILURE.name(), "DOCUMENT NOT FOUND IN DB");
                    }
                } else {
                    Log.w(Tags.FAILURE.name(), "ERROR: COULDN'T FETCH DOCUMENT", task.getException());
                }
            }
        });
    }

    public static void addData(String collection, String doc, Object data, StringCallback callback){
         db.collection(collection).document(doc)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w(Tags.SUCCESS.name(), "SUCCESSFULLY ADDED DATA");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tags.FAILURE.name(), "ERROR: FAILED TO ADD DATA - ", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            callback.passStringResult(Tags.SUCCESS.name());
                        }else callback.passStringResult(Tags.FAILURE.name());
                    }
                });
    }

    public static void addData(String collection, Object data, StringCallback callback){
        db.collection(collection)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Tags.SUCCESS.name(), "Successfully added data with generated id");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tags.FAILURE.name(), "ERROR: Failed to add data with generated id - ", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            callback.passStringResult(Tags.SUCCESS.name());
                        }else callback.passStringResult(Tags.FAILURE.name());
                    }
                });
    }

    public static void updateData(String collection, String doc, String field_to_edit,
                                               String new_data){
        db.collection(collection).document(doc)
                .update(field_to_edit.toString(), new_data.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Tags.SUCCESS.name(), "DATA UPDATED!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 Log.w(Tags.FAILURE.name(), "FAILED TO UPDATE DATA", e);
                }
            });
    }

    public static void updateData(String collection, String doc, String field_to_edit, int new_data){
        db.collection(collection).document(doc)
                .update(field_to_edit.toString(), new_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Tags.SUCCESS.name(), "DATA UPDATED!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tags.FAILURE.name(), "FAILED TO UPDATE DATA", e);
                    }
                });
    }

    public static void updateData(String collection, String doc, Map<String, Object> new_data){
        db.collection(collection).document(doc)
                .update(new_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Tags.SUCCESS.name(), "DATA UPDATED!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tags.FAILURE.name(), "FAILED TO UPDATE DATA", e);
                    }
                });
    }

//    public static Query getDocumentQuery(String collection, String field, String value){
//        Query q = db.collection(collection).whereEqualTo(field, value);
//        return q;
//    }

//    public static String GOVERNMENT_DATA = “government-registered-drivers”;
//    public static String USERS = “users”;
}
