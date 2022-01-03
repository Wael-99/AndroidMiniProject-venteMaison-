package com.app.dabshi_test_graphic.Users.Owner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dabshi_test_graphic.R;
import com.squareup.picasso.Picasso;

public class OwnerSingleProperty extends AppCompatActivity {

    private TextView titleHolder, propertyTitle, propertyPrice, propertyLocation, propertyRooms, propertyDescription;
    private Button returnBtn, editBtn;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_single_property);
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

    }

    private void initView() {
        titleHolder = findViewById(R.id.ownerPropertyTitleHolder);
        propertyTitle = findViewById(R.id.ownerPropertyTitle);
        propertyPrice = findViewById(R.id.ownerPropertyPrice);
        propertyLocation = findViewById(R.id.ownerPropertyLocation);
        propertyRooms = findViewById(R.id.ownerPropertyRooms);
        propertyDescription = findViewById(R.id.ownerPropertyDescription);
        returnBtn = findViewById(R.id.owner_SingleProperty_return);
        image = findViewById(R.id.owner_SingleProperty_image);
//        editBtn = findViewById(R.id.owner_SingleProperty_edit);
    }

    private void replace(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

    }
}