package com.example.to_dolist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.to_dolist.Model.Task;
//importing helper class to database
import com.example.to_dolist.Utils.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    //To create an complete table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_TASK_NAME +" TEXT,"
                + Constants.KEY_START + " INTEGER,"
                + Constants.KEY_DEAD + " INTEGER,"
                + Constants.KEY_STATUS + " TEXT,"
                + Constants.KEY_DATE + " LONG);";
        db.execSQL(CREATE_TASK_TABLE);
    }

    //for changing table
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    //crud operation- create - read - update - delete
    public void addTask(Task task){

        //getting helper class
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TASK_NAME,task.getTaskname());
        values.put(Constants.KEY_START,task.getStarttime());
        values.put(Constants.KEY_DEAD,task.getDeadline());
        values.put(Constants.KEY_STATUS,task.getStatus());
        values.put(Constants.KEY_DATE,java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("DBHandler", "addTask: ");
    }

    //reading a row from database
    public Task getTask(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_TASK_NAME,
                        Constants.KEY_START,
                        Constants.KEY_DEAD,
                        Constants.KEY_STATUS,
                        Constants.KEY_DATE},
                Constants.KEY_ID + "=?",
                new String[]{
                        String.valueOf(id)},null,null,null,null );

        if(cursor != null)
            cursor.moveToFirst();

        Task task = new Task();
        assert cursor != null;
        task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        task.setTaskname(cursor.getString(cursor.getColumnIndex(Constants.KEY_TASK_NAME)));
        task.setStarttime((cursor.getInt(cursor.getColumnIndex(Constants.KEY_START))));
        task.setDeadline(cursor.getInt(cursor.getColumnIndex(Constants.KEY_DEAD)));
        task.setStatus(cursor.getString(cursor.getColumnIndex(Constants.KEY_STATUS)));

        //convert Timestamp to something readable
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime());
        task.setDateadded(formattedDate);

        return task;
    }

    //getting all row from database
    public List<Task> getAllTasks(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Task> taskList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_TASK_NAME,
                        Constants.KEY_START,
                        Constants.KEY_DEAD,
                        Constants.KEY_STATUS,
                        Constants.KEY_DATE},
                null,null,null,null,Constants.KEY_DATE + " DESC");

        if(cursor.moveToFirst()){
            do{
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                task.setTaskname(cursor.getString(cursor.getColumnIndex(Constants.KEY_TASK_NAME)));
                task.setStarttime((cursor.getInt(cursor.getColumnIndex(Constants.KEY_START))));
                task.setDeadline(cursor.getInt(cursor.getColumnIndex(Constants.KEY_DEAD)));
                task.setStatus(cursor.getString(cursor.getColumnIndex(Constants.KEY_STATUS)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime());
                task.setDateadded(formattedDate);
                task.setDateadded(formattedDate);

                taskList.add(task);
            }while (cursor.moveToNext());
        }
        return taskList;

    }

    //updating the database
    public int updateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TASK_NAME,task.getTaskname());
        values.put(Constants.KEY_START,task.getStarttime());
        values.put(Constants.KEY_DEAD,task.getDeadline());
        values.put(Constants.KEY_STATUS,task.getStatus());
        values.put(Constants.KEY_DATE,java.lang.System.currentTimeMillis());

        return db.update(Constants.TABLE_NAME, values,Constants.KEY_ID + "=?", new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID+ "=?", new String[]{String.valueOf(id)});

        db.close();
    }

    //getting count of columns in database
    public int getTaskCount(){
        String countQuery ="SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }




}
