package com.example.dleam.todo_app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dleam.todo_app.fragments.EditDialogFragment;
import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.models.TodoTask;
import com.example.dleam.todo_app.adapters.TodoTaskAdapter;
import com.example.dleam.todo_app.network.TodoTaskDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditDialogFragment.EditDialogListener {
    private ArrayList<TodoTask> mItemList;
    private TodoTaskAdapter mTodoTaskAdapter;
    private ListView mListView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TodoTaskDBHelper itemDB = TodoTaskDBHelper.getInstance(this);

        mListView = (ListView) findViewById(R.id.listView);
        mItemList = itemDB.getAllItems();
        mTodoTaskAdapter = new TodoTaskAdapter(this, mItemList);
        mListView.setAdapter(mTodoTaskAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoTask item = (TodoTask) mTodoTaskAdapter.getItem(position);
                FragmentManager fm = getSupportFragmentManager();
                EditDialogFragment editDialogFragment = EditDialogFragment.newInstance("Edit Item", item);
                editDialogFragment.show(fm, "fragment_edit_task");
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoTask item = (TodoTask) mTodoTaskAdapter.getItem(position);
                mItemList.remove(item);
                itemDB.deleteItem(item);
                mTodoTaskAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onFinishEditDialog(TodoTask item) {

        TodoTaskDBHelper itemDB = TodoTaskDBHelper.getInstance(this);
        TextView content = (TextView) findViewById(R.id.editText);

        itemDB.updateItem(item);
        mItemList.set(item.position, item);
        mTodoTaskAdapter.notifyDataSetChanged();

        content.setText(null);
    }

    public void addTodo(View view) {
        TodoTaskDBHelper itemDB = TodoTaskDBHelper.getInstance(this);
        TextView content = (TextView) findViewById(R.id.editText);
        TodoTask item = new TodoTask();

        item.content = content.getText().toString();
        item.position = mListView.getCount();

        itemDB.addItem(item);
        mItemList.add(item);
        mTodoTaskAdapter.notifyDataSetChanged();

        content.setText(null);
    }
}
