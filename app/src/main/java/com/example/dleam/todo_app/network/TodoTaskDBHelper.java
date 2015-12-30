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
    private static final String DATABASE_NAME = "taskDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_TASKS = "tasks";

    // Task Table Columns
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_TITLE = "title";
    private static final String KEY_TASK_PRIORITY = "priority";
    private static final String KEY_TASK_DUE_DATE = "due_date";
    private static final String KEY_TASK_NOTES = "notes";
    private static final String KEY_TASK_STATUS = "status";

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
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                    KEY_TASK_ID + " INTEGER PRIMARY KEY," +
                    KEY_TASK_TITLE + " TEXT," +
                    KEY_TASK_PRIORITY + " TEXT," +
                    KEY_TASK_DUE_DATE + " TEXT," +
                    KEY_TASK_NOTES + " TEXT," +
                    KEY_TASK_STATUS + " TEXT" +
                ")";

        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    // Insert a task
    public void addTask(TodoTask task) {
        SQLiteDatabase db = getWritableDatabase();

        // Doing the insert in a transaction helps with performance and ensures consistency of the db
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_ID, task.position);
            values.put(KEY_TASK_TITLE, task.title);
            values.put(KEY_TASK_PRIORITY, task.priority);
            values.put(KEY_TASK_DUE_DATE, task.dueDate);
            values.put(KEY_TASK_NOTES, task.notes);
            values.put(KEY_TASK_STATUS, task.status);

            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Get all tasks
    public ArrayList<TodoTask> getAllTasks() {
        ArrayList<TodoTask> tasks = new ArrayList<>();
        String TASKS_SELECT_QUERY = String.format( "SELECT * FROM %s", TABLE_TASKS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try{
            if(cursor.moveToFirst()) {
                do {
                    TodoTask task = new TodoTask();
                    task.position = cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID));
                    task.title = cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE));
                    task.priority = cursor.getString(cursor.getColumnIndex(KEY_TASK_PRIORITY));
                    task.dueDate = cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE_DATE));
                    task.notes = cursor.getString(cursor.getColumnIndex(KEY_TASK_NOTES));
                    task.status = cursor.getString(cursor.getColumnIndex(KEY_TASK_STATUS));

                    tasks.add(task);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }

    // Update an existing task
    public int updateTask(TodoTask task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE, task.title);
        values.put(KEY_TASK_PRIORITY, task.priority);
        values.put(KEY_TASK_DUE_DATE, task.dueDate);
        values.put(KEY_TASK_NOTES, task.notes);
        values.put(KEY_TASK_STATUS, task.status);

        return db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(task.position) });
    }

    // Delete an existing task
    public void deleteTask(TodoTask task) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(TABLE_TASKS, KEY_TASK_ID + " = ?",
                    new String[] { String.valueOf(task.position) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
