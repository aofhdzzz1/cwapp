package chahyunbin.cwapp1.AdminMember;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.Person;
import chahyunbin.cwapp1.model.User;

import java.util.ArrayList;

public class SingleAdapter extends BaseAdapter {
    private static ArrayList<User> items = new ArrayList<User>();



    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(User user) {

        items.add(user);
    }

    public static void insert(User user) {

        items.add(0, user);
    }

    public void update(User user) {
        for (int i = 0; i < getCount(); i++) {
            User bean = items.get(i);
            if(bean.id.equals(user.id)) {
                items.remove(i);
                items.add(i, user);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void delete(int id) {
        String TAG ="BookDatabase";
        Log.d(TAG, "delete: ");
        for (int i = 0; i < getCount(); i++) {
            User bean = items.get(i);
            if(bean.id.equals(id + "")) {
                items.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }
    public String getItemPhone(int position){
        User bean = items.get(position);
        return bean.getPhone().trim();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public String getItem_ID(int position){
        User bean = items.get(position);
        return bean.id;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        User bean = items.get(position);
        ViewHolderItem holder;
        if (convertview == null) {
            int resId = R.layout.member_item2;
            convertview = LayoutInflater.from(parent.getContext()).inflate(resId, null);
            holder = new ViewHolderItem();
            holder.textname = (TextView) convertview.findViewById(R.id.nameView);
            holder.textphonenumber = (TextView) convertview.findViewById(R.id.phonenumberView);
            holder.imageView =(ImageView) convertview.findViewById(R.id.image);
            convertview.setTag(holder);
        } else
            holder = (ViewHolderItem) convertview.getTag();

        holder.textname.setText(bean.getName());
        holder.textphonenumber.setText(bean.getPhone());
        holder.imageView.setImageResource(R.mipmap.ic_adminicon_round);

        return convertview;
    }


    public String getItem_Name(int position) {
       User bean = items.get(position);
        return bean.getName();
    }
}

 class ViewHolderItem {
    TextView textname;
    TextView textphonenumber;
    ImageView imageView;

}
