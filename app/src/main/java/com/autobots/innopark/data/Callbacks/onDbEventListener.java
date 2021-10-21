package com.autobots.innopark.data.Callbacks;

import java.util.HashMap;

public interface onDbEventListener {
    void onEvent(String collection, String doc, Object data);
}

//  public static ArrayList<String> getDriversListDOB1(String license_plate_number) {

//        DbEvent e = new DbEvent();
//        e.setOnDbEvent(new onDbEventListener() {
//            @Override
//            public void onEvent(String collection, String doc, Object data) {
//                DatabaseUtils.db.collection(collection).document(doc)
//                        .set(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(Tags.SUCCESS.name(), "Successfully added data");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(Tags.FAILURE.name(), "ERROR: Failed to add data with the error - ", e);
//                            }
//                        })
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    callback.passStringResult(Tags.SUCCESS.name());
//                                }else callback.passStringResult(Tags.FAILURE.name());
//                            }
//                        });
//            }
//        });
//        e.doEvent();
//}