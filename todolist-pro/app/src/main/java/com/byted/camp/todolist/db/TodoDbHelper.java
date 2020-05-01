package com.byted.camp.todolist.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库
    private static String TAG = "SQLiteOpenHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = TodoContract.NoteDBSchema.TABLE_NAME;
    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG,TodoContract.NoteDBSchema.TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
        db.execSQL(TodoContract.ON_CREATE_SQL);
        }catch(SQLException e){
            Log.d(TAG,"SQLException!\n"+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
        db.execSQL(TodoContract.ON_DELETE_SQL);
        onCreate(db);
        }catch(SQLException e){
            Log.d(TAG,"SQLException!\n"+e.getMessage());
            e.printStackTrace();
        }
    }

}
