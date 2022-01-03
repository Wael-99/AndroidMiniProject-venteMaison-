package com.app.dabshi_test_graphic.Users.Owner;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dabshi_test_graphic.Property.Property;
import dabshi_test_graphic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.app.dabshi_test_graphic.Users.Owner.OwnerMainActivity.bottomNavigation;


public class OwnerHomeFragment extends Fragment {

    private DatabaseReference propertydatabase, UserDatabase;
    private RecyclerView recyclerView;
    private OwnerHomeAdapter OwnerHomeAdapter;
    private ArrayList<Property> list;
    private LinearLayout addview;
    private ImageView add;
    private TextView ownerPlaceHolder;
    private FirebaseAuth auth;
    private ProgressDialog pd; // need to change

    public OwnerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        recyclerView = v.findViewById(R.id.property_owner_list);
        ownerPlaceHolder = v.findViewById(R.id.ownerPlaceHolder);
        addview = v.findViewById(R.id.addview);
        add = v.findViewById(R.id.addbutton);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        auth = FirebaseAuth.getInstance();

        //progress dialog
        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
//        pd.show();
        Log.e("im here", "this is the message");

        UserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        propertydatabase = FirebaseDatabase.getInstance().getReference("Property");
        list = new ArrayList<>();
        OwnerHomeAdapter = new OwnerHomeAdapter(v.getContext(), list);
        recyclerView.setAdapter(OwnerHomeAdapter);


        add.setOnClickListener(view -> {
            replace(new OwnerAddScreen());
            bottomNavigation.show(2, true);

        });

        ValueListener();

        return v;
    }

    private void ValueListener() {

        // Get username and set it i the view
        UserDatabase.child("Owner").child(auth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ownerPlaceHolder.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

//         Getting data from firebase realtime database
        propertydatabase.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshotChild : snapshot.getChildren()) {
                    // check from the owner of the property

                    if (snapshotChild.child("idowner").getValue().equals(auth.getCurrentUser().getUid())) {

//                        get properties from database and set in Property instance
                        Property property = snapshotChild.getValue(Property.class);
                        //add property to recycle view in the first position
                        list.add(0, property);
                    }
                }
                //notifyItemInserted(list.indexOf(property));
                OwnerHomeAdapter.notifyDataSetChanged();
                //animate the appear of the view
                recyclerView.scheduleLayoutAnimation();

                // if there is no post add button will appearA
                if (list.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    addview.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//                pd.dismiss();
    }

    private void replace(Fragment fragment) {

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

    }

}