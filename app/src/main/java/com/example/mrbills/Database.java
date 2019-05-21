package com.example.mrbills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Database extends SQLiteOpenHelper {
    public static final String table_name=" mylist_data ";
    public static final String Database_name=" mylist.db ";
    public static final String col1 = " ID ";
    public static final String col2 = " ITEM1 ";

    public Database(Context context){
        super(context,Database_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String Create = " CREATE TABLE "+table_name+" ( ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM1 TEXT ) ";
            db.execSQL(Create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(" DROP IF TABLE EXISTS "+table_name);
    }

    public boolean addData(String item1,String item2,String item3){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        item1 = item1 + "\n"+ item2 +"   "+ date +"   "+item3 ;
        contentValues.put(col2,item1);
        long result = db.insert(table_name,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+ table_name,null);
        return data;
    }
}
