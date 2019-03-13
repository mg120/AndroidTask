package com.androidtask.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_Name = "registerFile.db";
    public static final String Table_Name = "users";
    public static final String Col_1 = "_ID";
    public static final String Col_2 = "FirstName";
    public static final String Col_3 = "LastName";
    public static final String Col_4 = "Password";
    public static final String Col_5 = "Email";
    public static final String Col_6 = "Phone";
    public static final String Col_7 = "Gender";

    public DatabaseHelper(Context context) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Careate anew Database Table
        db.execSQL("CREATE TABLE " + Table_Name + "(" + Col_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + Col_2 + " Text," + Col_3 + " Text," + Col_4 + " Text," + Col_5 + " Text," + Col_6 + " Text," + Col_7 + " Integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);// Drop Older Table and Create a new table
        onCreate(db);
    }
}
