package com.example.trandhawa.todoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {

    public static ArrayList<ToDoItem> items;
    ArrayAdapter<ToDoItem> itemsArrAdapter;
    private ListView lvItems;
    private final int REQUEST_CODE_ADD = 20;
    public static SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sqLiteDatabase = getBaseContext().openOrCreateDatabase("ToDoItems.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ToDoItems (title TEXT, deadline TEXT, notes TEXT, status TEXT, priority TEXT, item_id INTEGER PRIMARY KEY);");
        items = new ArrayList<ToDoItem>();
        readItems();
        itemsArrAdapter = new ToDoArrayAdapter(this, items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsArrAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && REQUEST_CODE_ADD == requestCode){
            ToDoItem editedItem = new ToDoItem();
            editedItem.setTitle(data.getStringExtra("Title"));
            String strDeadline = data.getStringExtra("Deadline");
            SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date deadlineDate = null;
            try{
                deadlineDate = dtFormat.parse(strDeadline);
            } catch(Exception e) {}

            editedItem.setDeadline(deadlineDate);
            editedItem.setNotes(data.getStringExtra("Notes"));
            editedItem.setStatus(data.getStringExtra("Status"));
            editedItem.setPriority(data.getStringExtra("Priority"));

            long id = writeNewItemToDB(editedItem);
            editedItem.setItem_id(id);
            items.add(editedItem);
            itemsArrAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAdd(View view) {
        Intent toAddActivity = new Intent(MainActivity.this, AddItemActivity.class);
        startActivityForResult(toAddActivity, REQUEST_CODE_ADD);
    }

    private void readItems(){

        try{

            String getQuery = "SELECT * FROM ToDoItems;";
            Cursor cursor = sqLiteDatabase.rawQuery(getQuery, null);

            if(cursor.moveToFirst()){
                while(cursor.isAfterLast() == false){
                    String title = cursor.getString(0);
                    String deadline = cursor.getString(1);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date newDate = sdf.parse(deadline);
                    String notes = cursor.getString(2);
                    String status = cursor.getString(3);
                    String priority = cursor.getString(4);
                    long item_id = cursor.getLong(cursor.getColumnIndex("item_id"));
                    ToDoItem newItem = new ToDoItem();
                    newItem.setTitle(title);
                    newItem.setDeadline(newDate);
                    newItem.setStatus(status);
                    newItem.setPriority(priority);
                    newItem.setNotes(notes);
                    newItem.setItem_id(item_id);
                    items.add(newItem);
                    cursor.moveToNext();
                }
            }

//            sqLiteDatabase.close();
        } catch(Exception e){
            Log.d ("MainActivity", "Exception while reading items from db: "+e);
        }
    }

    private long writeNewItemToDB(ToDoItem item){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("deadline", sdf.format(item.getDeadline()));
        values.put("notes", item.getNotes());
        values.put("status", item.getStatus());
        values.put("priority", item.getPriority());

        return sqLiteDatabase.insert("ToDoItems", null, values);
    }

    @Override
    public void onFinishEditDialog(String title, String deadline, String notes, String status, String priority, long item_id, int index) {

        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("deadline", deadline);
        values.put("status", status);
        values.put("priority", priority);
        values.put("notes", notes);
        sqLiteDatabase.update("ToDoItems", values, "item_id = " + item_id, null);

        items.get(index).setTitle(title);
        items.get(index).setNotes(notes);
        items.get(index).setStatus(status);
        items.get(index).setPriority(priority);

        Date deadlineDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try{
            deadlineDate = sdf.parse(deadline);
        } catch (Exception e){}
        items.get(index).setDeadline(deadlineDate);
        itemsArrAdapter.notifyDataSetChanged();
    }
}
