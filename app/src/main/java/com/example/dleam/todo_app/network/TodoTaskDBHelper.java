package com.example.dleam.todo_app.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dleam.todo_app.models.TodoTask;

import java.util.ArrayList;

public class TodoTaskDBHelper extends SQLiteOpenHelper {

    //<editor-fold desc="Variables">
    // DB Info
    private static final String DATABASE_NAME = "itemDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_ITEMS = "items";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT= "text";

    // Misc Variables
    private static TodoTaskDBHelper sInstance;
    //</editor-fold>

    public static synchronized TodoTaskDBHelper getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new TodoTaskDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TodoTaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                    KEY_ITEM_ID + " INTEGER PRIMARY KEY," +
                    KEY_ITEM_TEXT + " TEXT" +
                ")";

        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    // Insert an item
    public void addItem(TodoTask item) {
        SQLiteDatabase db = getWritableDatabase();

        // Doing the insert in a transaction helps with performance and ensures consistency of the db
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_ID, item.position);
            values.put(KEY_ITEM_TEXT, item.content);

            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Get all items
    public ArrayList<TodoTask> getAllItems() {
        ArrayList<TodoTask> items = new ArrayList<>();
        String ITEMS_SELECT_QUERY = String.format( "SELECT * FROM %s", TABLE_ITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try{
            if(cursor.moveToFirst()) {
                do {
                    TodoTask item = new TodoTask();
                    item.position = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID));
                    item.content = cursor.getString(cursor.getColumnIndex(KEY_ITEM_TEXT));

                    items.add(item);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    // Update an existing item
    public int updateItem(TodoTask item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_TEXT, item.content);

        return db.update(TABLE_ITEMS, values, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(item.position) });
    }

    public void deleteItem(TodoTask item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(TABLE_ITEMS, KEY_ITEM_ID + " = ?",
                    new String[] { String.valueOf(item.position) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}