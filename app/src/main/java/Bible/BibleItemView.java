package Bible;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chahyunbin.cwapp.R;

public class BibleItemView extends LinearLayout {

    TextView verseText, contentText;

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

        verseText = (TextView)findViewById(R.id.verse);
        contentText = (TextView)findViewById(R.id.content);
    }

    public void setVerse(int verse) {
        verseText.setText(verse);
    }

    public void setContent(String content) {
        contentText.setText(content);
    }
}

