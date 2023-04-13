package com.example.forum;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class postpage extends AppCompatActivity {
    private String userName = "john doe";
    private String title = "asdasd";
    private String content = "asdad";
    private String id = "1223";
    private int length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMessage();
        getSupportActionBar().setTitle("Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.postpage);
        createPostPage();
    }

    public void getMessage(){
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        id = intent.getStringExtra("id");
    }

    public void createPostPage(){

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.reply_linear);

        //add first card view
        CardView cardView = (CardView) inflater.inflate(R.layout.postcard,linearLayout,false);
        TextView titleView = cardView.findViewById(R.id.Post_Title);
        TextView userView = cardView.findViewById(R.id.Post_UserName);
        TextView contentView = cardView.findViewById(R.id.Post_Content);
        titleView.setText(title);
        userView.setText(userName);
        contentView.setText(content);
        linearLayout.addView(cardView);

        //add text view for comment
        TextView textView = new TextView(this);
        textView.setText("Comments");
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(30, 0, 30, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);


        //add comment cardviews
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("reply").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Map<String,Object> map = document.getData();
                        length = map.size();
                        for (Map.Entry<String, Object> entry: map.entrySet()) {
                            String value = entry.getValue().toString();
                            CardView replyView = (CardView) inflater.inflate(R.layout.replycard, linearLayout, false);
                            TextView replay_userView = replyView.findViewById(R.id.Reply_UserName);
                            TextView replay_contentView = replyView.findViewById(R.id.Reply_Content);
                            String userName1 = value.substring(0,value.indexOf("+"));
                            String content1 = value.substring(value.indexOf("+")+1);
                            replay_userView.setText(userName1);
                            replay_contentView.setText(content1);
                            linearLayout.addView(replyView);
                        }
                    }else {

                    }
                }else{
                    Log.e("","so badddddd");
                }
            }
        });

    }

    public void reply(View view){

        View replyWindow = getLayoutInflater().inflate(R.layout.replypopup,null);
        PopupWindow popupWindow = new PopupWindow(replyWindow, 1000, 700, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        Button cancel = replyWindow.findViewById(R.id.cancelBtn);
        Button confirm = replyWindow.findViewById(R.id.confirmBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText contentView = (EditText) replyWindow.findViewById(R.id.content);
                String content1 = contentView.getText().toString();

                if (content1.isEmpty()){
                    contentView.setError("This field cannot be empty");
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("reply").document(id);
                Map<String, Object> dataToSave = new HashMap<>();
                dataToSave.put(String.valueOf(length),userName+"+"+content1);

                documentReference.update(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            popupWindow.dismiss();
                            recreate();
                        }else {

                        }
                    }
                });

            }
        });
    }

}