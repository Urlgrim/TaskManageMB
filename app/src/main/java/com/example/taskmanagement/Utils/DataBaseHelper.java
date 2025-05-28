package com.example.taskmanagement.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taskmanagement.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TASK_DATABASE";
    private static final String TABLE_NAME = "TASK_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";
    private static  final int DATABASE_VERSION = 1;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(TaskModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, model.getTask());
        contentValues.put(COL_3, 0);
        db.insert(TABLE_NAME,null,contentValues);
    }

    public void updateTask(int id, String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, task);

        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, status);
        db.update(TABLE_NAME, contentValues,"ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<TaskModel> getAllTasks(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<TaskModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        TaskModel taskModel = new TaskModel();
                        taskModel.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        taskModel.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        taskModel.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(taskModel);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}
