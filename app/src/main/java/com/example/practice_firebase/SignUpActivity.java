package com.example.practice_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText upname,upemail,uppass;
    TextView intextview;
    Button upbutton;
    private FirebaseAuth mAuth;
    private ProgressBar upprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up Activity");
        mAuth = FirebaseAuth.getInstance();
        upprogressBar=findViewById(R.id.sign_up_progressbar);
        upname=findViewById(R.id.sign_up_name);
        upemail=findViewById(R.id.sign_up_email);
        uppass=findViewById(R.id.sign_up_password);
        intextview=findViewById(R.id.sign_in_textview);
        upbutton=findViewById(R.id.sign_up_button);
        intextview.setOnClickListener(this);
        upbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_button:
                userRegister();
                break;
            case R.id.sign_in_textview:
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void userRegister() {
        final String name=upname.getText().toString().trim();
        String email=upemail.getText().toString().trim();
        String password=uppass.getText().toString().trim();
        if(name.isEmpty())
        {
            upname.setError("Enter Your Name");
            upname.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            upemail.setError("Enter an email address");
            upemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            upemail.setError("Enter a valid email address");
            upemail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            uppass.setError("Enter a password");
            uppass.requestFocus();
            return;
        }
        if(password.length()<6){
            uppass.setError("Minimum length of a password should be 6");
            uppass.requestFocus();
            return;
        }
        upprogressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                upprogressBar.setVisibility(View.GONE);
             if(task.isSuccessful()){
                 Toast.makeText(getApplicationContext(),"Register is Successful",Toast.LENGTH_SHORT).show();
                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                 DatabaseReference myRef = database.getReference("users");

                 myRef.child(mAuth.getCurrentUser().getUid()).setValue(name);
             }
             else
             {
                 if(task.getException() instanceof FirebaseAuthUserCollisionException){
                     Toast.makeText(getApplicationContext(),"User is already Registered",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     Toast.makeText(getApplicationContext(),"Error :"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                 }

             }
            }
        });

    }
}
