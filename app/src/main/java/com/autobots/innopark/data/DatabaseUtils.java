package com.autobots.innopark.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
//                        DocumentSnapshot document = task.getResult();
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

    public static void addData(String collection, String doc, Object data, Listeners.DbListenerCallback callback){
         db.collection(collection).document(doc)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Tags.SUCCESS.name(), "Successfully added data");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tags.FAILURE.name(), "ERROR: Failed to add data with the error - ", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            callback.getResult(Tags.SUCCESS.name());
                        }else callback.getResult(Tags.FAILURE.name());
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
