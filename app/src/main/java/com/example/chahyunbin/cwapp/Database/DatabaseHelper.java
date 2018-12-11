package com.example.chahyunbin.cwapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chahyunbin.cwapp.Bible.BibleItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // 필드
    public final static String TABLE_NAME = "bible";
    public static final String BOOK = "book";
    public static final String CHAPTER = "chapter";
    public static final String VERSE = "verse";
    public static final String CONTENT ="content";
    private static final String ORDER_BY_DEFAULT = VERSE + " asc";
    public static final String DBNAME = "bible.db";
    public static final String DBLOCATION = "/data/data/com.example.chahyunbin.cwapp/databases/";
    private static Context mContext;
    public static SQLiteDatabase mDatabase;


    public DatabaseHelper(Context context) {

        super(context, DBNAME, null, 1);
        Log.d("bible", "2: ");
        this.mContext = context;
        Log.d("bible", "3: ");
    }



    public static void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }

    public List<BibleItem> getListProduct() {
        BibleItem bibleItem = null;
        List<BibleItem> bibleItems = new ArrayList<BibleItem>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM bible ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bibleItem = new BibleItem(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getString(3));
            bibleItems.add(bibleItem);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return bibleItems;
    }

    public static ArrayList<String> getListBook(){
        ArrayList<String> book = new ArrayList<>();
        String bookkr[] = {"창세기", "출애굽기", "레위기", "민수기","신명기","여호수아","사사기","룻기","사무엘상","사무엘하","열왕기상","열왕기하","역대상","역대하","에스라","느헤미야","에스더","욥기","시편","잠언","전도서",
        "아가","이사야","예레미야","예레미야애가","에스겔","다니엘","호세야","요엘","아모스","오바댜","요나","미가","나훔","하박국","스바냐","학개","스가랴","말라기","마태복음","마가복음","누가복음","요한복음","사도행전","로마서","고린도전서",
        "고린도후서","갈라디아서","에베소서","빌립보서","골로새서","데살로니가전서","데살로니가후서","디모데전서","디모데후서","디도서","빌레몬서","히브리서","야고보서","배드로전서","배드로후서","요한일서","요한이서","요한삼서","유다서","요한계시록"};

        for(int i = 0 ; i <  66; i++){
            book.add(bookkr[i]);

        }
        return book;
    }
    // 성경이 몇장까지 있는지 파악
    public static ArrayList<Integer> getChapter(int book){
        Log.d("bible", "book : "+book);
        ArrayList<Integer> chapter = new ArrayList<>();
        openDatabase();
        String SQL = "SELECT * FROM bible WHERE book = ?";
        int old = 0;
        String[] args = {book + ""};
        Cursor c = mDatabase.rawQuery(SQL,args);
        Log.d("bible", "getChapter.Cursor c : "+c.getCount());
        c.moveToFirst();
        //같지 않는 장수 저장
        for(int i = 0; i < c.getCount() ; i++){
            if (old != c.getInt(1)) {
                old = c.getInt(1);
                chapter.add(old);
            }
            c.moveToNext();
        }
        return chapter;
    }

    //장까지 선택했을 때 성경 로딩
    public ArrayList<BibleItem> loadByVerse(int book, int chapter){
        ArrayList<BibleItem> result = new ArrayList<>();
        openDatabase();
        Cursor c = mDatabase.query(TABLE_NAME,new String[] {"verse","content"}, "book ="+book+" and chapter = ?"
                ,new String[] {chapter+""},null,null,null);
        Log.d("bible", "loadByVerse: Cursor = "+c.getCount());
        if(c.getCount() == 0)
            return result;
        c.moveToFirst();
        while(!c.isAfterLast()){
            result.add(makeBean(c));
            c.moveToNext();
        }
        c.close();

        return result;
    }

    private BibleItem makeBean(Cursor c) {
        int verse = c.getInt(c.getColumnIndex(VERSE));
        String content = c.getString(c.getColumnIndex(CONTENT));
        return new BibleItem(verse,content);
    }


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+ PeopleTable.TABLE_NAME);
        db.execSQL(PeopleTable.createSql);
    }


    public ArrayList<Integer> getVerse(int book, int chapter) {
        ArrayList<Integer> verse = new ArrayList<Integer>();
        String SQL = "SELECT * FROM bible WHERE book = " +book+" AND chapter = ?";
        String arg1[] = {chapter+""};
        String arg2[] = {book+""};
        int old = 0;

        Cursor c = mDatabase.rawQuery(SQL,arg1);
        c.moveToFirst();
        for(int i = 0; i<c.getCount(); i++){
            if(old != c.getInt(2)){
                old = c.getInt(2);
                verse.add(old);
            }
            c.moveToNext();
        }
        return verse;
    }

    public static class BaseTable {
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
}
