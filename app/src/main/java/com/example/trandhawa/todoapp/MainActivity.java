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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {

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
//                writeItems();
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
                showEditDialog(items.get(position), position);

            }
        });
    }

    private void showEditDialog(ToDoItem i, int index){
        FragmentManager fragMan = getSupportFragmentManager();
        EditItemDialogFragment edtItemFrag = EditItemDialogFragment.newInstance(i.getTitle(),i.getDeadline(),i.getStatus(),i.getPriority(),i.getNotes(),index);
        edtItemFrag.show(fragMan, "fragment_edit_item");
//        edtItemFrag.setTargetFragment(edtItemFrag, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && REQUEST_CODE_ADD == requestCode){

//            ToDoItem editedItem = data.getExtras().getString("ItemTitle");
            ToDoItem editedItem = new ToDoItem();
            editedItem.setTitle(data.getStringExtra("Title"));
            String strDeadline = data.getStringExtra("Deadline");
            SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date deadlineDate = null;
            try{
                deadlineDate = dtFormat.parse(strDeadline);
            } catch(Exception e) {

            }

            editedItem.setDeadline(deadlineDate);
            editedItem.setNotes(data.getStringExtra("Notes"));
            editedItem.setStatus(data.getStringExtra("Status"));
            editedItem.setPriority(data.getStringExtra("Priority"));

            items.add(editedItem);
            itemsArrAdapter.notifyDataSetChanged();
//            writeItems();
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
//        File filesDir = getFilesDir();
//        File file = new File(filesDir, "ToDo.txt");
        try{
//            items = new ArrayList<ToDoItem>(FileUtils.readLines(file));
            items = SQLiteDatabaseHandler.getInstance(this).getAllItems();
            Log.d("MainActivity", "No of items read: " + items.size());
        } catch(Exception e){
            Log.d("MainActivity", "Exception while reading items from db: "+e);
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "ToDo.txt");
        try{
            FileUtils.writeLines(file, items);
        } catch(IOException e){
            Log.d("MainActivity", "Exception while writing items to db: "+e);
        }
    }

    @Override
    public void onFinishEditDialog(String title, String deadline, String notes, String status, String priority, int index) {

        items.get(index).setTitle(title);
        items.get(index).setNotes(notes);
        items.get(index).setStatus(status);
        items.get(index).setPriority(priority);
        SimpleDateFormat simpDF = new SimpleDateFormat("MM/dd/yyyy");
        Date deadlineModified = null;
        try{
            deadlineModified = simpDF.parse(deadline);
        } catch(Exception e) {
        }

        items.get(index).setDeadline(deadlineModified);
        itemsArrAdapter.notifyDataSetChanged();
//        writeItems();
    }
}
