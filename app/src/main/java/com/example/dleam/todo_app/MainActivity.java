package com.example.dleam.todo_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ListItem> mItemList;
    private CustomAdapter mCustomAdapter;
    private ListView mListView;

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
                ListItem item = (ListItem) mCustomAdapter.getItem(position);
                Log.i("OUTPUT", item.getContent());
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
        mItemList.add(item);
        mCustomAdapter.notifyDataSetChanged();
        content.setText(null);
    }
}
