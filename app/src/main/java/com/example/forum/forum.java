package com.example.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class forum extends AppCompatActivity{
    private ArrayList<String> post;
    private String userName;
    private String title;
    private String content;
    HashMap<String,String> dataToSave;
    private final String TITLE_KEY = "title";
    private final String CONTENT_KEY = "content";
    public boolean isSuccess;
    public String errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);
    }

    public void postBtn(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View puWindow = getLayoutInflater().inflate(R.layout.postpopup,null);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.popupWindow);
        PopupWindow popupWindow = new PopupWindow(puWindow, 1000,700,true);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        Button cancel = puWindow.findViewById(R.id.cancelBtn);
        Button confirm = puWindow.findViewById(R.id.confirmBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleView = (EditText) puWindow.findViewById(R.id.title);
                EditText contentView = (EditText) puWindow.findViewById(R.id.content);
                String title = titleView.getText().toString();
                String content = contentView.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    isSuccess=false;
                    errorMessage="You must enter title and content!";
                    return;
                }
                DocumentReference documentReference = db.document("post/something");
                dataToSave = new HashMap<String,String>();
                dataToSave.put(TITLE_KEY, title);
                dataToSave.put(CONTENT_KEY,content);
                dataToSave.put("userName","John Doe");
                documentReference.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        });
    }
}