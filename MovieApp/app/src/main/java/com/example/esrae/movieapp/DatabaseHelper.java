package com.example.esrae.movieapp;

/**
 * Created by esrae on 11/3/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FavoriteMovies.db";
    public static final String TABLE_NAME = "favorite_movie";
    public static final String COL_1 = "id";
    public static final String COL_2 = "name";
    public static final String COL_3 = "year";
    public static final String COL_4 = "rate";
    public static final String COL_5 = "describe";
    public static final String COL_6 = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (" +  COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT,"
                + COL_5 + " TEXT," + COL_6 + " TEXT)");
    }

    public boolean insertData(String id, String name, String date, String rate , String ov, String post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, rate);
        contentValues.put(COL_5, ov);
        contentValues.put(COL_6, post);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getSpData(String id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_NAME +" WHERE ID=?", new String[] {id });
        return res;
    }

/*
    public boolean updateData(String id,String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,url);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }
*/

}