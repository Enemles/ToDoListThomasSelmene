package com.example.emptyactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emptyactivity.Adapter.ToDoAdapter;
import com.example.emptyactivity.Adapter.ToDoAdapterFB;
import com.example.emptyactivity.Model.ToDoModel;
import com.example.emptyactivity.Model.ToDoModelFB;
import com.example.emptyactivity.Utils.DatabaseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DialogCloseListener {
    private long backPressedTime;
    private RecyclerView recyclerView;
    //private ToDoAdapter tasksAdapter;
    //private List<ToDoModel> taskList;
    private ImageView logOut;


    private FloatingActionButton fab;
    private FloatingActionButton fileOpen;

     //private DatabaseHandler db;
     private FirebaseAuth mAuth;
     private FirebaseFirestore firestore;
     private ToDoAdapterFB adapterFB;
     private List<ToDoModelFB> mList;
     private Query query;
     private ListenerRegistration listenerRegistration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();


        //db = new DatabaseHandler(this);
        //db.openDatabase();

        logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));

            }
        });

        fileOpen = findViewById(R.id.fileOpen);

        fileOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FileInteraction.class);
                startActivity(intent);
            }
        });

        //taskList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //tasksAdapter = new ToDoAdapter(this);
        //recyclerView.setAdapter(tasksAdapter);

        logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        fab = findViewById(R.id.fab);
        firestore = FirebaseFirestore.getInstance();


        //taskList = db.getAllTasks();
        //Collections.reverse(taskList);
        //tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        mList = new ArrayList<>();
        adapterFB = new ToDoAdapterFB(HomeActivity.this, mList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapterFB));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();

        recyclerView.setAdapter(adapterFB);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d("TAG", "user not logged");
        } else {
            TextView message = findViewById(R.id.registerTitle);
            int unicode = 0x1F44B;
            String emoji = getEmoji(unicode);
            message.setText("Hello" + " " + currentUser.getDisplayName() + " " + emoji);
        }
    }

    private void showData(){
        query = firestore.collection("task").orderBy("time" , Query.Direction.DESCENDING);

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        ToDoModelFB toDoModelFB = documentChange.getDocument().toObject(ToDoModelFB.class).withId(id);
                        mList.add(toDoModelFB);
                        adapterFB.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
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

    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapterFB.notifyDataSetChanged();
    }
}