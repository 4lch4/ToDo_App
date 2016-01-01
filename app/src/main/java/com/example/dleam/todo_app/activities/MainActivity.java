package com.example.dleam.todo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.adapters.TodoTaskAdapter;
import com.example.dleam.todo_app.fragments.TaskEditDialog;
import com.example.dleam.todo_app.models.TodoTask;
import com.example.dleam.todo_app.network.TodoTaskDBHelper;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements TaskEditDialog.TaskEditDialogListener {
    private TodoTaskDBHelper taskDB;
    private ArrayList<TodoTask> mTaskList;
    private TodoTaskAdapter mTodoTaskAdapter;
    private ListView mListView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar();

        taskDB = TodoTaskDBHelper.getInstance(this);

        mListView = (ListView) findViewById(R.id.listView);
        mTaskList = taskDB.getAllTasks();
        mTodoTaskAdapter = new TodoTaskAdapter(this, mTaskList);
        mListView.setAdapter(mTodoTaskAdapter);

        buildListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Search Button selected
        if (id == R.id.menu_add_task) {
            FragmentManager fm = getSupportFragmentManager();
            TaskEditDialog taskEditDialog = TaskEditDialog.newInstance("Add Task");
            taskEditDialog.show(fm, "add_task");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            TodoTask task = (TodoTask) data.getExtras().getSerializable("task");
            mTaskList.set(task.position, task);
            mTodoTaskAdapter.notifyDataSetChanged();
        }
    }

    private void buildListeners() {
        // ListView regular click - Edit Task
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoTask task = (TodoTask) mTodoTaskAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, TaskViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        // ListView long click - Delete Task
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoTask task = (TodoTask) mTodoTaskAdapter.getItem(position);
                mTaskList.remove(task);
                taskDB.deleteTask(task);
                mTodoTaskAdapter.notifyDataSetChanged();
                Snackbar.make(findViewById(R.id.relative_layout), "Task Deleted", Snackbar.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public void onFinishEditDialog(TodoTask task) {
        if(task.title.length() > 0) {
            TodoTaskDBHelper taskDB = TodoTaskDBHelper.getInstance(this);

            mTaskList.add(task);
            taskDB.addTask(task);

            mTodoTaskAdapter.notifyDataSetChanged();
        }
    }
}
