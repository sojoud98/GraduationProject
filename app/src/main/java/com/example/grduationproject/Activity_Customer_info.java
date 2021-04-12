package com.example.grduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Customer_info extends AppCompatActivity {
    EditText nameTxt, AddressTxt;
    Button btn;
    Database db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__customer_info);
        nameTxt = findViewById(R.id.name_txt);
        AddressTxt = findViewById(R.id.address_txt);
        btn = findViewById(R.id.make_order_btn);
        firebaseAuth = FirebaseAuth.getInstance();

        db = new Database(this);
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameTxt.getText().toString().length() != 0 &&  AddressTxt.getText().toString().length() != 0 ) {
                    db.addCustomer(nameTxt.getText().toString(), firebaseUser.getPhoneNumber(), AddressTxt.getText().toString());
                    Toast.makeText(Activity_Customer_info.this, "Customer Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Activity_Customer_info.this, ActivityAdminHome.class));

                }

                else {
                    Toast.makeText(Activity_Customer_info.this, "please fill info", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}