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
    private ArrayList<ListItem> mItemList;
    private CustomAdapter mCustomAdapter;
    private ListView mListView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle extras = data.getExtras();
            ListItem item = (ListItem) extras.getSerializable("item");
            mItemList.set(item.position, item);
            mCustomAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        mItemList = new ArrayList<>();
        mCustomAdapter = new CustomAdapter(this, mItemList);
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                ListItem item = (ListItem) mCustomAdapter.getItem(position);
                bundle.putSerializable("item", item);
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = (ListItem) mCustomAdapter.getItem(position);
                mItemList.remove(item);
                mCustomAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public void addTodo(View view) {
        TextView content = (TextView) findViewById(R.id.editText);
        ListItem item = new ListItem(content.getText().toString());
        // Set the position of the item for editing
        item.position = mListView.getCount();
        mItemList.add(item);
        mCustomAdapter.notifyDataSetChanged();
        content.setText(null);
    }
}
