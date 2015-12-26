package com.example.dleam.todo_app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditDialog.EditDialogListener{
    private ArrayList<TodoItem> mItemList;
    private CustomAdapter mCustomAdapter;
    private ListView mListView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ItemDatabase itemDB = ItemDatabase.getInstance(this);

        mListView = (ListView) findViewById(R.id.listView);
        mItemList = itemDB.getAllItems();
        mCustomAdapter = new CustomAdapter(this, mItemList);
        mListView.setAdapter(mCustomAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) mCustomAdapter.getItem(position);
                FragmentManager fm = getSupportFragmentManager();
                EditDialog editDialog = EditDialog.newInstance("Edit Item", item);
                editDialog.show(fm, "fragment_edit_item");
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) mCustomAdapter.getItem(position);
                mItemList.remove(item);
                itemDB.deleteItem(item);
                mCustomAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onFinishEditDialog(TodoItem item) {

        ItemDatabase itemDB = ItemDatabase.getInstance(this);
        TextView content = (TextView) findViewById(R.id.editText);

        itemDB.updateItem(item);
        mItemList.set(item.position, item);
        mCustomAdapter.notifyDataSetChanged();

        content.setText(null);
    }

    public void addTodo(View view) {
        ItemDatabase itemDB = ItemDatabase.getInstance(this);
        TextView content = (TextView) findViewById(R.id.editText);
        TodoItem item = new TodoItem();

        item.content = content.getText().toString();
        item.position = mListView.getCount();

        itemDB.addItem(item);
        mItemList.add(item);
        mCustomAdapter.notifyDataSetChanged();

        content.setText(null);
    }
}
