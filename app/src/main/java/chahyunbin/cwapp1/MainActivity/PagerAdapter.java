package chahyunbin.cwapp1.MainActivity;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

import chahyunbin.cwapp1.R;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {
    private List<AD> mAdList;
    Context context ;
    LayoutInflater inflater;

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
        ((ViewPager)container).removeView((View) object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        AD ad = mAdList.get(position);

        View view = inflater.inflate(R.layout.viewpager_childview,container,false);

        ImageView ADimage = (ImageView)view.findViewById(R.id.img_viewpager_childimage);

        Picasso.with(context)
                .load(ad.getmADUri())
                .into(ADimage);

        container.addView(view);

        return view;


    }
}
