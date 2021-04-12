package com.example.grduationproject;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<MyRequest> {
    ArrayList<MyRequest> dataSet;
    Context mContext;
    MyRequest req;
    SQLiteDatabase db;

    public MyAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<MyRequest> data) {
        super(context, 0, data);
        this.dataSet = data;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.info, parent, false);

        req = dataSet.get(position);

        final CheckBox served = listItem.findViewById(R.id.served);
        TextView mobile = listItem.findViewById(R.id.mobile);
        TextView date = listItem.findViewById(R.id.data);
        TextView service = listItem.findViewById(R.id.service);
        mobile.setText("Phone number: " + req.getMobile());
        service.setText("Service: " + req.getService());
        date.setText("date: " + req.getDate() + "  time: " + req.getTime());
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + req.getMobile());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                mContext.startActivity(i);
            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView note = new TextView(getContext());
                final TextView info = new TextView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(10, 10, 10, 10);
                info.setText("Info: " + req.getInfo());
                final LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(info);

                linearLayout.addView(note);
                linearLayout.setLayoutParams(params);
                note.setLayoutParams(params);
                info.setLayoutParams(params);
                AlertDialog d = new AlertDialog.Builder(getContext()).setTitle("Ok")
                        .setPositiveButton("call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("cancel", null).setCancelable(true).create();
                d.show();

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Request information")
                        .setView(linearLayout)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Uri u = Uri.parse("tel:" + req.getMobile());
                                Intent i = new Intent(Intent.ACTION_DIAL, u);
                                mContext.startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
            }
        });
        db = new Database(getContext()).getWritableDatabase();
        boolean isserved;
        if (req.getServed().equals("1")) isserved = true;
        else isserved = false;

        served.setChecked(isserved);
        served.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean bb;
                int b;
                if (buttonView.isChecked()) {
                    b = 1;
                    bb = true;
                } else {
                    b = 0;
                    bb = false;
                }
                buttonView.setChecked(bb);

                ContentValues cv = new ContentValues();
                cv.put("served", b); //These Fields should be your String values of actual column names
                db.update("request", cv, "id=?", new String[]{"" + req.getId()});
                Cursor c = db.query("Request", null, null, null, null, null, null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    Log.d("test served", "onCheckedChanged: " + c.getString(c.getColumnIndex("served")));
                    c.moveToNext();
                }
            }
        });

        return listItem;


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}