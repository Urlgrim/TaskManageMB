package com.example.taskmanagement.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.taskmanagement.AddNewTask;
import com.example.taskmanagement.MainActivity;
import com.example.taskmanagement.Model.TaskModel;
import com.example.taskmanagement.R;
import com.example.taskmanagement.Utils.DataBaseHelper;

import java.util.List;



public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<TaskModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public TaskAdapter(DataBaseHelper myDB, MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TaskModel item = mList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    myDB.updateStatus(item.getId(),1);
                }else {
                    myDB.updateStatus(item.getId(),0);
                }
            }
        });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTasks(List<TaskModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        TaskModel item = mList.get(position);
        myDB.deleteTask(item.getId());

        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItems(int position){
        TaskModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("Id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());


    }
    public  static class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox =  itemView.findViewById(R.id.checkBox);
        }
    }
}
