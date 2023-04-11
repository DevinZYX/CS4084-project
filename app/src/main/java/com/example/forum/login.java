package com.example.forum;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity {
    public final String PASSWORD_KEY = "password";
    private String userName_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void login(View view) {
        EditText userView = findViewById(R.id.userName);
        EditText passwordView = findViewById(R.id.password);
        String userName = userView.getText().toString();
        String password = passwordView.getText().toString();

        if (userName.isEmpty()) {
            userView.setError("Username cannot be empty.");
        } else if (password.isEmpty()) {
            passwordView.setError("Password cannot be empty");
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.document("user/" + userName);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> map = new HashMap<>();
                            map = document.getData();
                            String db_password = map.get("password").toString();
                            if ((!db_password.isEmpty()) && password.equals(db_password)) {
                                userName_pass = userName;
                                Log.e("", "happy");
                            }
                        } else {
                            userView.setError("User Does not Exist");
                        }
                    }else{
                        Log.e("login","Something went wrong");
                    }
                }
            });
        }
    }

    public void redirctResgiter(View view){
        Intent intent = new Intent(this,register.class);
        startActivity(intent);
    }
}


