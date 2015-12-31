package com.example.dleam.todo_app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.fragments.TaskEditDialog;
import com.example.dleam.todo_app.models.TodoTask;

public class TaskViewActivity extends AppCompatActivity implements TaskEditDialog.TaskEditDialogListener {
    private TodoTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        Bundle extras = getIntent().getExtras();
        mTask = (TodoTask) extras.getSerializable("task");

        setTaskValues();
    }

    private void setTaskValues() {
        TextView mTaskTitle = (TextView) findViewById(R.id.task_title_text);
        TextView mTaskDueDate = (TextView) findViewById(R.id.task_due_date_view);
        TextView mTaskNotes = (TextView) findViewById(R.id.task_notes_view);
        TextView mTaskPriority = (TextView) findViewById(R.id.task_priority_view);
        TextView mTaskStatus = (TextView) findViewById(R.id.task_status_view);

        mTaskTitle.setText(mTask.title);
        mTaskDueDate.setText(mTask.dueDate);
        mTaskNotes.setText(mTask.notes);
        mTaskPriority.setText(mTask.priority);
        mTaskStatus.setText(mTask.status);
    }

    @Override
    public void onFinishEditDialog(TodoTask task) {

    }

    private void onClick(View view) {
        /*FragmentManager fm = getSupportFragmentManager();
        TaskEditDialog taskEditDialog = TaskEditDialog.newInstance("Edit Task", task);
        taskEditDialog.show(fm, "edit_task");*/
    }

}
