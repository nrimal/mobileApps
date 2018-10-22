package edu.osu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.LinkedList;
import java.util.List;

public class ClosetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Closet_Frag_Activity";

    String Username;
    FirebaseDatabase database;
    List<ClothingInstance> Shoes;

    TextView ShoesText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up the login form.
        Log.d(TAG, this + ": onCreateView()");
        View v =  inflater.inflate(R.layout.activity_closet_fragment, container, false);


        Button mUsernameRegisterButton = v.findViewById(R.id.btnAddItem);
        mUsernameRegisterButton.setOnClickListener(this);

        Username = FirebaseInstanceId.getInstance().getId()+"";
        database = FirebaseDatabase.getInstance();

        ShoesText = v.findViewById(R.id.showShoes);

        getClothes();
        return v;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, this + ": onCreate()");
//        setRetainInstance(true);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAddItem:
//                attemptLogin();
                Intent ClosetIntent = new Intent(getActivity(),AddClosetItemActivity.class);
                startActivity(ClosetIntent);
                break;
        }
    }


    private void getClothes(){
        DatabaseReference myRef = database.getReference("User_"+Username+"/Closet");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, dataSnapshot.toString());
                Shoes = new LinkedList<ClothingInstance>();
                for (DataSnapshot clothingSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, clothingSnapshot.toString());
                    ClothingInstance item = clothingSnapshot.getValue(ClothingInstance.class);

                    if(item.Type.equals("Shoes")){
                        Shoes.add(item);
                        ShoesText.append("Shoes("+item.Color+ ")\t From :" + item.Store + "\n");
                    }

                }

                Log.d(TAG, "Shoes :"+Shoes.size());


                //ClosetActivity = dataSnapshot.getValue(Clothes.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private static class ClothingInstance {
        public String Type;
        public String Store;
        public String Color;
        public String Image;
        public ClothingInstance(){}
        public ClothingInstance(String Type, String Store, String Color, String Image) {
            this.Type = Type;
            this.Store = Store;
            this.Color = Color;
            this.Image = Image;
        }
    }

}

