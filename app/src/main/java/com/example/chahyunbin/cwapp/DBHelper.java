package com.example.chahyunbin.cwapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


class MemberDatabase {
    public static final String TAG = "BookDatabase";
    private static MemberDatabase database;
    public static String DATABASENAME = "member.db";
    private SQLiteDatabase db;
    public static String TABLENAME = "MEMBER_INFO";
    public static int DATABASEVERSION = 1;

    private DBHelper dbHelper;
    private Context context;


    private MemberDatabase(Context context) {
        this.context = context;
    }

    public static MemberDatabase getInstance(Context context) {
        if (database == null) {
            database = new MemberDatabase(context);
        }
        return database;
    }

<<<<<<< Updated upstream
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE person ( id_INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phonenumber TEXT, age INTEGER, birthmonth INTEGER, birthday INTEGER);");
=======
    public boolean open() {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }
    public void close(){
        db.close();
        database = null;
    }
>>>>>>> Stashed changes

    public void insertRecord(String name, String author, String contents) {
        try{
            int i;
        }
            db.execSQL("insert into "+ TABLENAME +"(NAME, AUTHOR, CONTENTS) values ('" + name + "' , '" + author + "', '" + contents +"')");

        }catch(Exception ex){
            Log.d(TAG,"exception in insertRecord",ex);
        }
    }


    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASENAME, null, DATABASEVERSION);
        }

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE person ( id_integer PRIMARY KEY AUTOINCREMENT, name TEXT, phonnumber TEXT, age INTEGER, birthmonth INTEGER, birthday INTEGER)");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS person");
            onCreate(db);
        }
    }
}
