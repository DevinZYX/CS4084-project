package com.example.forum;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class connection {
    private static DocumentReference documentReference = FirebaseFirestore.getInstance().document("post/something");

    public static void main(String[] args){
        Map<String,String> map = new HashMap<>();
        map.put("userName","John");
        map.put("title","How are you");
        map.put("content","I hate myself");
        documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("Hey","Good");
                }else{
                    Log.d("Hey","bad");
                }
            }
        });
    }
}
