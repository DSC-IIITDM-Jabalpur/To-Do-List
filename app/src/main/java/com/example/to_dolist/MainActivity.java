package com.example.to_dolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.to_dolist.Data.DatabaseHandler;
import com.example.to_dolist.Model.Task;
import com.example.to_dolist.Ui.RecyclerViewAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Task> taskList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText tasks;
    private EditText startime ;
    private EditText deadline;
    private EditText status;
    private FloatingActionButton fab;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        //sets toolbar above on the page
        MaterialToolbar toolbar = findViewById(R.id.developerToolbar);
        setSupportActionBar(toolbar);

        //sets floating button on down right corner
        fab = findViewById(R.id.fab_button);

        //This functions invokes on pressing floating button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });

        //retrieving data from database
        databaseHandler = new DatabaseHandler(this);
        taskList = new ArrayList<>();
        taskList = databaseHandler.getAllTasks();

        //setting above obtained data to recyclerview
        recyclerViewAdapter = new RecyclerViewAdapter(this,taskList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.notifyDataSetChanged();

    }

    //Process begins from here
    private void createPopup() {

        //next 5 lines to create popup
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        //to getting all the views of popup
        tasks = view.findViewById(R.id.tasks_pop);
        startime =view.findViewById(R.id.starttime_pop);
        deadline = view.findViewById(R.id.deadline_pop);
        status = view.findViewById(R.id.status_pop);
        saveButton = view.findViewById(R.id.saveStatus);
        //TODO:save button on popup
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tasks.getText().toString().isEmpty() &&
                        !startime.getText().toString().isEmpty() &&
                        !deadline.getText().toString().isEmpty()&&
                        !status.getText().toString().isEmpty()) {
                    //and then saving it to database
                    saveTask(view);
                }else
                {
                    //if empty any field is empty
                    Snackbar.make(view,"Empty Not Allowed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void saveTask(View view) {
        //Creating an object of Model class
        Task task = new Task();
        //converting popup variables to variable of Model class
        String newTask = tasks.getText().toString().trim();
        int starttime =  Integer.parseInt(startime.getText().toString().trim());
        int dead =  Integer.parseInt(deadline.getText().toString().trim());
        String Status = status.getText().toString().trim();

        //Setting above value in task object
        task.setTaskname(newTask);
        task.setStarttime(starttime);
        task.setDeadline(dead);
        task.setStatus(Status);

        //and saving it to database
        databaseHandler.addTask(task);

        Snackbar.make(view,"Item Saved",Snackbar.LENGTH_SHORT).show();


        //Refreshing activity to view the data
        //handler is pre-existing class to add delay
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this , MainActivity.class));
            finish();

        },10);
        dialog.dismiss();

    }
}