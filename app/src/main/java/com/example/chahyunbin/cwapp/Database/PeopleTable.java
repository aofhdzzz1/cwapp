package com.example.chahyunbin.cwapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.chahyunbin.cwapp.model.Person;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.BaseColumns._ID;

public class PeopleTable extends BaseTable {
    protected static PeopleTable instance;
    final String TAG = "BookDatabase";

    // 테이블
    public final static String TABLE_NAME = "people";

    // 필드
    public static final String ID = _ID;
    public static final String NAME = "name";
    public static final String PHONENUMBER = "phonenumber";
    public static final String AGE ="age";
    public static final String DAY = "day";
    public static final String MONTH = "month";

    private static final String ORDER_BY_DEFAULT = _ID + " asc";
    private static final String ORDER_BY_DEFAULT_DESC = _ID + " desc";

    private static final String[] COLUMNS = { _ID,NAME, PHONENUMBER, AGE, DAY, MONTH};

    private static final String WHERE_BY_ID = _ID + "=?";

    public static final String createSql = "CREATE TABLE if not exists " + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, " + NAME + " text," + PHONENUMBER+ " text, " + AGE+" text, "+ MONTH
            + " text," + DAY + " text);";

    public synchronized static PeopleTable instance(Context context) {
        if (instance == null) {
            synchronized (PeopleTable.class) {
                if (instance == null)
                    instance = new PeopleTable(context);
            }
        }
        return instance;
    }

    private PeopleTable(Context context){
        super(context);
    }

    public int insert(String name, String phonenumber, String age, String month, String day) {
        Log.d(TAG, "3");

        ContentValues values = new ContentValues();

        values.put(NAME, name.trim());
        values.put(PHONENUMBER, phonenumber.trim());
        values.put(AGE, age.trim());
        values.put(MONTH, month.trim());
        values.put(DAY, day.trim());
        db().insertOrThrow(TABLE_NAME, null, values);
        Log.d(TAG, "4");
        return super.insert();
    }
    private Person makeBean(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(ID));
        String name = cursor.getString(cursor.getColumnIndex(NAME));
        String phonenumber = cursor.getString(cursor.getColumnIndex(PHONENUMBER));
        String age = cursor.getString(cursor.getColumnIndex(AGE));
        String day = cursor.getString(cursor.getColumnIndex(DAY));
        String month = cursor.getString(cursor.getColumnIndex(MONTH));
        return new Person(id, name, phonenumber,age, day, month);
    }

    public ArrayList<Person> loadByDate(boolean isDesc) {
        ArrayList<Person> result = new ArrayList<>();

        String order = ORDER_BY_DEFAULT;
        if(isDesc)
            order = ORDER_BY_DEFAULT_DESC;
        Cursor c = db().query(TABLE_NAME,null,null,null,null,null, order);
        if(c.getCount() == 0)
            return result;
        c.moveToFirst();
        while (!c.isAfterLast()) {
            result.add(makeBean(c));
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public static int deleteById(int id) {
        String TAG = "BookDatabase";
        Log.d(TAG, "deleteById input position : " + id);
        String[] whereArgs = {id + ""};
        return db().delete(TABLE_NAME, WHERE_BY_ID, whereArgs);
    }

    public String call(int position){
        Cursor c = db().rawQuery("select * from "+WHERE_BY_ID, null);
       c.moveToPosition(position);
        String phonenumber = "tel:"+ c.getString(c.getColumnIndex(PHONENUMBER));

        return phonenumber;
    }

}
