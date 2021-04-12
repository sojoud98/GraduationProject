
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
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Customers_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Admins_frag extends Fragment {
    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SQLiteDatabase db;
    LinearLayout linear;
    public Admins_frag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Admins_frag newInstance(String param1, String param2) {
        Admins_frag fragment = new Admins_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void showAdmins(View view) {
        db = new Database(getContext()).getWritableDatabase();
        linear = view.findViewById(R.id.linear);
        linear = view.findViewById(R.id.linear);
        Cursor res = db.rawQuery("select * from admins", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            String name = res.getString(res.getColumnIndex("name"));
            final String mobile = res.getString(res.getColumnIndex("phone"));
            View card = getLayoutInflater().inflate(R.layout.card, linear, false);
            TextView n = card.findViewById(R.id.name);
            n.setText(name);
            Log.d("Test", " Name" + name);
            TextView a = card.findViewById(R.id.mobile);
            a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse("tel: "+mobile);
                    Intent i = new Intent(Intent.ACTION_DIAL, u);
                    startActivity(i);
                }
            });
            a.setText(mobile);
            linear.addView(card);
            res.moveToNext();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.admins_frag, container, false);
        showAdmins(view);
        FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog(getContext());

            }

            @SuppressLint("ResourceAsColor")
            private void showAddItemDialog(Context c) {
                final EditText name = new EditText(c);
                final EditText mobile = new EditText(c);
                name.setHint("Name");
                mobile.setHint("Mobile");
                final TextView res = new TextView(c);
                res.setGravity(Gravity.CENTER);
                res.setText("");
                mobile.setInputType(InputType.TYPE_CLASS_PHONE);
                final LinearLayout linearLayout = new LinearLayout(c);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(name);
                linearLayout.addView(mobile);
                linearLayout.addView(res);

                AlertDialog dialog = new AlertDialog.Builder(c)
                        .setTitle("Add new admin")
                        .setView(linearLayout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Name = String.valueOf(name.getText());
                                String Mobile = String.valueOf(mobile.getText());
                                ContentValues values = new ContentValues();
                                values.put("name", Name);
                                values.put("phone", Mobile);
                                long id = db.insert("admins", null, values);
                                db.close();

                                if (id != -1) {
                                    if ((linear).getChildCount() > 0) {
                                        linear.removeAllViews();
                                    }
                                    res.setText("Admin added successfully");
                                    showAdmins(view);

                                } else {
                                    res.setText("Error! try again");

                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
            }
        });
        return  view;
    }

}