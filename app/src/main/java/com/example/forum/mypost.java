package com.example.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mypost extends AppCompatActivity {

    private String userName = "john doe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("My post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.mypost);
        createMyPost();
    }

    public void createMyPost(){
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("user/"+userName+"/post");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = document.getData();
                    String title = map.get("title").toString();
                    String content = map.get("content").toString();
                    String id = document.getId();
                    createCard(title, content, id);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    public void createCard(String title, String content, String id){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLayout =  findViewById(R.id.mypost_linear);
        CardView cardView = (CardView) inflater.inflate(R.layout.mypostcard, linearLayout, false);
        TextView userView = cardView.findViewById(R.id.Forum_UserName);
        TextView titleView = cardView.findViewById(R.id.Forum_Title);
        TextView contentView = cardView.findViewById(R.id.Forum_Content);
        userView.setText(userName);
        titleView.setText(title);
        contentView.setText(content);
        linearLayout.addView(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mypost.this, postpage.class);
                intent.putExtra("userName",userName);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        ImageButton imageButton = cardView.findViewById(R.id.delete);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View puWindow = getLayoutInflater().inflate(R.layout.deletepopup, null);
                PopupWindow popupWindow = new PopupWindow(puWindow, 1000, 596, true);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
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
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = db.document("user/"+userName+"/post/"+id);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    recreate();
                                    DocumentReference documentReference1 = db.document("post/"+id);
                                    documentReference1.delete();
                                    DocumentReference documentReference2 = db.document("reply/"+id);
                                    documentReference2.delete();
                                    popupWindow.dismiss();
                                }else {

                                }
                            }
                        });
                    }
                });
                    }
        });

    }


}