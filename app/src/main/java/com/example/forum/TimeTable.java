package com.example.forum;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeTable extends AppCompatActivity implements View.OnTouchListener {

    public final String DB_NAME = "classes_db.db";
    public final String TABLE_NAME = "classes_db";

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("TimeTable");
        userName = getIntent().getStringExtra("userName");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        DBHelper dbHelper = new DBHelper(TimeTable.this, DB_NAME, null, 1);

        framework();

        applyDraw(dbHelper);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TimeTable.this, MainActivity.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
        finish();
    }

    public GridLayout LayoutColumn(int i) {

        GridLayout gridLayout = findViewById(R.id.d1);

        switch (i) {
            case 1: {
                gridLayout = findViewById(R.id.d1);
                break;
            }
            case 2: {
                gridLayout = findViewById(R.id.d2);
                break;
            }
            case 3: {
                gridLayout = findViewById(R.id.d3);
                break;
            }
            case 4: {
                gridLayout = findViewById(R.id.d4);
                break;
            }
            case 5: {
                gridLayout = findViewById(R.id.d5);
                break;
            }
            case 6: {
                gridLayout = findViewById(R.id.d6);
                break;
            }
            case 7: {
                gridLayout = findViewById(R.id.d7);
                break;
            }
        }
        return gridLayout;
    }

    public void framework() {

        GridLayout gridLayout;
        int id = 1;

        for (int i = 1; i < 8; i++) {

            gridLayout = LayoutColumn(i);

            for (int j = 1; j < 10; j +=2) {
                TextView textView1 = new TextView(this);

                textView1.setId(id++);
                textView1.setText("");
                textView1.setMaxLines(5);
                textView1.setEllipsize(TextUtils.TruncateAt.END);
                textView1.setBackgroundColor(Color.parseColor("#F0FFFF"));
                textView1.setGravity(Gravity.CENTER);


                GridLayout.LayoutParams params1 = new GridLayout.LayoutParams();
                params1.rowSpec = GridLayout.spec( j, 2,1);
                params1.setMargins(5,10,5,10);
                params1.width = GridLayout.LayoutParams.MATCH_PARENT;
                params1.height = 0;

                gridLayout.addView(textView1, params1);
            }

        }

    }

    public void applyDraw(DBHelper dbHelper) {
        List<Classes> classes = query(dbHelper);

        for (Classes aClass : classes) {
            int i = Integer.parseInt(aClass.getC_time().charAt(0) + "");

            int j = utils.getDay(aClass.getC_day());

            TextView Class = findViewById((j - 1) * 5 + ((i - 1)/2 + 1));

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            if (aClass.getC_day().equals(simpleDateFormat.format(date).toString())) {
                Class.setBackgroundColor(Color.rgb(28, 217, 204));
            }

            Class.setText(aClass.getC_name());

            Class.setOnTouchListener(this);

        }

    }

    @SuppressLint("Range")
    public List<Classes> query(DBHelper dbHelper) {

        List<Classes> classes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            if (!cursor.getString(cursor.getColumnIndex("c_day")).equals("0")) {
                Classes Class = new Classes();

                Class.setC_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("c_id"))));
                Class.setC_name(cursor.getString(cursor.getColumnIndex("c_name")));
                Class.setC_time(cursor.getString(cursor.getColumnIndex("c_time")));
                Class.setC_day(cursor.getString(cursor.getColumnIndex("c_day")));

                classes.add(Class);
            }

            cursor.moveToNext();

        }

        db.close();
        return classes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_menu, menu);


        MenuItem menuItem=menu.findItem(R.id.action_menu);

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                intent.setClass(TimeTable.this, DialogModal.class);
                startActivity(intent);
                return true;
            }
        });

        return  super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("Range")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN: {
                TextView textView = (TextView) view;


                DBHelper dbHelper = new DBHelper(TimeTable.this, DB_NAME, null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Cursor cursor = db.query(TABLE_NAME, null, "c_id=?", new String[]{String.valueOf(textView.getId())}, null, null, null);
                Cursor cursor = db.query(TABLE_NAME, null, "c_id=?", new String[]{String.valueOf(textView.getId())}, null, null, null);

                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    Classes Class = new Classes();
                    System.out.println(textView.getId());
                    System.out.println(cursor.getString(cursor.getColumnIndex("c_name")));

                    Intent intent = new Intent();
                    intent.putExtra("name", cursor.getString(cursor.getColumnIndex("c_name")));
                    intent.putExtra("time", cursor.getString(cursor.getColumnIndex("c_time")));
                    intent.putExtra("day", cursor.getString(cursor.getColumnIndex("c_day")));
                    intent.putExtra("teacher", cursor.getString(cursor.getColumnIndex("c_teacher")));
                    intent.setClass(TimeTable.this, Detail.class);
                    startActivity(intent);

                }
               return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Intent intent = new Intent(this, TimeTable.class);
                startActivity(intent);
                break;
            }
        }

        return false;
    }

}