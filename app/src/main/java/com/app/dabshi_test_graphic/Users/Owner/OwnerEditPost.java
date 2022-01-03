package com.app.dabshi_test_graphic.Users.Owner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import dabshi_test_graphic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.app.dabshi_test_graphic.Users.Owner.OwnerHomeAdapter.propertyId;
import static com.app.dabshi_test_graphic.Users.Owner.OwnerMainActivity.bottomNavigation;
import static com.app.dabshi_test_graphic.SplashScreenActivity.isInternetAvailable;


public class OwnerEditPost extends Fragment {
    private EditText title, rooms,price, description;
    private Spinner location;
    private Button editBtn;
    private DatabaseReference database;
    private FirebaseAuth auth;


    public OwnerEditPost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_post, container, false);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference("Property");

        title = v.findViewById(R.id.property_edit_title);
        rooms = v.findViewById(R.id.property_edit_room);
        location = v.findViewById(R.id.property_edit_location);
        price = v.findViewById(R.id.property_edit_price);
        description = v.findViewById(R.id.property_edit_description);
        editBtn = v.findViewById(R.id.edit_property_button);


        editBtn.setOnClickListener(view -> {

            String titleTxt = title.getText().toString();
            String locationTxt = location.getSelectedItem().toString();
            String descriptionTxt = description.getText().toString();
            String roomsTxt = rooms.getText().toString();
            String priceTxt = price.getText().toString();

            if (titleTxt.isEmpty()) {
                title.setError("Forget to set a title ");
                title.requestFocus();
                return;
            }
            if (roomsTxt.isEmpty()) {
                rooms.setError("Forget to right rooms number");
                rooms.requestFocus();
                return;
            }

            if (roomsTxt.length() > 1 && roomsTxt.charAt(0) > 5) {
                rooms.setError("Entre a valid rooms number");
                rooms.requestFocus();
                return;
            }

            if (priceTxt.isEmpty()) {
                price.setError("Forget to set property price");
                price.requestFocus();
                return;
            }
            if (priceTxt.length() > 4 || Integer.parseInt(priceTxt) < 50) {
                price.setError("Set price between 50 DT and 1500 DT ");
                price.requestFocus();
                return;
            }
            if (descriptionTxt.isEmpty()) {
                description.setError("Add some description");
                description.requestFocus();
                return;
            }

            if (!isInternetAvailable()){

                database.child(propertyId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        database.child("title").setValue(titleTxt);
                        database.child("location").setValue(locationTxt);
                        database.child("price").setValue(priceTxt);
                        database.child("rooms").setValue(roomsTxt);
                        database.child("description").setValue(descriptionTxt);
                        bottomNavigation.show(1, true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }else {
                Toast.makeText(getContext(),"Check Your Connexion",Toast.LENGTH_SHORT).show();
            }



        });
        return v;
    }

}