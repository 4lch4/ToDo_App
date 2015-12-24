package com.example.dleam.todo_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 42;
    private ArrayList<TodoItem> mItemList;
    private CustomAdapter mCustomAdapter;
    private ListView mListView;

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("STOP", "Stopping.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItemDatabase itemDB = ItemDatabase.getInstance(this);

        mListView = (ListView) findViewById(R.id.listView);
        mItemList = new ArrayList<>();
        mCustomAdapter = new CustomAdapter(this, mItemList);
        mListView.setAdapter(mCustomAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                TodoItem item = (TodoItem) mCustomAdapter.getItem(position);
                bundle.putSerializable("item", item);
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) mCustomAdapter.getItem(position);
                mItemList.remove(item);
                mCustomAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void buildListView() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle extras = data.getExtras();
            TodoItem item = (TodoItem) extras.getSerializable("item");
            mItemList.set(item.position, item);
            mCustomAdapter.notifyDataSetChanged();
        }
    }

    public void addTodo(View view) {
        TextView content = (TextView) findViewById(R.id.editText);
        TodoItem item = new TodoItem();

        item.content = content.getText().toString();
        item.position = mListView.getCount();

        mItemList.add(item);
        mCustomAdapter.notifyDataSetChanged();

        content.setText(null);
    }
}
