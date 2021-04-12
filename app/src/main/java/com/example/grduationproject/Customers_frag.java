
package com.example.grduationproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Customers_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Customers_frag extends Fragment {
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
    RadioGroup radiogroup;
    RadioButton all, cutomersB, membersB;
    public Customers_frag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Customers_frag newInstance(String param1, String param2) {
        Customers_frag fragment = new Customers_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void showCustomers(View view , int users) {
        db = new Database(getContext()).getWritableDatabase();

        linear = view.findViewById(R.id.linear);
        linear.removeAllViews();

        Cursor res = db.rawQuery("select * from customers", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            String name = res.getString(res.getColumnIndex("name"));
            final String mobile = res.getString(res.getColumnIndex("phone"));
            final String address = res.getString(res.getColumnIndex("address"));
            View card = getLayoutInflater().inflate(R.layout.card, linear, false);
            TextView n = card.findViewById(R.id.name);
            n.setText(name);
            TextView a = card.findViewById(R.id.mobile);
            TextView addressTxt = card.findViewById(R.id.address);
            a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse("tel: "+mobile);
                    Intent i = new Intent(Intent.ACTION_DIAL, u);
                    startActivity(i);
                }
            });
            a.setText(mobile);
            addressTxt.setText(address);

            Cursor user = db.rawQuery("select * from applications where mobile = ?" , new String[]{mobile});

            if (user.getCount() < 1 && (users == 1 || users == 2)) {
                ImageView member = view.findViewById(R.id.memberImg);
                member.setVisibility(View.INVISIBLE);
                linear.addView(card);

            }
            else if (user.getCount() > 0 && (users == 1 || users == 3)) {
                linear.addView(card);
            }

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

        view =  inflater.inflate(R.layout.customers_frag, container, false);


        showCustomers(view, 1);


        return  view;
    }
}