package com.example.chahyunbin.cwapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseTable {
    private static final int VERSION = 1;
    private static SQLiteOpenHelper helper;


    protected BaseTable(Context context) {
        helper = new DatabaseHelper(context, context.getPackageName().replaceAll("\\.", "_"), null, VERSION);
    }

    protected static SQLiteDatabase db() {
        return helper.getWritableDatabase();
    }
    public void close() {
        db().close();
    }

    protected int insert() {
        SQLiteDatabase db = helper.getWritableDatabase();
        // 마지막에 넣은 rowid 를 리턴한다
        String sql = "SELECT last_insert_rowid();";
        Cursor c = db.rawQuery(sql, null);

        int result = 0;
        c.moveToFirst();
        if(c.getCount() > 0)
            result = c.getInt(0);
        c.close();

        Log.i("BaseTable : insert", "lowid : " + result + " inserted");
        return result;
    }

}
