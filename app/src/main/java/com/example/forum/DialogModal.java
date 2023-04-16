package com.example.forum;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DialogModal extends Activity {
    Button close_activity;
    Button save_activity;
    Spinner selected_time;
    Spinner selected_day;
    EditText subject;
    EditText teacher;

    public final String DB_NAME = "classes_db.db";
    public final String TABLE_NAME = "classes_db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set);

        setFinishOnTouchOutside (true);

        close_activity=(Button) findViewById(R.id.close_activity);
        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogModal.this.finish();
            }
        });

        save_activity = findViewById(R.id.save_activity);
        save_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                selected_day = findViewById(R.id.selected_day);
                String day = selected_day.getSelectedItem().toString();

                if (day.equals("--Select--")) {
                    save_activity.setError("");
                    return;
                }
                selected_time = findViewById(R.id.selected_time);
                String time = selected_time.getSelectedItem().toString();
                if (time.equals("--Select--")) {
                    save_activity.setError("");
                    return;
                }

                subject = findViewById(R.id.subject);
                String text = subject.getText().toString();
                if ("".equals(text)) {
                    save_activity.setError("");
                    return;
                }

                teacher = findViewById(R.id.teacher);
                String teacher1 = teacher.getText().toString();
                if ("".equals(teacher1)) {
                    save_activity.setError("");
                    return;
                }

                DBHelper dbHelper = new DBHelper(DialogModal.this, DB_NAME, null, 1);

                ContentValues contentValues = new ContentValues();

                contentValues.put("c_id", combineId(day, time));
                contentValues.put("c_name", text);
                contentValues.put("c_time", time);
                contentValues.put("c_day", day);
                contentValues.put("c_teacher", teacher1);

                update(dbHelper, contentValues);

                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.setClass(DialogModal.this, TimeTable.class);
                startActivity(intent);
            }
        });
    }

    public String combineId(String day, String time) {

        int day1 = utils.getDay(day);


        int time1 = Integer.parseInt(time.substring(0, 1));

        return String.valueOf((day1 - 1) * 5 + ((time1 - 1)/2 + 1));
    }

    public void update(DBHelper dbHelper, ContentValues contentValues){

        String []a = {contentValues.get("c_id").toString()};


        SQLiteDatabase db=dbHelper.getWritableDatabase();

        db.update(TABLE_NAME,contentValues,"c_id=?", a);


        db.close();
    }

}
