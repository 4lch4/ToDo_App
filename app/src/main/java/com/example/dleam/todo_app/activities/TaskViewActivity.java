package com.example.dleam.todo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.fragments.TaskEditDialog;
import com.example.dleam.todo_app.models.TodoTask;
import com.example.dleam.todo_app.network.TodoTaskDBHelper;

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
        TodoTaskDBHelper taskDB = TodoTaskDBHelper.getInstance(this);
        taskDB.updateTask(task);

        mTask = task;
        setTaskValues();
    }

    public void onClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        TaskEditDialog taskEditDialog = TaskEditDialog.newInstance("Edit Task", mTask);
        taskEditDialog.show(fm, "edit_task");
    }

    public void saveClick(View view) {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", mTask);
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
