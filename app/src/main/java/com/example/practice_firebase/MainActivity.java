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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText inemail, inpass;
    TextView uptextview;
    Button inbutton;
    private FirebaseAuth mAuth;
    ProgressBar inprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Sign In Activity");
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }
        inprogressbar=findViewById(R.id.sign_in_progressbar);
        inemail = findViewById(R.id.sign_in_email);
        inpass = findViewById(R.id.sign_in_password);
        uptextview = findViewById(R.id.sign_up_textview);
        inbutton = findViewById(R.id.sign_in_button);
        uptextview.setOnClickListener(this);
        inbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                userLogin();
                break;
            case R.id.sign_up_textview:
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userLogin() {
        String email = inemail.getText().toString().trim();
        String password = inpass.getText().toString().trim();
        if (email.isEmpty()) {
            inemail.setError("Enter an email address");
            inemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inemail.setError("Enter a valid email address");
            inemail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            inpass.setError("Enter a password");
            inpass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            inpass.setError("Minimum length of a password should be 6");
            inpass.requestFocus();
            return;
        }
        inprogressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                inprogressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent =new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
