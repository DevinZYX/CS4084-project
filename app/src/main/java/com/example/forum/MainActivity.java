package com.example.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> post;
    private String userName;
    private String title;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void postBtn(View view){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.forum);
        PopupWindow popupWindow = new PopupWindow();
    }
}