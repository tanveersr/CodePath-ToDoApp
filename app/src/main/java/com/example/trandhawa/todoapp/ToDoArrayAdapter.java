package com.example.trandhawa.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by trandhawa on 8/4/16.
 */
public class ToDoArrayAdapter extends ArrayAdapter<ToDoItem> implements EditItemDialogFragment.EditItemDialogListener{

    public ToDoArrayAdapter(Context context, ArrayList<ToDoItem> toDoList) {
        super(context, 0, toDoList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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

        Button deleteItemView = (Button) convertView.findViewById(R.id.deleteItemButton);
        deleteItemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.sqLiteDatabase.delete("ToDoItems", "item_id = " + MainActivity.items.get(position).getItem_id() , null);
                MainActivity.items.remove(position);
                notifyDataSetChanged();
            }
        });

        Button editItemButton = (Button) convertView.findViewById(R.id.editItemButton);
        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragMan = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                EditItemDialogFragment edtItemFrag = EditItemDialogFragment.newInstance(MainActivity.items.get(position), position);
                edtItemFrag.show(fragMan, "fragment_edit_item");
            }
        });

        return convertView;
    }

    @Override
    public void onFinishEditDialog(String title, String deadline, String notes, String status, String priority, long item_id, int index) {

        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("deadline", deadline);
        values.put("status", status);
        values.put("priority", priority);
        values.put("notes", notes);
        MainActivity.sqLiteDatabase.update("ToDoItems", values, "item_id = " + item_id, null);

        MainActivity.items.get(index).setTitle(title);
        MainActivity.items.get(index).setNotes(notes);
        MainActivity.items.get(index).setStatus(status);
        MainActivity.items.get(index).setPriority(priority);

        Date deadlineDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try{
            deadlineDate = sdf.parse(deadline);
        } catch (Exception e){}
        MainActivity.items.get(index).setDeadline(deadlineDate);
        notifyDataSetChanged();
    }
}
