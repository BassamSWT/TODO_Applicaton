package com.bassam.todo_applicaton.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bassam.todo_applicaton.AddNewTask;
import com.bassam.todo_applicaton.Database.DatabaseHandler;
import com.bassam.todo_applicaton.MainActivity;
import com.bassam.todo_applicaton.Model.TodoModel;
import com.bassam.todo_applicaton.R;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{
    List<TodoModel> tasks;
    MainActivity activity;
    DatabaseHandler db;


    public TodoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        TodoModel item = tasks.get(position);
        holder.task.setText(item.getTasks());
        holder.task.setChecked(checked(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }
    public void editItem(int position){
        TodoModel item = tasks.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTasks());
        AddNewTask newTask = new AddNewTask();
        newTask.setArguments(bundle);
        newTask.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }

    private boolean checked(int n){
        return n!=0;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public void setTasks(List<TodoModel> tasks) {
        this.tasks = tasks;
    }
    public void deleteItem(int position){
        TodoModel item = tasks.get(position);
        db.deleteTask(item.getId());
        tasks.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.cb_task);
        }
    }
}
