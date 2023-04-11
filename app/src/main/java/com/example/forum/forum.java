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
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class forum extends AppCompatActivity{
    HashMap<String,String> dataToSave;
    private final String TITLE_KEY = "title";
    private final String CONTENT_KEY = "content";

    private final String USER_KEY = "userName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);
        createPost();
    }

    public void createPost(){
        //Create a Forum Page withing reading data from firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("post");

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = document.getData();
                    String userName = map.get("userName").toString();
                    String title = map.get("title").toString();
                    String content = map.get("content").toString();
                    String id = document.getId();
                    createCard(userName,title,content,id);
                    }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failure","Something went wrong");
            }
        });
    }

    public void postBtn(View view) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View puWindow = getLayoutInflater().inflate(R.layout.postpopup, null);
        PopupWindow popupWindow = new PopupWindow(puWindow, 1000, 700, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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

                if (title.isEmpty()) {
                    titleView.setError("This field cannot be empty.");
                    return;
                }else if (content.isEmpty()){
                    contentView.setError("This field cannot be empty");
                    return;
                }

                CollectionReference collectionReference = db.collection("post");
                dataToSave = new HashMap<String, String>();
                dataToSave.put(TITLE_KEY, title);
                dataToSave.put(CONTENT_KEY, content);
                dataToSave.put(USER_KEY, "John Doe");
                collectionReference.add(dataToSave).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            String id = task.getResult().getId();
                            DocumentReference document = db.collection("reply").document(id);
                            document.set(new HashMap<String, Object>()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        popupWindow.dismiss();
                                        recreate();
                                    }else {
                                        titleView.setError("Something went wrong");
                                    }
                                }
                            });



                        }else {
                            titleView.setError("Something went wrong");
                        }
                    }
                });
            }
        });

    }

    public void createCard(String userName, String title, String content, String id){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLayout =  findViewById(R.id.forum_linear);
        CardView cardView = (CardView) inflater.inflate(R.layout.card, linearLayout, false);
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
                Intent intent = new Intent(forum.this, postpage.class);
                intent.putExtra("userName",userName);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }



}
