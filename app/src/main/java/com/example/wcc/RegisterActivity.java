package com.example.wcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EmptyStackException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText UserName;
    private EditText Email;
    private EditText Password;
    private Button CreateAcc;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar=(Toolbar)findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mAuth = FirebaseAuth.getInstance();
        UserName=(EditText) findViewById(R.id.RegisterUser);
        Email=(EditText)findViewById(R.id.RegisterEmail);
        Password=(EditText)findViewById(R.id.RegisterPassword);
        CreateAcc=(Button)findViewById(R.id.CreateAccount);

        progressBar=new ProgressDialog(this);
        CreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setMessage("Please wait while you are being registered");

                String userN = UserName.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                if (!TextUtils.isEmpty(userN) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    progressBar.show();
                    RegisterUsers(userN, email, password);
                }
            }
        });

    }

    private void RegisterUsers(final String userN, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();
                            // Write a message to the database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference().child("Users").child(uid);
                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",userN);
                            userMap.put("status","Im using Firechat");
                            userMap.put("image","defualt");
                            userMap.put("thumb","default");

                            myRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressBar.dismiss();
                                        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
