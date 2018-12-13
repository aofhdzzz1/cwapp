package com.example.chahyunbin.cwapp.Bible;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chahyunbin.cwapp.Database.DatabaseHelper;
import com.example.chahyunbin.cwapp.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Bible extends Activity{


    private BibleAdapter adapter;


    int bookposition = 0;
    int chapterposition = 0;
    ArrayList<Integer> chapter;
    String booktext ;
    int chaptertext;
    TextView textView;
    ListView listView;
    static public int globProg = 50 ;

    TextView number;
    SeekBar bar;
    boolean visable = false;
    DatabaseHelper mDBHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bible);
        Log.d("bible", "1: ");
        mDBHelper = new DatabaseHelper(this);
        //Check exists database

        Log.w("bible", "4");
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == database.exists()) {
            Log.w("bible", "5");
            mDBHelper.getReadableDatabase();
            //Copy db
            if (copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
                Log.d("bible", "bible copy success");
            } else {
                Log.d("bible", "bible copy fail");
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        textView = (TextView) findViewById(R.id.book);
        number = (TextView) findViewById(R.id.number);





        MakeBook();


        bar = (SeekBar) findViewById(R.id.seekBar);
        number.setText("" + globProg);

        final Button fontSizeChange = (Button) findViewById(R.id.fontchangebtn);
        fontSizeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visable == false) {
                    bar.setVisibility(View.VISIBLE);
                    visable = true;
                    fontSizeChange.setText("Close Seekbar");
                    bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        int Blast;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            globProg = seekBar.getProgress();
                            Log.d("bible", "globProg " + globProg);
                            Blast = progress;
                            listView.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                            number.setText("" + globProg);


                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            globProg = seekBar.getProgress();


                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            globProg = seekBar.getProgress();


                        }
                    });

                } else {
                    bar.setVisibility(View.INVISIBLE);
                    visable = false;
                    fontSizeChange.setText("Size Change");
                }

            }
        });

    }

    public void MakeBook(){
        Spinner bibleSpinner = (Spinner)findViewById(R.id.bibleSpinner);
        final ArrayList<String> book;
        book = mDBHelper.getListBook();
        final ArrayAdapter<String> bibleadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,book);
        bibleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bibleSpinner.setAdapter(bibleadapter);
        bibleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookposition = position +1;
                Log.d("bible", "Makebook : success");
                MakeChapter(bookposition);
                booktext = book.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        }

    public void MakeChapter(int book) {
        Spinner chapterSpinner = (Spinner)findViewById(R.id.chepterSpinner);
        chapter =  new ArrayList<Integer>();
        chapter = mDBHelper.getChapter(book);
        Log.d("bible", "chapter : "+chapter.size());
        ArrayAdapter<Integer> chapteradapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,chapter);
        chapteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapterSpinner.setAdapter(chapteradapter);
        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chapterposition = position +1;
                //성경 listView 띄우기
                Log.d("bible", "Makechapter : success ");
                loadVerseFromDB(bookposition,chapterposition);


                chaptertext = chapter.get(position);

                //verse 선택지 만들기
                MakeVerse(chapterposition);

                chaptertext = chapter.get(position);

                if (booktext != null && chaptertext != 0)
                    textView.setText(booktext + " " + chaptertext+ "장");
                else
                    textView.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        }

    private void MakeVerse(int chapterposition) {
        ArrayList<Integer> verse;
        verse = mDBHelper.getVerse(bookposition,chapterposition);
        Spinner verseSpinner = (Spinner)findViewById(R.id.verseSpinner);
        ArrayAdapter<Integer> verseadapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,verse);
        verseadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        verseSpinner.setAdapter(verseadapter);

        verseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listView.setSelection(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void loadVerseFromDB(int book,int chapter){
        Log.d("bible", "loadVerseFromDB: book = "+book+" chapter = "+ chapter);
        ArrayList<BibleItem> verse = mDBHelper.loadByVerse(book,chapter);
        adapter = new BibleAdapter();
        listView = (ListView) findViewById(R.id.biblelist);


        for(BibleItem bibleItem : verse){
            adapter.add(bibleItem);
        }
       // adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private boolean copyDatabase(Context context) {
        try {
            Log.w("MainActivity","1");
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
