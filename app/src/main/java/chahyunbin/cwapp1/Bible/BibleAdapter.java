package chahyunbin.cwapp1.Bible;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import chahyunbin.cwapp1.R;

public class BibleAdapter extends BaseAdapter {

   // private  ArrayList<BibleVerse> items = new ArrayList<>();
    private List<BibleItem> items = new ArrayList<>();
    private Context mContext;

    public BibleAdapter() {
    }

    public BibleAdapter(Context mcontext, List<BibleItem> mBibleItem) {
        this.mContext = mcontext;
        this.items = mBibleItem;

    }
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add (BibleItem bibleItem){
        items.add(bibleItem);
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
        BibleItem bean = items.get(position);
        return bean.getChapter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       BibleItem bean = items.get(position);

       ViewHolderItem holder;

           convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bible_item,null);
           holder = new ViewHolderItem();



           holder.verseText = (TextView)convertView.findViewById(R.id.verse);
           holder.contentsText = (TextView)convertView.findViewById(R.id.content);


           //글씨크기변경
           holder.verseText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Bible.globProg);
           holder.contentsText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Bible.globProg );

           convertView.setTag(holder);

           holder = (ViewHolderItem) convertView.getTag();

           holder.verseText.setText("");

           holder.contentsText.setText("");

           holder.verseText.setText(String.valueOf(bean.getVerse()));

           holder.contentsText.setText(String.valueOf(bean.getContent()));


        holder.verseText.setText(String.valueOf(bean.getVerse()));
        holder.contentsText.setText(String.valueOf(bean.getContent()));

       return convertView;
    }

    public static class ViewHolderItem {
        public static TextView contentsText;
        public static TextView verseText;
    }


}
