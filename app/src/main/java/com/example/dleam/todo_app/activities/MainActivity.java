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
import com.example.dleam.todo_app.models.TodoItem;
import com.example.dleam.todo_app.adapters.TodoItemAdapter;
import com.example.dleam.todo_app.network.TodoItemDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditDialogFragment.EditDialogListener {
    private ArrayList<TodoItem> mItemList;
    private TodoItemAdapter mTodoItemAdapter;
    private ListView mListView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TodoItemDBHelper itemDB = TodoItemDBHelper.getInstance(this);

        mListView = (ListView) findViewById(R.id.listView);
        mItemList = itemDB.getAllItems();
        mTodoItemAdapter = new TodoItemAdapter(this, mItemList);
        mListView.setAdapter(mTodoItemAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) mTodoItemAdapter.getItem(position);
                FragmentManager fm = getSupportFragmentManager();
                EditDialogFragment editDialogFragment = EditDialogFragment.newInstance("Edit Item", item);
                editDialogFragment.show(fm, "fragment_edit_item");
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) mTodoItemAdapter.getItem(position);
                mItemList.remove(item);
                itemDB.deleteItem(item);
                mTodoItemAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onFinishEditDialog(TodoItem item) {

        TodoItemDBHelper itemDB = TodoItemDBHelper.getInstance(this);
        TextView content = (TextView) findViewById(R.id.editText);

        itemDB.updateItem(item);
        mItemList.set(item.position, item);
        mTodoItemAdapter.notifyDataSetChanged();

        content.setText(null);
    }

    public void addTodo(View view) {
        TodoItemDBHelper itemDB = TodoItemDBHelper.getInstance(this);
        TextView content = (TextView) findViewById(R.id.editText);
        TodoItem item = new TodoItem();

        item.content = content.getText().toString();
        item.position = mListView.getCount();

        itemDB.addItem(item);
        mItemList.add(item);
        mTodoItemAdapter.notifyDataSetChanged();

        content.setText(null);
    }
}
