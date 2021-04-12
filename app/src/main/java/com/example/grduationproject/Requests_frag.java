package com.example.grduationproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Requests_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Requests_frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;    MyRequest req;

    public Requests_frag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Requests_frag newInstance(String param1, String param2) {
        Requests_frag fragment = new Requests_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.requests_frag, container, false);
        List<MyRequest> list = new ArrayList<>();
        SQLiteDatabase db = new Database(getContext()).getReadableDatabase();


        Cursor cursor = db.query("request", null, null, null, null, null, "date ASC");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            req = new MyRequest(
                    Long.parseLong(cursor.getString(cursor.getColumnIndex("id"))),
                    cursor.getString(cursor.getColumnIndex("info")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("mobile")),
                    cursor.getString(cursor.getColumnIndex("service")),
                    cursor.getString(cursor.getColumnIndex("served"))

            );
            list.add(req);
            cursor.moveToNext();
        }
        ListView listView = view.findViewById(R.id.listv);
        MyAdapter adapter = new MyAdapter(getContext(), (ArrayList<MyRequest>) list);
        listView.setAdapter(adapter);
        return view;
    }


}