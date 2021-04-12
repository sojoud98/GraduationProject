package com.example.grduationproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;

public class Database  extends SQLiteOpenHelper {


     public static final String DATABASE_NAME = "EcoHome.db";

        public Database(Context context) {

            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table admins " +
                    "(id integer primary key AUTOINCREMENT," +
                    "name text," +
                    "phone text)");
            db.execSQL("create table applications (id integer primary key AUTOINCREMENT, mobile text)");

            db.execSQL("create table request " +
                    "(id integer primary key AUTOINCREMENT," +
                    "mobile text," +
                    "service text," +
                    "info text,"+
                    "time text," +
                    "served boolean DEFAULT 0," +
                    "date text)");

            db.execSQL("create table customers " +
                    "(id integer primary key AUTOINCREMENT," +
                    "name text," +
                    "phone text," +
                    "address text)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    public boolean addCustomer(String name, String phoneNumbet, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentVals = new ContentValues();
        contentVals.put("name", name);
        contentVals.put("phone", phoneNumbet);
        contentVals.put("address", address);
        long result = db.insert("customers", null, contentVals);
        if (result == -1) {
            Log.e("Database logs"," adding customer failed" );

        }
        else {
            Log.d("Database logs"," adding customer done" );
        }
        return result== -1? false: true;
    }



}
