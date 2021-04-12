package com.example.grduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.grduationproject.databinding.ActivityProfileBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityCleaning extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityProfileBinding binding;
    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String phone;
    int hour, minute;
    TextView timeSel;
    private DatePickerDialog datePickerDialog;
    private Button dateButton, makeOrderBtn;
    EditText number_input;
    SQLiteDatabase db;
    Context Thiscontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning);
        makeOrderBtn = findViewById(R.id.make_order_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        Thiscontext = this;
        db = new Database(this).getWritableDatabase();
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        initDatePicker();
        dateButton = findViewById(R.id.datepickerBtn);
        dateButton.setText(getTodaysDate());
        timeSel = findViewById(R.id.timeSelect);
        phone = firebaseAuth.getCurrentUser().getPhoneNumber();

        number_input = findViewById(R.id.rooms_number_input);
        makeOrderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean done = addRequest();
                if (done) {
                    findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                    makeOrderBtn.setEnabled(false);
                }
                else{
                    Toast.makeText(ActivityCleaning.this, "Failed, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        timeSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ActivityCleaning.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                minute = minute;

                                String time = hour + ":" + minute;
                                SimpleDateFormat f24Hour = new SimpleDateFormat("HH:MM");
                                try {
                                    Date date = f24Hour.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    timeSel.setText(f12Hours.format((date)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },12,0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour, minute);
                timePickerDialog.show();
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return  getMonthFormat(month) + " "+ dayOfMonth +" "+ year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1: return  "Jan";
            case 2: return  "Feb";
            case 3: return  "Mar";
            case 4: return  "Apr";
            case 5: return  "May";
            case 6: return  "Jun";
            case 7: return  "Jul";
            case 8: return  "Aug";
            case 9: return  "Sep";
            case 10: return  "Oct";
            case 11: return  "Nov";
            case 12: return  "Dec";
            default: return  "Mon";
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(ActivityCleaning.this, ActivityHome.class));

                break;
            case R.id.nav_profile:

                startActivity(new Intent(ActivityCleaning.this, Activity_profile.class));

                break;
            case R.id.nav_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(ActivityCleaning.this, MainActivity.class));

                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }


    public boolean addRequest() {

        ContentValues contentVals = new ContentValues();
        contentVals.put("mobile", phone);
        contentVals.put("service", "cleaning");
        contentVals.put("info", "Rooms: " + number_input.getText().toString());
        contentVals.put("time", timeSel.getText().toString());
        contentVals.put("date", dateButton.getText().toString());
        Log.d("request", contentVals.toString());
        long result = db.insert("request", null, contentVals);
        if (result == -1) {
            Log.e("Database logs"," adding request failed" );

        }
        else {
            Log.d("Database logs"," adding request done" );
        }
        return result== -1? false : true;
    }


}