package com.example.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class forum extends AppCompatActivity {

    private ArrayList<String> post;
    private String userName;
    private String title;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);
    }

    public void postBtn(View view){
        boolean isSuccess;
        View puWindow = getLayoutInflater().inflate(R.layout.postpopup,null);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.popupWindow);
        PopupWindow popupWindow = new PopupWindow(puWindow, 1000,700);
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

            }
        });
    }
}