package com.example.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view){
        View registerPage = getLayoutInflater().inflate(R.layout.activity_register, null);
        EditText userView = registerPage.findViewById(R.id.userName);
        EditText passwordView = registerPage.findViewById(R.id.password);
        String userName = userView.getText().toString();
        String password = passwordView.getText().toString();

        if (userName.isEmpty()) {
            userView.setError("Username cannot be empty.");
        } else if (password.isEmpty()) {
            passwordView.setError("Password cannot be empty");
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.document("user");
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userView.setError("User Already Exists");
                        }else{
                            Map<String, Object> map = new HashMap<>();
                            map.put("password",password);
                            db.collection("user").document(userName).set(map);
                        }
                    } else{
                        Log.e("Failure","Something went wrong");
                    }
                }
            });
        }
    }
}
