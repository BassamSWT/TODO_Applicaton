package com.bassam.todo_applicaton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bassam.todo_applicaton.Adapter.TodoAdapter;
import com.bassam.todo_applicaton.Database.DatabaseHandler;
import com.bassam.todo_applicaton.Model.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    RecyclerView rvTasks;
    TodoAdapter adapter;
    List<TodoModel> tasks;
    DatabaseHandler db;
    FloatingActionButton fabNewTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        tasks = new ArrayList<>();
        rvTasks = findViewById(R.id.rv_tasks);
        fabNewTask = findViewById(R.id.btn_add);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoAdapter(db,this);
        adapter.notifyDataSetChanged();
        rvTasks.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(rvTasks);

        fabNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

        tasks = db.getAllTasks();
        Collections.reverse(tasks);
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasks = db.getAllTasks();
        Collections.reverse(tasks);
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }
}