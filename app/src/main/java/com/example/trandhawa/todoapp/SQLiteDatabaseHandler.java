package com.example.trandhawa.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ParseException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by trandhawa on 8/2/16.
 */
public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "toDoListDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "toDoItems";

    // Post Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TITLE = "title";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_DEADLINE = "deadline";

    private static SQLiteDatabaseHandler sInstance;

    public static synchronized SQLiteDatabaseHandler getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteDatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    private SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," +
                KEY_ITEM_TITLE + " TEXT " +
                KEY_STATUS + " TEXT " +
                KEY_PRIORITY + " TEXT " +
                KEY_NOTES + " TEXT " +
                KEY_DEADLINE + " DATE " +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXITS " + TABLE_ITEMS);
    }



    public long addItem(ToDoItem item) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long item_id = -1;

        try{

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_ID, item.getItem_id());
            values.put(KEY_ITEM_TITLE, item.getTitle());
            values.put(KEY_STATUS, item.getStatus());
            values.put(KEY_PRIORITY, item.getPriority());
            values.put(KEY_NOTES, item.getNotes());

            String deadlineDate = new SimpleDateFormat("MM/dd/yyyy").format(item.getDeadline());
            values.put(KEY_DEADLINE, deadlineDate);

            int rows = db.update(TABLE_ITEMS, values, KEY_ITEM_ID + " = ? " , new String[]{item.getItem_id()+""});

            if(rows!=1) {
                item_id = db.insertOrThrow(TABLE_ITEMS, null, values);
                db.setTransactionSuccessful();
            }

        } catch(Exception e) {
            Log.d("SQLiteDatabaseHandler" , "Exception in adding item to database: " + e);
        } finally {
            db.endTransaction();
        }

        return item_id;
    }


    public ArrayList<ToDoItem> getAllItems(){

        ArrayList<ToDoItem> allItems = new ArrayList<ToDoItem>();

        String query = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        try{
            if(cursor.moveToFirst()){
                do{
                    ToDoItem tempItem = new ToDoItem();
                    tempItem.setItem_id(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID)));
                    String strDate = cursor.getString(cursor.getColumnIndex(KEY_DEADLINE));
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date date = format.parse(strDate);
                        tempItem.setDeadline(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tempItem.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));
                    tempItem.setPriority(cursor.getString(cursor.getColumnIndex(KEY_PRIORITY)));
                    tempItem.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                    tempItem.setTitle(cursor.getString(cursor.getColumnIndex(KEY_ITEM_TITLE)));

                    allItems.add(tempItem) ;

                } while (cursor.moveToNext());
            }
        } catch(Exception e){
            Log.d("SQLiteDatabaseHandler", "Exception while getting all items: " + e);

        } finally {

        }

        Log.d("SQLiteDatabaseHandler", "No of items : " + allItems.size());
        return allItems;
    }

    public void deleteAllItems(){

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try{

            db.delete(TABLE_ITEMS, null, null);
            db.setTransactionSuccessful();
        } catch(Exception e){
            Log.d("SQLiteDatabaseHandler", "Error while trying to delete all items.");
        } finally{
            db.endTransaction();
        }
    }
}
