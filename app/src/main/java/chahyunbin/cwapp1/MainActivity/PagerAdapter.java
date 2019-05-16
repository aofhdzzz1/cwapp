package chahyunbin.cwapp1.MainActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import chahyunbin.cwapp1.R;

public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    LayoutInflater inflater;
    private List<AD> mAdList;
    Context context;

    public PagerAdapter(Context context, List<AD> mAdList) {
        this.mAdList = mAdList;
        this.context = context;

        inflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return mAdList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //inflate view

        View view = inflater.inflate(R.layout.viewpager_childview,container,false);

        //View
        ImageView imageView = (ImageView)view.findViewById(R.id.img_viewpager_childimage);

        //set data

        Picasso.with(context).load(mAdList.get(position).getmAdad()).into(imageView);

        return view;
    }
}
