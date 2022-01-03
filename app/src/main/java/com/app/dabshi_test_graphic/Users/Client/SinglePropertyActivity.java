package com.app.dabshi_test_graphic.Users.Client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import dabshi_test_graphic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class SinglePropertyActivity extends AppCompatActivity {

    private TextView titleHolder, propertyTitle, propertyPrice, propertyLocation, propertyRooms, propertyDescription, propertyOwnername, descriptionTitle;
    private Button returnBtn, callBtn;
    private String ownerNumber;
    private DatabaseReference ownerInfo;
    private ImageView image, profileOwnerImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_property);

        ownerInfo = FirebaseDatabase.getInstance().getReference("Users").child("Owner");
        //initialisation of views
        initView();
        //Calling property information
        setPropertyInfo();

        returnBtn.setOnClickListener(view -> {
            onBackPressed();
        });


    }


    private void setPropertyInfo() {
        // Getting information from PropertyAdapter
        String idOwner = getIntent().getExtras().getString("IdOwner");
        String idProperty = getIntent().getExtras().getString("IdProperty");
        String title = getIntent().getExtras().getString("Title");
        String price = getIntent().getExtras().getString("Price");
        String location = getIntent().getExtras().getString("Location");
        String rooms = getIntent().getExtras().getString("Rooms");
        String description = getIntent().getExtras().getString("Description");
        String imageLink = getIntent().getExtras().getString("ImageLink");


        //Setting Property information to the views
        titleHolder.setText(title);
        propertyTitle.setText(title);
        propertyPrice.setText(price + " DT");
        propertyLocation.setText(location);
        propertyRooms.setText(rooms);
        propertyDescription.setText(description);
        Picasso.get().load(imageLink).placeholder(R.drawable.load).into(image);


        //Getting the owner information from the database
        ownerInfo.child(idOwner).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                propertyOwnername.setText(snapshot.child("username").getValue().toString());
                ownerNumber = snapshot.child("phone").getValue().toString();
                //set owner Image
                Picasso.get().load(snapshot.child("profileImage").getValue().toString())
                        .placeholder(R.drawable.load).into(profileOwnerImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Hide description label if there is no description
        if (description.equals("")) {
            descriptionTitle.setVisibility(View.GONE);
        }
        //Call Button
        callBtn.setOnClickListener(view -> {

            //check permission from user
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        1);
            } else {
                // if permission accepted client will call
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + ownerNumber));
                startActivity(i);
            }
        });

    }

    private void initView() {
        titleHolder = findViewById(R.id.singlePropertyTitleHolder);
        propertyTitle = findViewById(R.id.singlePropertyTitle);
        propertyPrice = findViewById(R.id.singlePropertyPrice);
        propertyLocation = findViewById(R.id.singlePropertyLocation);
        propertyRooms = findViewById(R.id.singlePropertyRooms);
        propertyDescription = findViewById(R.id.singlePropertyDescription);
        descriptionTitle = findViewById(R.id.clientSingleDescription);
        propertyOwnername = findViewById(R.id.clientSingleOwnername);
        callBtn = findViewById(R.id.callBtn);
        returnBtn = findViewById(R.id.client_SingleProperty_return);
        image = findViewById(R.id.client_SingleProperty_image);
        profileOwnerImage = findViewById(R.id.client_single_profil_img);


    }


}