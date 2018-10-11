package com.example.chahyunbin.cwapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

public class MemberDatabase {
    public static final String TAG = "BookDatabase";
    private static MemberDatabase database;
    public static String DATABASENAME = "member.db";
    private SQLiteDatabase db;
    public static String TABLENAME = "MEMBER_INFO";
    public static int DATABASEVERSION = 1;
    public static String TABLE_MEMBER_INFO = "MEMBER_INFO";

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



        public boolean open(){
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();

            return true;
        }
        public void close () {
            db.close();
            database = null;
        }

    public Cursor rawQuery(String SQL, Object o) {


        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);

        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }


        public void insertRecord (String name, String phonenumber, int age, int month, int day){
            try {

            db.execSQL("insert into " + TABLENAME + "(NAME, PHONENUMBER, AGE, MONTH, DAY) values ('" + name + "' , '" + phonenumber + "', '" + age + "' , '" + month + " , " + day +"')");

        }catch(Exception ex){
            Log.d(TAG, "exception in insertRecord", ex);
        }
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASENAME, null, DATABASEVERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase _db) {
            String DROP_SQL = "drop table if exists " + TABLE_MEMBER_INFO;
            try {
                _db.execSQL(DROP_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            // create table
            String CREATE_SQL = "create table " + TABLE_MEMBER_INFO + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  NAME TEXT, "
                    + "  PHONENUMBER TEXT, "
                    + "  AGE INTEGER, "
                    + "  MONTH INTEGER, "
                    + "  DAY INTEGER, "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                _db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS person");
            onCreate(db);
        }
    }

}
