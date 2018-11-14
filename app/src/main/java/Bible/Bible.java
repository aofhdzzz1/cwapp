package Bible;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chahyunbin.cwapp.AdminMember.SingleAdapter;
import com.example.chahyunbin.cwapp.Database.BibleTable;
import com.example.chahyunbin.cwapp.Database.PeopleTable;
import com.example.chahyunbin.cwapp.R;
import com.example.chahyunbin.cwapp.model.BibleVerse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bible extends Activity{

    BibleTable bibleTable;

    BibleAdapter adapter;

    int bookposition = 0;
    int chapterposition = 0;

    String booktext ;
    int chaptertext;
    TextView textView;
    ListView listView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bible);
        bibleTable = bibleTable.instance(getApplicationContext());







        MakeBook();
      textView = (TextView)findViewById(R.id.book);









    }

    public void MakeVerse(int chapter) {
        ArrayList<Integer> verse;
        verse = bibleTable.getVerse(bookposition,chapterposition);
        Spinner verseSpinner = (Spinner)findViewById(R.id.verseSpinner);
        ArrayAdapter<Integer> verseadapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, verse);
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

    public void MakeChapter(int book) {
        final ArrayList<Integer>  chapter;
        final Spinner chapterSpinner = (Spinner)findViewById(R.id.chepterSpinner);
        chapter = bibleTable.getChapter(book);
        final ArrayAdapter<Integer> chapteradapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,chapter);
        chapteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapterSpinner.setAdapter(chapteradapter);
        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chapterposition = position+1;
                //성경 listview 띄우기
                adapter = new BibleAdapter();
                listView = (ListView) findViewById(R.id.biblelist);
                listView.setAdapter(adapter);
                loadVerseFromDB(bookposition, chapterposition);

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

    public void MakeBook() {
        Spinner bibleSpinner = (Spinner)findViewById(R.id.bibleSpinner);
        final ArrayList<String> book;
        book = bibleTable.getBook();
        final ArrayAdapter<String> bibleadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,book);
        bibleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bibleSpinner.setAdapter(bibleadapter);
        bibleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookposition = position+1;

                //chapter스피너 만들기
                MakeChapter(bookposition);
                booktext = book.get(position);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadVerseFromDB(int book,int chapter){

        ArrayList<BibleVerse> verse = bibleTable.loadByVerse(book,chapter);

        for(BibleVerse bibleVerse : verse){
            adapter.add(bibleVerse);
        }
        adapter.notifyDataSetChanged();
    }


}
