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
    ArrayList<ListItem> itemList;
    CustomAdapter customAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.listView);
        itemList = new ArrayList<>();
        customAdapter = new CustomAdapter(this, itemList);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = (ListItem) customAdapter.getItem(position);
                Log.i("OUTPUT", item.getContent());
            }
        });
    }

    public void addTodo(View view) {
        TextView content = (TextView) findViewById(R.id.editText);
        ListItem item = new ListItem(content.getText().toString());
        itemList.add(item);
        customAdapter.notifyDataSetChanged();
    }
}
