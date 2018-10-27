package com.example.chahyunbin.cwapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BibleItemView extends LinearLayout {

    TextView paragraphText, sentenceText;

    public BibleItemView(Context context) {
        super(context);
        info(context);
    }

    public BibleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        info(context);
    }

    private void info(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bible_item,this,true);

        paragraphText = (TextView)findViewById(R.id.paragraph);
        sentenceText = (TextView)findViewById(R.id.sentence);


    }

    public void setParagraph(String paragraph){
        paragraphText.setText(paragraph);
    }
    public void setSentence(String sentence){
        sentenceText.setText(sentence);
    }

}
