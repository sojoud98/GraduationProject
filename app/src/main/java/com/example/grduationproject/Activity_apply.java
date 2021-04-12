package com.example.grduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Activity_apply extends AppCompatActivity {
    Button applyBtn;
    FirebaseAuth firebaseAuth;
    SQLiteDatabase db;
    TextView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        applyBtn = findViewById(R.id.apply_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        feedback = findViewById(R.id.feedback);
        db = new Database(this).getWritableDatabase();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentVals = new ContentValues();
                contentVals.put("mobile", firebaseAuth.getCurrentUser().getPhoneNumber());

                long result = db.insert("applications", null, contentVals);

                if (result == -1) {
                    Log.e("Database logs"," adding applications failed" );
                    feedback.setText("Application declined, please apply again");
                }
                else {
                    Log.d("Database logs"," adding applications done" );
                    feedback.setText("Application sent! we will contact you soon");

                }
            }
        });
    }
}