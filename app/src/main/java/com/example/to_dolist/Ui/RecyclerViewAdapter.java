package com.example.to_dolist.Ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.to_dolist.Data.DatabaseHandler;
import com.example.to_dolist.Model.Task;
import com.example.to_dolist.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflator;


    //constructor
    public RecyclerViewAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    //to initialize a row of list
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    //
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //binding data with list of recycler view XML
        Task task = taskList.get(position);
        holder.taskName.setText(task.getTaskname());
        holder.starttime.setText(MessageFormat.format("Starting on:  {0}", String.valueOf(task.getStarttime())));
        holder.deadline.setText(MessageFormat.format("Ending on:  {0}", String.valueOf(task.getDeadline())));
        holder.status.setText(MessageFormat.format("Status:  {0}", task.getStatus()));
        holder.dateAdded.setText(MessageFormat.format("Added on:  {0}", task.getDateadded()));


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView taskName;
        public TextView starttime;
        public TextView deadline;
        public TextView status;
        public TextView dateAdded;
        public Button editButton;
        public Button delete;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            //converting all list of recycler to java variables
            taskName = itemView.findViewById(R.id.taskName_list);
            starttime = itemView.findViewById(R.id.startime_list);
            deadline = itemView.findViewById(R.id.deadline_list);
            status = itemView.findViewById(R.id.status_list);
            dateAdded = itemView.findViewById(R.id.dateAdded_list);


            editButton = itemView.findViewById(R.id.editButton);
            delete = itemView.findViewById(R.id.deleteBut);

            editButton.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        //this which invokes when we click on edit or delete button
        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Task task = taskList.get(position);
            switch (view.getId()) {
                case R.id.editButton:
                    editTask(task);
                    break;

                case R.id.deleteBut:
                    deletetask(task.getId());
                    break;
            }
        }



        private void deletetask(final int id) {

            //to set popup
            builder = new AlertDialog.Builder(context);
            inflator = LayoutInflater.from(context);
            View view = inflator.inflate(R.layout.confirmation_popup, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();


            Button noButton = view.findViewById(R.id.conf_no_but);
            Button yesButton = view.findViewById(R.id.conf_yes_but);


            //on clicking yes button
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteTask(id);
                    taskList.remove(getAdapterPosition());
                    //worked only with getAdapterPosition not with id
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });

            //on clicking no button
            noButton.setOnClickListener(v -> dialog.dismiss());


        }


        private void editTask(final Task newtask) {

            Button saveButton_pop;
            final EditText tasks_pop;
            final EditText startime_pop ;
            final EditText deadline_pop;
            final EditText status_pop;

            builder = new AlertDialog.Builder(context);
            inflator = LayoutInflater.from(context);
            final View  view = inflator.inflate(R.layout.popup,null);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();


            //converting vies from the popup to java variables
            tasks_pop = view.findViewById(R.id.tasks_pop);
            startime_pop =view.findViewById(R.id.starttime_pop);
            deadline_pop = view.findViewById(R.id.deadline_pop);
            status_pop = view.findViewById(R.id.status_pop);

            saveButton_pop = view.findViewById(R.id.saveStatus);
            saveButton_pop.setText(R.string.Update);

            tasks_pop.setText(newtask.getTaskname());
            startime_pop.setText(String.valueOf(newtask.getStarttime()));
            deadline_pop.setText(String.valueOf(newtask.getDeadline()));
            status_pop.setText(newtask.getStatus());



            saveButton_pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    newtask.setTaskname(tasks_pop.getText().toString());
                    newtask.setStarttime((Integer.parseInt(startime_pop.getText().toString().trim())));
                    newtask.setDeadline(Integer.parseInt(deadline_pop.getText().toString().trim()));
                    newtask.setStatus(status_pop.getText().toString());

                    if(!tasks_pop.getText().toString().isEmpty() &&
                            !(startime_pop.getText().toString().isEmpty()) &&
                            deadline_pop.getText().toString().trim().length()!=0 &&
                            !status_pop.getText().toString().isEmpty()) {
                        //update function of database
                        db.updateTask(newtask);
                        notifyItemChanged(getAdapterPosition(),newtask);//important
                    }else
                    {
                        Snackbar.make(view,"Empty Not Allowed",Snackbar.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });

        }
    }
}
