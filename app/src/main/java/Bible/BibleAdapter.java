package Bible;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chahyunbin.cwapp.Database.BibleTable;
import com.example.chahyunbin.cwapp.R;
import com.example.chahyunbin.cwapp.model.BibleVerse;

import java.util.ArrayList;

public class BibleAdapter extends BaseAdapter {
    private  ArrayList<BibleVerse> items = new ArrayList<>();

    public BibleAdapter() {
        super();

    }

    public void add (BibleVerse bibleVerse){
        items.add(bibleVerse);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        BibleVerse bean = items.get(position+1);
        return bean.verse;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       BibleVerse bean = items.get(position);

       ViewHolderItem holder;
       if(convertView == null){
           convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bible_item,null);
           holder = new ViewHolderItem();
           holder.verseText = (TextView)convertView.findViewById(R.id.verse);
           holder.contentsText = (TextView)convertView.findViewById(R.id.content);
           convertView.setTag(holder);
       }else
           holder = (ViewHolderItem) convertView.getTag();

       holder.verseText.setText(String.valueOf(bean.verse));
       holder.contentsText.setText(bean.content);

       return convertView;
    }

    static class ViewHolderItem {
        TextView contentsText;
        TextView verseText;
    }


}
