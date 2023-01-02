package com.example.emptyactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private long backPressedTime;
    private EditText registerUsername, registerPwd;
    private Button registerBtn;
    private TextView registerLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        TextView textView = findViewById(R.id.registerTitle);
        int unicode = 0x1F601;
        String emoji = getEmoji(unicode);
        textView.setText("Créer un compte" + emoji);

        registerUsername = findViewById(R.id.registerUsername);
        registerPwd = findViewById(R.id.registerPassword);
        registerBtn = findViewById(R.id.registerButton);
        registerLogin = findViewById(R.id.registerLogin);

        registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public String getEmoji(int uni) {
        return new String(Character.toChars(uni));
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}