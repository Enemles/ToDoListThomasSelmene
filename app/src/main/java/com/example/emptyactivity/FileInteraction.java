package com.example.emptyactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileInteraction extends AppCompatActivity {
    private ImageView logOut;
    private FloatingActionButton fab;
    private FloatingActionButton share;
    Button write, read;
    TextView text;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_interaction);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FileInteraction.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        share = findViewById(R.id.shared);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FileInteraction.this, SharedPreference.class);
                startActivity(intent);
            }
        });

        logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FileInteraction.this, LoginActivity.class));
            }
        });

        write = findViewById(R.id.write);
        read = findViewById(R.id.read);
        text = findViewById(R.id.output);
        input = findViewById(R.id.input);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = input.getText().toString();
                writeToFile("file.txt", data);
                input.setText("");
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = readFromFile("file.txt");
                text.setText(data);
            }
        });
    }
    public void writeToFile(String fileName, String content) {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream write = new FileOutputStream(new File(path, fileName));
            write.write(content.getBytes());
            write.close();
            Toast.makeText(this, "Ecrit dans " + getFilesDir() + "/" + fileName, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String readFromFile(String filename){
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, filename);
        byte[] data = new byte[(int) readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(data);
            return new String(data);
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }

    }
}