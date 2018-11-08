package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Bible extends Activity{
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bible);




        ArrayList<String> book, chapter, verse;
        book = new ArrayList<String>();
        chapter = new ArrayList<String>();
        verse = new ArrayList<String>();







        Spinner bibleSpinner = (Spinner)findViewById(R.id.bibleSpinner);
        Spinner chapterSpinner = (Spinner)findViewById(R.id.chepterSpinner);
        Spinner verseSpinner = (Spinner)findViewById(R.id.verseSpinner);

        ArrayAdapter<String> bibleadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,book);
        ArrayAdapter<String> chapteradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,chapter);
        ArrayAdapter<String> verseadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, verse);

        bibleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        verseadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bibleSpinner.setAdapter(bibleadapter);
        chapterSpinner.setAdapter(chapteradapter);
        verseSpinner.setAdapter(verseadapter);



    }
}
