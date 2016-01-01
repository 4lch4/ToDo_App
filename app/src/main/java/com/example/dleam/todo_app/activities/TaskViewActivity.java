package com.example.dleam.todo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.fragments.TaskEditDialog;
import com.example.dleam.todo_app.models.TodoTask;
import com.example.dleam.todo_app.network.TodoTaskDBHelper;

public class TaskViewActivity extends BaseActivity implements TaskEditDialog.TaskEditDialogListener {
    private TodoTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        Bundle extras = getIntent().getExtras();

        activateToolbar();

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTaskValues((TodoTask) extras.getSerializable("task"));
    }

    private void setTaskValues(TodoTask task) {
        mTask = task;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            // Edit Item selected
            case R.id.menu_edit_task:
                FragmentManager fm = getSupportFragmentManager();
                TaskEditDialog taskEditDialog = TaskEditDialog.newInstance("Edit Task", mTask);
                taskEditDialog.show(fm, "edit_task");
                break;

            // Save Item selected
            case R.id.menu_save_task:
                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", mTask);
                returnIntent.putExtras(bundle);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditDialog(TodoTask task) {
        TodoTaskDBHelper taskDB = TodoTaskDBHelper.getInstance(this);
        taskDB.updateTask(task);

        setTaskValues(task);
    }
}
