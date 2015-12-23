package com.example.dleam.todo_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dleam on 12/22/2015.
 */
public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> itemList;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context contextIn, ArrayList<ListItem> itemListIn) {
        context = contextIn;
        itemList = itemListIn;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return itemList.size(); }

    @Override
    public Object getItem(int position) { return itemList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) { view = inflater.inflate(R.layout.item_row, null); }

        TextView content = (TextView) view.findViewById(R.id.contentView);

        content.setText(itemList.get(position).getContent());

        return view;
    }
}
