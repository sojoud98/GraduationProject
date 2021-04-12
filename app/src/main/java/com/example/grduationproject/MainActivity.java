package com.example.grduationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grduationproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String mVertificationId;
    private static final String TAG = "Main_tag";
    private FirebaseAuth firebaseAuth;
    SQLiteDatabase db;

    private ProgressDialog dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new Database(this).getWritableDatabase();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        binding.phoneLl.setVisibility(View.VISIBLE);
        binding.codeLl.setVisibility(View.GONE);
        dp = new ProgressDialog(this);
        dp.setTitle("Please wait");
        dp.setCanceledOnTouchOutside(false);


        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signWithPhoneAuthCredential(phoneAuthCredential);
            }
//
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                dp.dismiss();
                Toast.makeText(MainActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

            }
//
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d("Bitch", "??");
                super.onCodeSent(s, forceResendingToken);
                mVertificationId = s;
                forceResendingToken = forceResendingToken;
                dp.dismiss();

                binding.phoneLl.setVisibility(View.GONE);

                binding.codeLl.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "code sent...", Toast.LENGTH_SHORT).show();
                binding.codeSendDesc.setText("Please type code\n"+
                        binding.phoneEt.getText().toString().trim());
            }
        };
        binding.phoneContinueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String phone = binding.phoneEt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(MainActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                }
                else {

                    startPhoneNumberVertification(phone);
                }
            }
        });
        binding.resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone =binding.phoneEt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(MainActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    resendVerificationCode(phone, forceResendingToken);

                }
            }
        });

        binding.codeSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code =binding.codeEt.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(MainActivity.this, "Please enter Verification code...", Toast.LENGTH_SHORT).show();
                }
                else {
                    verificationPhoneNumberWithCode(mVertificationId, code);

                }

            }
        });
    }
//
    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        dp.setMessage("Resend");
        dp.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallback)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVertification(String phone) {

        dp.setMessage("Verifying Phone Number");
        dp.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallback)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verificationPhoneNumberWithCode(String mVertificationId, String code) {
        dp.setMessage("Verify Code");
        dp.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVertificationId, code);
        signWithPhoneAuthCredential(credential);
    }

    private void signWithPhoneAuthCredential(PhoneAuthCredential credential) {
        dp.setMessage("Logging in");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dp.dismiss();
                        String phone = binding.phoneEt.getText().toString().trim();
                        Cursor res = db.rawQuery("select phone from admins", null);
                        res.moveToFirst();
                        while (res.isAfterLast() == false) {
                             String mobile = res.getString(res.getColumnIndex("phone"));
                            if (mobile.equals(phone)) {
                                Toast.makeText(MainActivity.this, "Logged In as Admin "+phone, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, ActivityAdminHome.class));
                                return;
                            }
                            res.moveToNext();
                        }


                        Toast.makeText(MainActivity.this, "Logged In as "+phone, Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(MainActivity.this, ActivityHome.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dp.dismiss();
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}