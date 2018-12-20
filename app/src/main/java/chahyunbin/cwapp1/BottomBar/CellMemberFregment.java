package chahyunbin.cwapp1.BottomBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import chahyunbin.cwapp1.AdminMember.SingleAdapter;
import chahyunbin.cwapp1.MainActivity;

import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.Person;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CellMemberFregment extends Fragment {




    SwipeMenuListView listView;


    Button button;
    final String TAG = "BookDatabase";
    private int CALL_PERMISSION_CODE=1;
    Activity activity;

    String name, phone, age, month, day;
    public String key;

    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<Person> list;
    SingleAdapter adapter;
    Person person;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.adminmember,container,false);




        listView = (SwipeMenuListView)view.findViewById(R.id.listView);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        list = new ArrayList<>();
        adapter = new SingleAdapter();

        ref.child("User/"+MainActivity.email+"/Members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Person person = snapshot.getValue(Person.class);
                    person.Key = snapshot.getKey();
                    list.add(person);
                    adapter.add(person);
                }
                if(adapter.getCount() == 0){
                    Toast.makeText(getActivity(), "셀원을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        SwipeMenuCreator creator  = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xdf, 0x01,
                        0x3a)));
                // set item width
                deleteItem.setWidth(300);
                // set item title

                // set item title font color
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);

                // create "delete" item
                SwipeMenuItem callItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                callItem.setBackground(new ColorDrawable(Color.rgb(0x04,
                        0xb4, 0x04)));
                // set item width
                callItem.setWidth(300);
                // set a icon
                callItem.setIcon(R.drawable.ic_phone);

                // add to menu
                menu.addMenuItem(callItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.d(TAG, "position = " + position);
                switch (index) {
                    case 0:
                        // delete

                        Person person = list.get(position);
                        list.remove(position);
                        ref.child("User/"+MainActivity.email+"/Members").child(person.Key).removeValue();
                        Toast.makeText(getActivity(), "성공적으로 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // phone
                        String phonenumber = adapter.getItemPhone(position);
                        Log.d("Firebase", "phone : "+ phonenumber);
                        String tel = "tel:"+phonenumber;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(tel));
                        try {
                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(getActivity(), "설정에서 전화 걸기 권한을 부여해주세요.", Toast.LENGTH_SHORT).show();
                        }
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String name = adapter.getItem_Name(i);
               Toast.makeText(getActivity(), "선택" + name, Toast.LENGTH_SHORT).show();

            }
        });

            return view;
    }





//    private void loadAllFromDB() {
//        ArrayList<Person> people = peopleTable.loadByDate(true);
//        for (Person person : people) {
//
//            adapter.add(person);
//        }
//        adapter.notifyDataSetChanged();
//    }


}

