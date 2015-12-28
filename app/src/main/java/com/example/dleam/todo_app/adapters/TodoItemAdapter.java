package com.example.dleam.todo_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.models.TodoItem;

import java.util.ArrayList;

/**
 * Created by dleam on 12/22/2015.
 */
public class TodoItemAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TodoItem> mItemList;
    private static LayoutInflater sInflater = null;

    public TodoItemAdapter(Context context, ArrayList<TodoItem> itemList) {
        mContext = context;
        mItemList = itemList;
        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return mItemList.size(); }

    @Override
    public Object getItem(int position) { return mItemList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) { view = sInflater.inflate(R.layout.item_row, null); }

        TextView content = (TextView) view.findViewById(R.id.contentView);

        content.setText(mItemList.get(position).content);

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }
}
