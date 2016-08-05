package com.example.trandhawa.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.*;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<ToDoItem> items;
    ArrayAdapter<ToDoItem> itemsArrAdapter;
    private EditText etNewItem;
    private ListView lvItems;
    private final int REQUEST_CODE_ADD = 20;
    private final int REQUEST_CODE_EDIT = 30;
    private static int editIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        items = new ArrayList<ToDoItem>();
        readItems();
//        itemsArrAdapter = new ArrayAdapter<ToDoItem>(this,android.R.layout.simple_list_item_1,items);
        itemsArrAdapter = new ToDoArrayAdapter(this, items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsArrAdapter);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsArrAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent editScrnIntent = new Intent(MainActivity.this, EditItemActivity.class);
//                editIndex = position;
//                editScrnIntent.putExtra("ItemTitle", items.get(position).toString());
//                startActivityForResult(editScrnIntent, REQUEST_CODE_EDIT);
                showEditDialog(items.get(position));

            }
        });
    }

    private void showEditDialog(ToDoItem i){
        FragmentManager fragMan = getSupportFragmentManager();
        EditItemDialogFragment edtItemFrag = EditItemDialogFragment.newInstance(i.getTitle(),i.getDeadline(),i.getStatus(),i.getPriority(),i.getNotes());
        edtItemFrag.show(fragMan, "fragment_edit_item");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && REQUEST_CODE_ADD == requestCode){

//            ToDoItem editedItem = data.getExtras().getString("ItemTitle");
            ToDoItem editedItem = new ToDoItem();
            editedItem.setTitle(data.getStringExtra("Title"));
            String strDeadline = data.getStringExtra("Deadline");
            SimpleDateFormat dtFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
            Date deadlineDate = dtFormat.parse(strDeadline, new ParsePosition(0));
            editedItem.setDeadline(deadlineDate);
            editedItem.setNotes(data.getStringExtra("Notes"));
            editedItem.setStatus(data.getStringExtra("Status"));
            editedItem.setPriority(data.getStringExtra("Priority"));

            items.add(editedItem);
            Log.d("RETURNED_DATA", "*********************EDITED_ITEM: "+items.size());
            Log.d("RETURNED_DATA", "*********************EDITED_INDEX: "+editIndex);
            itemsArrAdapter.notifyDataSetChanged();
            writeItems();
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
//        String newItem;
//        etNewItem = (EditText) findViewById(R.id.etEditText);
//        newItem = etNewItem.getText().toString();
////        items.add(newItem);
//        etNewItem.setText("");
//        etNewItem.requestFocus();
//        writeItems();

        Intent toAddActivity = new Intent(MainActivity.this, AddItemActivity.class);
        startActivityForResult(toAddActivity, REQUEST_CODE_ADD);
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "ToDo.txt");
        try{
//            items = new ArrayList<ToDoItem>(FileUtils.readLines(file));
            items = SQLiteDatabaseHandler.getInstance(this).getAllItems();
        } catch(Exception e){

        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "ToDo.txt");
        try{
            FileUtils.writeLines(file, items);
        } catch(IOException e){

        }
    }
}
