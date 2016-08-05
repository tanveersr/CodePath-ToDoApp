package com.example.trandhawa.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by trandhawa on 8/4/16.
 */
public class ToDoArrayAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoArrayAdapter(Context context, ArrayList<ToDoItem> toDoList) {
        super(context, 0, toDoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ToDoItem todoitem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_list_layout, parent, false);
        }

        TextView tvTitleMain = (TextView) convertView.findViewById(R.id.tvTitleMain);
        TextView tvStatusMain = (TextView) convertView.findViewById(R.id.tvStatusMain);
        TextView tvPriorityMain = (TextView) convertView.findViewById(R.id.tvPriorityMain);

        tvTitleMain.setText(todoitem.getTitle());
        tvStatusMain.setText(todoitem.getStatus());
        tvPriorityMain.setText(todoitem.getPriority());

        return convertView;
    }


}
