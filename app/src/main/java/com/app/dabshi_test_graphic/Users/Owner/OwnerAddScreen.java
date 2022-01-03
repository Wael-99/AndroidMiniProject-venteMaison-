package com.app.dabshi_test_graphic.Users.Owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dabshi_test_graphic.Property.Property;
import dabshi_test_graphic.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static com.app.dabshi_test_graphic.Users.Owner.OwnerMainActivity.bottomNavigation;
import static com.app.dabshi_test_graphic.SplashScreenActivity.isInternetAvailable;


public class OwnerAddScreen extends Fragment {
    private EditText title, rooms, price, description;
    private Spinner location;
    private Button addbutton;
    private TextView updateBtn, imageName;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private StorageReference storageRef;
    private Uri filePath;
    private static String linkImage;
    private ProgressBar progressBar;

    // push database key for the property
    public static String idProperty;

    public OwnerAddScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_screen, container, false);

        database = FirebaseDatabase.getInstance().getReference("Property");
        auth = FirebaseAuth.getInstance();
        //Get storage reference
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://rento-test-graphic.appspot.com/").child("images/").child(auth.getCurrentUser().getUid());    //change the url acco

        title = v.findViewById(R.id.property_add_title);
        rooms = v.findViewById(R.id.property_add_room);
        location = v.findViewById(R.id.property_add_location);
        price = v.findViewById(R.id.property_add_price);
        description = v.findViewById(R.id.property_add_description);
        addbutton = v.findViewById(R.id.add_property_button);
        updateBtn = v.findViewById(R.id.property_add_picture);
        progressBar = v.findViewById(R.id.addProgressBar);
        imageName = v.findViewById(R.id.imageName);

        //disable add button until image added
        addbutton.setEnabled(false);

        handleAddClick();

        return v;
    }


    private void handleAddClick() {
        updateBtn.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);

        });
        addbutton.setOnClickListener(view -> {
            if (!isInternetAvailable()) {
                //disable add button until post successfully added
                addbutton.setEnabled(false);
                addProperty();
            } else {
                Toast.makeText(getContext(), "Check Your Connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProperty() {
        String titleTxt = title.getText().toString();
        String locationTxt = location.getSelectedItem().toString();
        String descriptionTxt = description.getText().toString();
        String roomsTxt = rooms.getText().toString();
        String priceTxt = price.getText().toString();
        idProperty = database.push().getKey();


        if (titleTxt.isEmpty()) {
            title.setError(" Need your name");
            title.requestFocus();
            return;
        }
        if (roomsTxt.isEmpty()) {
            rooms.setError("Forget to right rooms number");
            rooms.requestFocus();
            return;
        }

        if (roomsTxt.length() > 1) {
            rooms.setError("Entre a valid rooms number");
            rooms.requestFocus();
            return;
        }
        if (priceTxt.isEmpty()) {
            price.setError("Forget to set property price");
            price.requestFocus();
            return;
        }


        //Adding progresse bar until image is loaded
        progressBar.setVisibility(View.VISIBLE);

        // uploading the image to the storage if property data added to the database
        final StorageReference ref = storageRef.child(idProperty).child(filePath.getLastPathSegment()); // saving image in folder with  post id and under the name of the image
        UploadTask uploadTask = ref.putFile(filePath);
        // Test if the picture added successfully
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                // if upload didn't finish
                Toast.makeText(getContext(), "Couldn't upload your post", Toast.LENGTH_LONG).show();


            }
            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {

            // Handle unpicked image exception
            try {
//            getting image from gallery

                if (task.isSuccessful()) {
                    // Getting the downloaded URL
                    linkImage = task.getResult().toString();

                    // Create an instance of the Property class
                    Property property = new Property(auth.getCurrentUser().getUid(), idProperty, titleTxt, locationTxt, descriptionTxt, "s+" + roomsTxt, priceTxt, linkImage);

                    // Adding the instance to the database
                    // handle the failure exception from adding to the database
                    database.child(idProperty).setValue(property).addOnSuccessListener(aVoid -> {

                        // if image was successfully uploaded will return to the home page
                        bottomNavigation.show(1, true);
                        Toast.makeText(getContext(), "Add Succesfully", Toast.LENGTH_LONG).show();

                    }).addOnFailureListener(e -> Toast.makeText(getContext(), "Add Failed", Toast.LENGTH_LONG).show());

                } else {
                    // Handle failures
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Failed To Add The Post", Toast.LENGTH_LONG).show();
                    addbutton.setEnabled(true);
                    return;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


//
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();


            if (filePath != null) {

                //Setting the name of the image
                imageName.setVisibility(View.VISIBLE);
                // setting the image name from the gallery
                imageName.setText(filePath.getLastPathSegment() + ".jpg");
                // set the add button visible if image selected from the gallery ( if the there is an image path)
                addbutton.setEnabled(true);

            } else {
                Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
            }

        }
    }

}