package com.example.emptyactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        TextView textView = findViewById(R.id.registerTitle);
        int unicode = 0x1F601;

        String emoji = getEmoji(unicode);

        textView.setText("Créer un compte" + emoji);
    }
    public String getEmoji(int uni) {
        return new String(Character.toChars(uni));
    }
}