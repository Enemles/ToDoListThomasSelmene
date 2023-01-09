package com.example.emptyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private TextView resetPassBack;
    private EditText resetPassEmail;
    private Button resetPassBtn;
    private ProgressBar resetPassProgressBar;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

         resetPassBack = findViewById(R.id.resetPassBack);
         resetPassBtn = findViewById(R.id.resetPassBtn);
         resetPassEmail = findViewById(R.id.resetPassEmail);

        auth = FirebaseAuth.getInstance();

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        resetPassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void resetPassword(){
        String email = resetPassEmail.getText().toString().trim();

        if (email.isEmpty()) {
            resetPassEmail.setError("L'adresse mail est requis");
            resetPassEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            resetPassEmail.setError("Merci de founir une adresse mail valide");
            resetPassEmail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new  OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Un email vient de vous être envoyé, vérifiez vos spam.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgotPassword.this, "L'adresse mail n'existe pas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    };
}