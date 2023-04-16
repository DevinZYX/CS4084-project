package com.example.forum;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public DBHelper(@Nullable Context context,
                    @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory,
                    int version) {

        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table classes_db" +
                "(c_id Integer not null primary key autoincrement," +
                " c_name varchar(50) not null," +
                " c_time varchar(50) not null," +
                " c_day varchar(50) not null," +
                " c_teacher varchar(50) not null)");

        ContentValues contentValues = new ContentValues();

        contentValues.put("c_name", "0");

        contentValues.put("c_time", "0");

        contentValues.put("c_day", "0");

        contentValues.put("c_teacher", "0");


        for (int i = 1; i < 37; i++) {
            contentValues.put("c_id", i);
            sqLiteDatabase.insert("classes_db", "", contentValues);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



}
