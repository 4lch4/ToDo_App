package com.example.dleam.todo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.adapters.TodoTaskAdapter;
import com.example.dleam.todo_app.fragments.TaskEditDialog;
import com.example.dleam.todo_app.models.TodoTask;
import com.example.dleam.todo_app.network.TodoTaskDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskEditDialog.TaskEditDialogListener {
    private TodoTaskDBHelper taskDB;
    private ArrayList<TodoTask> mTaskList;
    private TodoTaskAdapter mTodoTaskAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDB = TodoTaskDBHelper.getInstance(this);

        mListView = (ListView) findViewById(R.id.listView);
        mTaskList = taskDB.getAllTasks();
        mTodoTaskAdapter = new TodoTaskAdapter(this, mTaskList);
        mListView.setAdapter(mTodoTaskAdapter);

        buildListeners();
    }

    private void buildListeners() {
        Button addButton = (Button) findViewById(R.id.add_button);

        // Button listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                TaskEditDialog taskEditDialog = TaskEditDialog.newInstance("Add Task");
                taskEditDialog.show(fm, "add_task");
            }
        });

        // ListView regular click - Edit Task
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoTask task = (TodoTask) mTodoTaskAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, TaskViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                intent.putExtras(bundle);
                startActivity(intent);
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
        TodoTaskDBHelper taskDB = TodoTaskDBHelper.getInstance(this);

        if(mTaskList.contains(task)) {
            mTaskList.set(task.position, task);
            taskDB.updateTask(task);
        } else {
            mTaskList.add(task);
            taskDB.addTask(task);
        }

        mTodoTaskAdapter.notifyDataSetChanged();
    }
}
